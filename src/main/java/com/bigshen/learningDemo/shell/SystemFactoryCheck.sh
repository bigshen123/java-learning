#!/bin/sh

# 获取硬件型号
get_hardware_model() {
  dmidecode | grep -i prod | uniq | head -n 1 | awk -F ':' '{print $NF}' | sed 's/^\s//'
}

# 获取硬件序列号
get_serial_number() {
  dmidecode -t system | grep 'Serial Number' | awk '{print $3}'
}

# 获取CPU信息
get_cpu_info() {
  local cpu_model_name
  local cpu_frequency
  local model_name
  local model_number
  local model_cores
  local cpu_counts

  cpu_model_name=$(cat /proc/cpuinfo | grep "model name" | uniq | awk -F ':' '{print $NF}' | sed 's/^\s//')
  cpu_frequency=$(dmidecode -s processor-frequency | awk '{printf "@ %.2f GHz\n", $1/1000}' | head -n 1)
  model_name="$cpu_model_name $cpu_frequency"
  model_number=$(cat /proc/cpuinfo | grep 'model' | grep -v 'model name' | uniq | awk -F ':' '{print $NF}' | sed 's/^\s//')
  model_cores=$(cat /proc/cpuinfo | grep 'cpu cores' | uniq | awk -F ':' '{print $NF}' | sed 's/^\s//')
  cpu_counts=$(cat /proc/cpuinfo | grep 'model name' | wc -l)

  echo "{\"modelName\":\"$model_name\",\"modelCount\":\"$model_number\",\"coreCount\":\"$model_cores\",\"cpuCount\":\"$cpu_counts\"}"
}

# 获取内存信息
get_memory_info() {
  local memory_info
  local memory_total
  local memory_used
  local memory_unused
  local manufacturer="N/A"
  local type="N/A"
  local speed="N/A"

  memory_info=$(free -m | grep "Mem")
  memory_total=$(echo "$memory_info" | awk '{printf "%.2f", $2/1024}')
  memory_used=$(echo "$memory_info" | awk '{printf "%.2f", ($2 - $4)/1024}')
  memory_unused=$(echo "$memory_info" | awk '{printf "%.2f", $4/1024}')

  MemoryDevicesCount=$(dmidecode -t memory | grep Handle|grep -v 'Array Handle'|grep -v 'Error Information Handle'|wc -l)
  MemoryDevices=$(dmidecode -t memory | grep Handle|grep -v 'Array Handle'|grep -v 'Error Information Handle'|sed 's/$/@@/g'|xargs)
  local i=1
  while [ $i -le $MemoryDevicesCount ]; do
    MemoryDevice=$(echo $MemoryDevices|awk -F '@@ ' -v n="$i" '{print $n}')
    MemoryDeviceInfo=$(dmidecode -t memory | grep -A 22 "$MemoryDevice")
    if echo "$MemoryDeviceInfo" | grep 'Physical Memory Array' >/dev/null; then
      i=$((i+1))
      continue
    fi
    if echo "$MemoryDeviceInfo" | grep 'Size: No Module Installed' >/dev/null; then
      i=$((i+1));
      continue
    fi
    if echo "$MemoryDeviceInfo" | grep 'Installed Size: Not Installed' >/dev/null; then
      i=$((i+1));
      continue
    fi
    Manufacturer=$(echo "$MemoryDeviceInfo" | grep 'Manufacturer:' | awk -F ':' '{print $NF}' | sed 's/^\s*//')
    Size=$(echo "$MemoryDeviceInfo" | grep 'Size:' | awk -F ':' '{print $NF}' | sed 's/^\s*//')
    Type=$(echo "$MemoryDeviceInfo" | grep 'Type:' | awk -F ':' '{print $NF}' | sed 's/^\s*//')
    Speed=$(echo "$MemoryDeviceInfo" | grep 'Speed:' | awk -F ':' '{print $NF}' | head -n 1 | sed 's/^\s*//')

    # 如果获取到完整信息，则退出循环
    if [ -n "$Manufacturer" ] && [ -n "$Type" ] && [ -n "$Speed" ]; then
      break
    fi

    i=$((i+1))
  done

  echo "{\"manufacturer\":\"$Manufacturer\",\"type\":\"$Type\",\"speed\":\"$Speed\",\"memoryTotal\":\"$memory_total GB\",\"memoryUsed\":\"$memory_used GB\",\"memoryUnused\":\"$memory_unused GB\"}"
}

# 获取磁盘信息
get_disk_info() {
  local partition_info
  local partitions

  partitions=$(df -H | grep '^/dev/' | awk '{print $1}')

  for partition in $partitions; do
    local total used unused
    total=$(df -H | grep "$partition" | awk '{print $2}')
    used=$(df -H | grep "$partition" | awk '{print $3}')
    unused=$(df -H | grep "$partition" | awk '{print $4}')
    partition_info="$partition_info {\"diskPartition\":\"$partition\",\"diskTotal\":\"$total\",\"diskUsed\":\"$used\",\"diskUnused\":\"$unused\"},"
  done

  echo "[${partition_info%,}]"
}

# 获取操作系统和内核信息
get_os_kernel_info() {
  source /etc/os-release
  KERNEL_VERSION=$(uname -r)

  echo "{\"prettyName\":\"$PRETTY_NAME\",\"name\":\"$NAME\",\"versionId\":\"$VERSION_ID\",\"version\":\"$VERSION\",\"id\":\"$ID\",\"homeUrl\":\"$HOME_URL\",\"bugReportUrl\":\"$BUG_REPORT_URL\",\"versionCodename\":\"$VERSION_CODENAME\",\"platformId\":\"$PLATFORM_ID\",\"kernelVersion\":\"$KERNEL_VERSION\"}"
}

# 获取硬件信息并生成JSON
generate_json() {
  local product_name
  local serial_number
  local cpu_info
  local memory_info
  local disk_info
  local os_kernel_info

  product_name=$(get_hardware_model)
  serial_number=$(get_serial_number)
  cpu_info=$(get_cpu_info)
  memory_info=$(get_memory_info)
  disk_info=$(get_disk_info)
  os_kernel_info=$(get_os_kernel_info)

  echo "{ \"productName\" : \"$product_name\", \"systemSerialNumber\" : \"$serial_number\", \"cpuInfo\" : $cpu_info, \"memoryInfo\" : $memory_info, \"diskInfo\" : $disk_info, \"osKernelInfo\" : $os_kernel_info }"
}

generate_json
