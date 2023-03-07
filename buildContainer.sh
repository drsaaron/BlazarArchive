#! /bin/sh

imageName=drsaaron/blazararchive
imageVersion=0.2
containerName=blazararchive

docker stop $containerName
docker rm $containerName

docker run -d --name $containerName -v ~/blazar-archive/repository:/archive-data  -p 8081:8081 $imageName:$imageVersion
