#!/usr/bin/env bash
docker rm -f $(docker ps -a -q)
docker rmi -f cache
docker rmi -f location
docker rmi -f eureka
docker rmi -f weather
docker rmi -f web
docker rmi -f zuul
docker rmi $(sudo docker images | grep "^<none>" | awk "{print $3}")
##sudo docker rmi $(sudo docker images | grep "axele" | awk "{print $3}")

docker images
