#!/bin/bash
# compile
#mvn clean package -Dmaven.test.skip=true
# start server
ssh -t root@1.15.119.160 "cd /home/zhzx-server;sh kill-jar.sh;scp ./target/zhzx-server-0.0.1.jar .;sh start-jar.sh"