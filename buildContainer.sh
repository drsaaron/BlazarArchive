#! /bin/sh

imageName=drsaaron/$(getPomAttribute.sh artifactId | tr '[[:upper:]]' '[[:lower:]]')
imageVersion=$(getPomAttribute.sh version | sed 's/-RELEASE//')
containerName=blazararchive

docker stop $containerName
docker rm $containerName

docker run -d --name $containerName -v ~/blazar-archive/repository:/archive-data  -p 8081:8081 $imageName:$imageVersion
