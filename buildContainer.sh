#! /bin/sh 

while getopts :cf OPTION
do
    case $OPTION in
	c)
	    skipBuild=true
	    ;;
	f)
	    forceBuild=true
	    ;;
	*)
	    echo "unknown option $OPTARG" 1>&2
	    exit 1
    esac
done

artifactName=$(getPomAttribute.sh artifactId | tr '[[:upper:]]' '[[:lower:]]')
imageName=drsaaron/$artifactName
containerName=$artifactName

# get the pom version number for tagging
pomVersion=$(getPomAttribute.sh version)
imageVersion=${pomVersion%-RELEASE}
echo "building container version $imageVersion"

# get the port the production app listens on
port=$(grep -E '^ *server.port' src/main/resources/prod.properties | awk -F= '{ print $2 }' | sed 's/ *$//')

# if there's a management port, grab it too
managementPort=$(grep -E '^ *management.server.port' src/main/resources/prod.properties | awk -F= '{ print $2 }' | sed 's/ *$//')

# remove the current version
docker stop $containerName
docker rm $containerName

# build the image
[ "$forceBuild" = "true" ] && extraArgs="-f"
[ "${skipBuild:-false}" = "true" ] || buildDocker.sh -v $imageVersion -n $imageName $extraArgs
status=$?
if [ "$status" != 0 ]
then
    echo "build failed" 1>&2
    exit 1
fi

# handle user
case $(uname -s) in
    Linux)
	userFlag="$(id -u):$(id -g)"
	;;
    *)
	userFlag=""
esac

# start 'er up
portConnector="-p $port:$port"
[ -n "$managementPort" ] && portConnector="$portConnector -p $managementPort:$managementPort"
docker run -d --name $containerName -v ~/blazar-archive/repository:/archive-data $portConnector $userFlag $imageName:$imageVersion
