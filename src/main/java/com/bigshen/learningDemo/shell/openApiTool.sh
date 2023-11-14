#!/bin/bash

if [ ! -f "project.properties" ]; then
  helpStr=$(
    cat <<EOF
  请在项目目录下创建 project.properties文件, 并配置如下值：
  NAME={项目名}# 如：NAME=cms
  PROJECT_NAME={项目名} #如：PROJECT_NAME=证书管理服务
  API_PATH={api路径} # 如：API_PATH=./openapi/openapi.json
  DESCRIPTION={项目描述} # 如：DESCRIPTION=提供证书管理接口
  VERSION={版本号} # 如：VERSION=1.0.0
EOF
  )
  echo -e "${helpStr}"
  exit 1
fi

source ./project.properties

if [ "${NAME}" == "" ]; then
  echo "project.properties中未配置NAME"
  exit 1
fi
if [ "${PROJECT_NAME}" == "" ]; then
  echo "project.properties中未配置PROJECT_NAME"
  exit 1
fi
baseName=$(basename "${API_PATH}")
ext="${baseName##*.}"
if [ "${API_PATH}" == "" ]; then
  echo "project.properties中未配置API_PATH"
  exit 1
fi
if [ ! -f "${API_PATH}" ]; then
  echo "${API_PATH}文件不存在"
  exit 1
fi
if [ "${ext}" == "json" ]; then
  echo "如果有format为json且type为string的，将进行type修改为object操作"
  sed -zEi 's/"type": "string",([^\n]*\n[^\n]*"format": "json")/"type": "object",\1/g' "${API_PATH}"
fi
if [ "${DESCRIPTION}" == "" ]; then
  echo "project.properties中未配置DESCRIPTION"
  exit 1
fi
if [ "${VERSION}" == "" ]; then
  echo "project.properties中未配置VERSION"
  exit 1
fi

# 生成php代码
rm -rf composer_dist/
packageName="koal/gcrp_sdk_${NAME,,}"
java -jar /opt/openapi-generator/modules/openapi-generator-cli/target/openapi-generator-cli.jar generate \
  -i ${API_PATH} \
  -g php \
  --package-name "${PROJECT_NAME}" \
  --invoker-package "GCRPS\\SDK\\${NAME^^}" \
  --artifact-version "${VERSION}" \
  -p composerPackageName="${packageName}" \
  --skip-validate-spec \
  -o composer_dist/

# 更改描述
cd composer_dist
sed -i 's/"description":.*/"description":"'${DESCRIPTION}'",/' composer.json

# 制作composer包上传到nexus
fileName="gcrp_sdk_$(date +%Y%m%d%H%M%S)"
composer archive --no-cache --format=zip --file="${fileName}"

curl -v --user 'deployment:deployment123' --upload-file "${fileName}.zip" "http://nexus3.koal.com:8081/repository/composer-snapshots/packages/upload/${packageName}/${VERSION}"

cd ../

# 生成java代码
rm -rf java_dist/
group_id="kl.gcrp"
artifact_id="${NAME}-sdk"
java -jar /opt/openapi-generator/modules/openapi-generator-cli/target/openapi-generator-cli.jar generate \
  -i ${API_PATH} \
  -g java \
  --package-name "${PROJECT_NAME}" \
  --invoker-package "${group_id}" \
  --artifact-version "${VERSION}" \
  -p groupId="${group_id}" \
  -p artifactId="${artifact_id}" \
  --skip-validate-spec \
  -o java_dist/


# 制作jar包上传到nexus
cd java_dist

echo -e  "task sourcesJar(type: Jar) {\n\tfrom sourceSets.main.java.srcDirs\n\tclassifier = 'sources'\n}\n" >> build.gradle

/bin/bash gradlew build
/bin/bash gradlew sourcesJar

# 查询删除release包
releases_res=$(curl --user 'deployment:deployment123' "http://nexus3.koal.com:8081/service/rest/v1/search?repository=maven-releases&group=${group_id}&name=${artifact_id}&version=${VERSION}" -s |grep "\"id\" :"|head -n 1)

if [ "${releases_res}" != "" ]; then
  component_id=$(echo "${releases_res}" | awk -F '"' '{print $4}')
  if [ "${component_id}" != "" ]; then
    echo "已存在相同版本包，开始删除"
    curl -v --user 'deployment:deployment123' -X DELETE "http://nexus3.koal.com:8081/service/rest/v1/components/${component_id}"
  fi
fi

# 推送release包
curl -v --user 'deployment:deployment123' "http://nexus3.koal.com:8081/service/rest/v1/components?repository=maven-releases" -F maven2.asset1=@build/libs/${artifact_id}-${VERSION}.jar -F maven2.asset1.extension=jar -F maven2.asset2=@build/libs/${artifact_id}-${VERSION}-sources.jar -F maven2.asset2.extension=jar -F maven2.asset2.classifier=sources -F maven2.groupId=${group_id} -F maven2.artifactId=${artifact_id} -F maven2.version=${VERSION} -F maven2.generate-pom=true

cd ../


newApiPath="${NAME}.${ext}"

cp -f "${API_PATH}" "${newApiPath}"

# 将api文档上传到debian.koal.com
smbclient //debian.koal.com/upload -N -D gcrp/api/ -c "mkdir ${NAME};cd ${NAME}; mkdir ${VERSION}" || true

smbclient //debian.koal.com/upload -N -D gcrp/api/${NAME}/${VERSION}/ -c "prompt OFF; recurse ON; mput ${newApiPath}"

