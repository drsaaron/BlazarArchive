#! /bin/sh

version=$(getPomAttribute.sh version | sed 's/-RELEASE//')
image=drsaaron/$(artifactName.sh)

ssh scott@192.168.1.2 "docker save $image:$version | gzip" | gunzip | docker load
