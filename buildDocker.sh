#! /bin/sh

imageName=drsaaron/$(getPomAttribute.sh artifactId | tr '[[:upper:]]' '[[:lower:]]')
version=$(getPomAttribute.sh version | sed 's/-RELEASE//')

while getopts :fv:n: OPTION
do
    case $OPTION in
	v)
	    version=$OPTARG
	    ;;
	n)
	    imageName=$OPTARG
	    ;;
	f)
	    force=TRUE
	    ;;
	*)
	    echo "usage: $0 -v <version> [-n <imageName>]" 1>&2
	    exit 1
    esac
done

if [ "$version" = "" ]
then
    echo "missing version" 1>&2
    exit 1
fi

# sanity check that the image doesn't already exist
if docker images $imageName | grep -Fq $version
then
    echo "image $imageName:$version already exists.  Run again with -f as argument to overwrite."

    if [ "$force" = "TRUE" ]
    then
	echo "forcing an overwrite"
    else
	exit 0
    fi
fi

# now build the container
docker build --network host -t $imageName .

# tag
docker tag $imageName $imageName:$version
