#!/bin/bash

set -e

if [ $# -le 0 ]; then
    echo "Error: Missing patch pack file or download url address"
    exit 1
fi

echo -e "\n\nWARN: Patching will stop core serices, are you continue?\n"
timer_ttl=15
while [ $timer_ttl -gt 0 ]; do
    echo -ne "\rPress any key to continue or Ctrl+C to exit in ${timer_ttl} seconds: "
    read -rs -n1 -t 1 && rc=$? || rc=$?
    if [ $rc -eq 0 ]; then
        break
    fi
    ((timer_ttl--))
done

echo "INFO: Begin Patching"

echo "INFO: Patching $1"

PACK_FILE="$1"
if [[ "$1" =~ "/" ]]; then
    PACK_FILE_NAME="${1##*/}"
else
    PACK_FILE_NAME="$1"
fi

rm -rf /tmp/migration
mkdir -p /tmp/migration/upgrade/
cp -af $PACK_FILE /tmp/migration/upgrade/
version=$(cat /var/cfg/product.version | grep upgradeVersion | awk -F '\"' '{print $2}')
mkdir -p /var/log/nsag/upgrade
/usr/local/bin/ConfigUpgrade.sh /tmp/migration/upgrade/$PACK_FILE_NAME K0a1UpgradeV7 backend 2>&1 | tee /var/log/nsag/upgrade/upgrade-from-${version}.log
if [ $? -ne 0 ]; then
    echo "Error: Failed. Exited"
    rm -rf /tmp/migration
    exit 1
fi

rm -rf /tmp/migration

echo "INFO: End Patching"
