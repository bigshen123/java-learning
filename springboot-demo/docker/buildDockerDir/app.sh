#!/bin/bash

if [ ! -f /conf/application.yml ];then
        cp /application.yml /conf/application.yml
fi

java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:1235 -jar /svs-demo-1.0.0.jar --spring.config.location=/conf/application.yml