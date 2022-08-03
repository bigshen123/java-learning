#!/bin/bash
unzip -o gw-cloud-rms-7.0.*-SNAPSHOT-*.jar
docker exec -it rms rm -rf /gw-cloud-rms/lib/BOOT-INF
docker exec -it rms rm -rf /gw-cloud-rms/lib/META-INF
docker exec -it rms rm -rf /gw-cloud-rms/lib/org
docker cp BOOT-INF rms:/gw-cloud-rms/lib/
docker cp META-INF rms:/gw-cloud-rms/lib/
docker cp org rms:/gw-cloud-rms/lib/
docker restart rms
tail -f /var/log/nsag/RMS.log
