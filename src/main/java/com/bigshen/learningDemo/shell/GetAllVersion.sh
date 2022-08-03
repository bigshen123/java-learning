#!/bin/bash
# 通过docker ps -a 命令获取当前运行的镜像的相关信息
container_ids=$(docker ps -a | awk -F " " 'NR == 1 {next} {print $1}')
container_names=($(docker ps -a | awk -F " " 'NR == 1 {next} {print $2}' | awk -F "/" '{print $NF}' | awk -F ":" '{print $1}'))
container_versions=($(docker ps -a | awk -F " " 'NR == 1 {next} {print $2}' | awk -F "/" '{print $NF}' | awk -F ":" '{print $2}'))
i=0
echo -n "" >/var/cfg/nameAndVersions.txt
for container_id in $container_ids; do
    build_number=$(docker inspect "${container_id}" | grep -E "APP_BUILD_NUMBER|API_BUILD_NUMBER" | awk -F "=" '{print $2}' | sed 's/,$//' | sed 's/.$//g')
    if [ -z $build_number ]; then
        build_number="null"
    fi
    printf "%-20s %-40s %-20s \n" "${container_names[i]}" "${container_versions[i]}" "$build_number" >>/var/cfg/nameAndVersions.txt
    i=$(($i + 1))
done

# 特殊处理：获取四个二进制文件相关信息
binary_pkgs="kl-dms kl-netman kl-bdm kl-rdt"
for pkg_name in $binary_pkgs; do
    version=$($pkg_name -v | grep "^[vV]ersion" | awk -F ":" '{print $2}' | awk '{sub("^ *","");sub(" *$","");print}')
    commit_id=$($pkg_name -v | grep "^Git Commit Hash" | awk -F ":" '{print $2}' | awk '{sub("^ *","");sub(" *$","");print}')
    printf "%-20s %-40s %-20s \n" "$pkg_name" "${version}" "$commit_id" >>/var/cfg/nameAndVersions.txt
done
cat /var/cfg/nameAndVersions.txt
