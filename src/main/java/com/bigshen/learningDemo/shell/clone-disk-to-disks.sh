#!/bin/bash

#set -e

export LC_ALL=en_US.UTF-8
export LANG=en_US.UTF-8


welcome(){
    local -r version="2311.1"
    echo
    echo "###  $(basename $0)-V${version} ### "
    echo
    echo "欢迎使用本工具，本工具可以将qcow2镜像或者源磁盘批量写入到有效的目标磁盘"
    echo "如果工具有什么不足或者对工具有什么改进的想法，欢迎向 http://git.koal.com/gaodq/public-miscs/-/edit/main/clone-disk-to-disks.sh 发起 Merge Request "
    echo
    echo "###  $(basename $0)-V${version} ### "
    echo
}

help(){
    echo
    echo "### HELP BEGIN ###"
    echo
    echo "- 依赖条件:"
    echo "- 1. 使用 root 运行"
    echo "- 2. 必须已安装 curl "
    echo "- 3. 依赖 qemu-utils qemu-img clonezilla 等工具包，但会尝试自动检测和安装该工具包"
    echo
    echo "- 安装方法："
    echo "- 1. curl -sSkLO http://git.koal.com/gaodq/public-miscs/-/raw/main/clone-disk-to-disks.sh && chmod +x ./clone-disk-to-disks.sh"
    echo
    echo "- 使用说明："
    echo "- 1. 克隆qcow2文件到所有已连接的有效磁盘： $0 /path/to/source.qcow2"
    echo "- 2. 克隆网络上的qcow2文件到所有已连接的有效磁盘： $0 http://images.com/source.qcow2"
    echo "      source.qcow2 会先缓存到 /tmp/$(basename "$0")/ , 再次使用该命令的时候会直尝试使用缓存"
    echo "- 3. 克隆磁盘到所有已连接的有效磁盘： $0 /dev/sdxyz"
    echo
    echo "### HELP END  ###"
    echo
}

log_info(){
  # blue
  echo -ne "\e[34mINFO\e[0m: $*"
}
log_warn(){
  # yellow
  echo -ne "\e[33mWARN\e[0m: $*"
}

log_error(){
  # red
  echo -ne "\e[31mERROR\e[0m: $*"
}

trim() {
    local s="$1"
    # 删除前导空格
    s="${s#"${s%%[![:space:]]*}"}"
    # 删除尾部空格
    s="${s%"${s##*[![:space:]]}"}"
    echo -n "$s"
}

file_size(){
    du -b $1 | awk '{print $1}' | (read line; trim $line)
}

url_resource_size(){
    curl -skLI --connect-timeout 3 "$1" | grep Content-Length | awk -F ": " '{print $2}' | (read line; trim $line)
}


declare -A cache_disk_display_caption
disk_display_caption() {
    local d=$1
    local s=$2
    if [ "${cache_disk_display_caption[$1]}" != "" ]; then
        echo -n ${cache_disk_display_caption[$1]}
        return
    fi
    local product=$(lshw -c disk 2>/dev/null | grep $d -B4 | grep product | awk -F ":" '{print $2}' | (read line; trim $line) |sed "s/[\"\ ]/_/g")
    local Model_Number=$(hdparm -I $d | grep "Model Number" | awk -F ":" '{print $2}' | (read line; trim $line) | sed "s/[\"\ ]/_/g" )
    local Serial_Number=$(hdparm -I $d | grep "Serial Number" | awk -F ":" '{print $2}' | (read line; trim $line) | sed "s/[\"\ ]/_/g")
    local caption="$d: ${product}_${Model_Number}_${Serial_Number} ${s}GiB"
    cache_disk_display_caption[$1]=$caption
    echo -n $caption

}

select_target_disks(){

  # 获取所有磁盘设备列表
  local disk_devices=$(lsblk -d -o name | grep -v NAME)
  local valid=true

  # 遍历每个设备
  for device in $disk_devices; do

    #  拼接完整的设备地址
    device="/dev/$device"

    # 忽略源磁盘
    if [ "$source_disk" == "$device" ]; then
  	    log_info "忽略源磁盘: $device\n"
      	continue
    fi

    # 忽略无效设备
    fdisk -l "$device" >/dev/null 2>&1 && valid=true || valid=false
    if [ "false" == "$valid" ]; then
  	    log_info "忽略无效的设备: $device\n"
      	continue
    fi

    # 获取设备的总空间大小（以GiB为单位）
    local size_gib=$(fdisk -l "$device" | head -n1 | grep GiB | awk  '{print $3}')

    # 忽略已经被挂载的磁盘
    local mounted=$(mount | grep -m1 "$device")
    if [ "" != "$mounted" ]; then
  	  log_info "$mounted\n"
      log_info "忽略已经被挂载的磁盘: $(disk_display_caption $device $size_gib)\n"
  	  continue
    fi

    # 忽略磁盘空间小于28GiB磁盘
    if [ $( echo "$size_gib < 28" | bc) -eq 1 ]; then
  	  log_info "忽略小于28GiB的磁盘: $device\n"
  	  continue
    fi
    # 警告带有分区的设备，空白盘通常没有分区，需要交互确认
    local partitions=$(fdisk -l "$device" | grep Start)
    if [ "" != "$partitions" ]; then
  	  log_warn "磁盘已经存在分区和数据: $(disk_display_caption $device $size_gib)\n"
  	  fdisk -l "$device"
  	  log_warn "磁盘已经存在分区和数据，确定继续往 '$(disk_display_caption $device $size_gib)' 写入源镜像吗? [y/n](缺省: n):"
  	  read -n 1 selected
  	  echo
  	  if [ "y" != "$selected" ]; then
  		  log_info "忽略已经存在分区和数据的磁盘: $(disk_display_caption $device $size_gib)\n"
  		  continue
  	  fi
    fi

    selected_valid_target_disks="$device $selected_valid_target_disks"
  done

  if [ -z "$selected_valid_target_disks" ]; then
      log_error "没有找到任何有效的目标磁盘!\n"
      destruct 1
  fi

  echo
  log_warn "再确认一次，确定往这些磁盘写入源镜像吗:\n"
  for device in $selected_valid_target_disks ; do
       log_warn "$(disk_display_caption $device)\n"
  done
  log_warn "确认？[y/n](缺省: n):"
  read -n 1 selected
  echo

  echo

  if [ "n" == "$selected" ]; then
      destruct 1
  fi

}

batch_clone_to_disks(){

  for device in $selected_valid_target_disks ; do
       log_info "开始向 $(disk_display_caption $device) 写入源镜像\n"
       # -g auto 自动检测并重新安装grub
       # -e1 auto 如果NTFS开机分割表存在，自动调整文件系统的CHS值
       # -e2 用户端执行sfdisk时强迫使用EDD的硬盘CHS值（用于非grub开机管理程序）
       # -r  在目的磁盘的分区调整文件系统大小符合分区大小，不会调整磁盘分区大小
       # -j2 使用dd复制mbr与第1个分区之间的隐藏数据
       # -sfsck 跳过源文件系统的fsck检测
       # -nogui 不使用图形界面
       # -b 【危险】进入批处理模式，不会有任何交互式中断
       # -p true 结束后正常退出
       # -f /dev/source_disk_device 待复制的源磁盘设备
       # -d /dev/target_disk_device 待写入的目标磁盘设备
       ocs-onthefly -g auto -e1 auto -e2 -r -j2 -sfsck -nogui -b -p true -f "$source_disk" -d "$device"
       if [ $? -ne 0 ]; then destruct 1; fi
  done

  log_info "已经向全部选中磁盘写入源镜像\n"

}

command_exists(){
  command -v $1 >/dev/null 2>&1
}

install_qemu_nbd(){
    log_warn "检测到没有安装 qemu_nbd 工具，现在开始安装\n"
    log_warn "如果安装失败，请自行到网上搜索安装方法\n"

    if command_exists "apt-get" ; then
        apt-get update
	    apt-get install qemu-utils -y &&
        log_info "成功安装 qemu_nbd  工具\n" || destruct 1
    else
        yum install qemu-img -y &&
        log_info "成功安装 qemu_nbd  工具\n" || destruct 1
    fi
}

install_ocs(){
    log_warn "检测到没有安装 clonezilla 工具箱，现在开始安装\n"
    log_warn "如果安装失败，请自行到 https://drbl.org/installation/02-install-required-packages.php 学习安装方法\n"

    if command_exists "apt-get" ; then
        curl -sL --connect-timeout 3 http://drbl.org/GPG-KEY-DRBL | sudo apt-key add -
        echo "deb http://free.nchc.org.tw/drbl-core drbl stable" >> /etc/apt/sources.list.d/drbl.list
        apt-get update &&
        apt-get install drbl -y &&
        apt-get install clonezilla mkpxeinitrd-net partclone -y &&
        log_info "成功安装 clonezilla 工具箱\n" || destruct 1
    else
        log_error "目前不支持在 yum源 的系统一键安装 clonezilla 工具箱，请自行到 https://drbl.org/installation/02-install-required-packages.php 学习安装方法\n"
        destruct 1
    fi
}

pre_check_and_auto_fix() {
  if ! command_exists "qemu-nbd" ; then
	install_qemu_nbd
  fi

  # 加载模块
  modprobe nbd nbds_max=1
  if [ $? -ne 0 ]; then
      log_error "加载 nbd 模块失败，请手动修复!\n"
      destruct 1
  fi

  if ! command_exists "ocs-onthefly" ; then
	install_ocs
  fi
}


load_qcow2_file_to_tmp_nbd_disk() {
    qemu-nbd -d "${tmp_nbd_disk}" || echo "continue..."
    qemu-nbd -c "${tmp_nbd_disk}" $1
    if [ $? -eq 0 ]; then
        log_info "成功挂载镜像文件到 ${tmp_nbd_disk}\n"
    else
        log_error "挂载镜像文件到 ${tmp_nbd_disk} 失败!\n"
        destruct 1
    fi
}

select_source_disk() {
    local -r source_disk_or_file=$1
    if [ -z "$source_disk_or_file" ]; then
        log_error "无效的源qcow2文件或磁盘!\n"
        destruct 1
    fi

    if [[ "$source_disk_or_file" == "/dev/"* ]]; then
        source_disk=$source_disk_or_file

    elif [[ "$source_disk_or_file" == "http"*  ]]; then
        mkdir -p "/tmp/$(basename $0)/"
        local local_file="/tmp/$(basename $0)/$(basename $source_disk_or_file)"
        local local_size=$(file_size $local_file)
        local remote_size=$(url_resource_size "$source_disk_or_file")

        log_info "本地文件($(basename $source_disk_or_file))大小 $local_size 字节，远程文件大小 $remote_size 字节\n"

        if [ -z "$remote_size" ]; then
            log_error "检测新版本失败，请检查你的网络能否连接到 $source_disk_or_file\n"
            destruct 1
        fi
        if [ "$local_size" != "$remote_size" ]; then
            log_info "文件有更新，开始下载 $(basename $source_disk_or_file) 到 /tmp/$(basename $0)/\n"
            curl -kL --connect-timeout 3 $source_disk_or_file > $local_file
        fi

        load_qcow2_file_to_tmp_nbd_disk $local_file
        source_disk=$tmp_nbd_disk

    elif [ -f "$source_disk_or_file" ]; then
        load_qcow2_file_to_tmp_nbd_disk $source_disk_or_file
        source_disk=$tmp_nbd_disk
    else
        log_error "无效的源qcow2文件!\n"
        destruct 1
    fi
}



upgrade(){
    local local_size=$(file_size $0)
    local remote_size=$(url_resource_size "http://git.koal.com/gaodq/public-miscs/-/raw/main/$(basename $0)")

    log_info "本地文件($(basename $0))大小 $local_size 字节，远程文件大小 $remote_size 字节\n"

    if [ -z "$remote_size" ]; then
        log_warn "检测新版本失败，请检查你的网络能否连接到 http://git.koal.com， 现在继续克隆磁盘\n"
        return 0
    fi

    if [ "$local_size" != "$remote_size" ]; then
        log_info "检测到新版本，是否更新？[y/n](缺省: y):"
        echo
        read -n 1 selected
        if [ "$selected" == "n" ]; then
            log_info "跳过更新\n"
        else
            log_info "现在开始更新\n"
            curl -skL --connect-timeout 3 "http://git.koal.com/gaodq/public-miscs/-/raw/main/$(basename $0)" > $0 &&
            chmod +x $0 &&
            log_info "更新成功，请重新执行程序\n" || destruct 1
            destruct 0
        fi
    else
        log_info "没有新版本\n"
    fi
}

destruct() {
    # 卸载镜像
    qemu-nbd -d ${tmp_nbd_disk} || true

    if [ $1 -ne 0 ]; then
        help
    fi

    exit $1
}

# 申明 来源磁盘
source_disk=

# 申明 选定的有效目标磁盘
selected_valid_target_disks=

# 申明 临时的nbd磁盘, 用于映射qcow2文件
tmp_nbd_disk=/dev/nbd0

welcome &&

upgrade &&

pre_check_and_auto_fix &&

select_source_disk $1 &&

select_target_disks &&

time batch_clone_to_disks

destruct $?


