#! /bin/sh

die() {
    echo $1 1>&2
    exit 1
}

while getopts :d:e: OPTION
do
    case $OPTION in
	d)
	    appDirectory=$OPTARG
	    [ -d "$appDirectory" ] || die "work directory $appDirectory not found"
	    ;;
	e)
	    ENVIRONMENT=$OPTARG
	    ;;
	*)
	    die "unknown option $OPTARG"
    esac
done

[ -z "$appDirectory" ] && appDirectory=$(pwd)

pomFile=$appDirectory/pom.xml
[ -f "$pomFile" ] || die "$appDirectory doesn't seem to be an application project: no pom file"

artifactId=$(getPomAttribute.sh -p $pomFile artifactId)
version=$(getPomAttribute.sh -p $pomFile version)

java -jar $appDirectory/target/$artifactId-$version.jar --spring.config.name=application,${ENVIRONMENT:-test}



