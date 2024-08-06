#!/bin/bash

# 避免中文字符对字符切割功能的干扰
export LC_ALL=C

record_log() {
    msg=$1
    echo "$msg"
    logger "$msg"
}

grow_root_part() {
    record_log "INFO: Will exec GrowRootPart.sh"
    
    record_log "INFO: Before:"
    df -h /

    part_name=$(df -T / | awk -F " " 'NR==2 {print $1}')
    record_log "INFO: part_name=$part_name"

    fs_type=$(df -T / | awk -F " " 'NR==2 {print $2}')
    record_log "INFO: fs_type=$fs_type"
    if [ "$fs_type" != "ext4" ] && [ "$fs_type" != "xfs" ]; then     
        record_log "Error: only support ext4 and xfs, but fs_type=$fs_type "
        return 1
    fi
    
    # 如果是lvm, 则重新查找磁盘分区
    part_is_lv=$((echo $part_name | grep "/dev/mapper" >/dev/null) && echo "true" || echo "false" )
    if [ "$part_is_lv" == "true" ]; then
        record_log "INFO: $part_name is lv"

        lv_part_name=$part_name
        record_log "INFO: lv_part_name=$lv_part_name"
        
        lv_name=$(echo $lv_part_name | sed  -E "s/\/dev\/(mapper\/)+//")
        part_canonical_name=$(lsblk  | grep $lv_name -B 1 -m 1 | head -n 1 | awk '{print $1}' | sed -E "s/([^a-z0-9]*)([a-z0-9]+)/\2/")
        
        part_name="/dev/$part_canonical_name"
        record_log "INFO: part_name=$part_name"
    fi
    
    # 超过9可能找的就不准确了
    disk_name=$(echo "$part_name" | sed 's/.$//')
    record_log "INFO: disk_name=$disk_name"
    part_number=${part_name:0-1}
    record_log "INFO: part_number=$part_number"
    
    disk_part_table_is_gpt=$((parted -s $disk_name print 2>/dev/null | grep "Partition Table" | grep -i gpt > /dev/null) && echo "true" || "false")
    
    # 更新gpt分区表
    echo -e "print free\nFix\nquit\n" | parted $disk_name ---pretend-input-tty

    # 扩容分区
    if [ "$part_is_lv" == "true" ]; then
        (echo -e "resizepart\n$part_number\n100%\nquit\n"| parted $disk_name ---pretend-input-tty) && result=success || result=fail
        if [ "$result" == "fail" ]; then
            record_log "Error: resizepart  fail"
            return 1
        fi
        record_log "INFO: extend lv"
        pvresize $part_name
        lvextend $lv_part_name -l +100%FREE
    else
        (echo -e "resizepart\n$part_number\nYes\n100%\nquit\n"| parted $disk_name ---pretend-input-tty) && result=success || result=fail
        if [ "$result" == "fail" ]; then
            record_log "Error: resizepart  fail"
            return 1
        fi
    fi

    # 同步文件系统
    if [ "$part_is_lv" == "true" ]; then
        part_name=$lv_part_name
        record_log "INFO: part_name=$part_name"
    fi
    if [ "$fs_type" == "ext4" ]; then     
        resize2fs "$part_name" || true
        record_log "INFO: exec resize2fs success"
    elif [ "$fs_type" == "xfs" ]; then
        xfs_growfs "$part_name" && result=success || result=fail
        if [ "$result" == "fail" ]; then
            #兼容不同版本的xfs_growfs命令
            xfs_growfs / >/dev/null 2>&1 || true
        fi
        record_log "INFO: exec xfs_growfs success"
    else
        record_log "Error: only support ext4 and xfs, but fs_type=$fs_type "
        return 1
    fi

    record_log "INFO: After:"
    df -h /
}

grow_root_part
