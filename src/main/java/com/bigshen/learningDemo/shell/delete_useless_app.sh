#!/bin/bash
## 删除网关无用资源 仅保留需要的资源
set -e

useless_app_id=$1
dir_file=/opt/all_appid.txt


if [ -z "$1" ]; then
  echo "Error: 参数不能为空，请输入无需删除的资源id"
  exit 1
fi

curl "http://127.0.0.1:60700/apps?fields[apps]=appId&neq[appId]=${useless_app_id}" >${dir_file}

count=$(cat ${dir_file} | jq -r .data.totalItems)
if [ ${count} != "0" ]; then
    arr=$(cat ${dir_file} | jq -r .data.items)
    echo ${arr} >${dir_file}
    count=$((${count} - 1))
    for (( ; count >= 0; count--)); do
        app_id=$(jq -r .[${count}].appId ${dir_file})
        curl -X DELETE http://127.0.0.1:60700/apps/${app_id}
    done
fi
