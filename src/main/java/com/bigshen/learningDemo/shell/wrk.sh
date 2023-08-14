#!/bin/bash

ulimit -n 819200
ulimit -i 819200

sysctl -w net.core.somaxconn=65535
sysctl -w net.ipv4.ip_local_port_range="1025    65535"
sysctl -w net.ipv4.tcp_timestamps=1
sysctl -w net.ipv4.tcp_tw_reuse=1
sysctl -w net.ipv4.tcp_tw_recycle=1
sysctl -w net.ipv4.tcp_fin_timeout=1
sysctl -w net.netfilter.nf_conntrack_max=2097152
sysctl -w fs.file-max=1622794

while true; do
    sudo docker run -it --rm --privileged --net=host -v /usr/local/ssl:/usr/local/ssl git.koal.com:4567/gw-cloud/docker/wrk:amd64 wrk -t1 -c2 -d300 --clientcert /usr/local/ssl/lzyrsa.pem --clientkey /usr/local/ssl/lzyrsa.key --protocol tls1.2 --ciphers ECDHE-RSA-AES256-SHA384 --latency https://10.0.100.201:12345 &
    ./wrk -t20 -c200 -d300 --timeout 300s --clientcert client.cer --clientkey client.key --protocol tls1.2 --ciphers ECDHE-RSA-AES256-SHA384 -D -k -r --latency https://10.0.243.79:12345/1b.html
    sleep 0.1
done
