#!/bin/bash
#v.1.0

tags=( $@ )
len=${#tags[@]}

printHelp() {
  echo
  echo "HELP for 'create-docker-image.sh'"
  echo
  echo "Usage: ./create-docker-image.sh [<tag>] [,<tag>]"
  echo
  echo "Create a docker image"
  echo
  echo "Script takes zero, one, or more tags."
  echo "Script creates the docker image using the name found in DockerImageName and steps found in Dockerfile."
  echo "If one or more tags are provided, the script then tags the docker image using those tags."
  echo "If no tags are provided, 'latest' is assumed to be the single tag to be used."
  echo
  echo "Note: DockerImageName file is required to be in the same directory and contains the name of the docker image."
  echo
}

createDockerIgnoreFile() {
  echo ".git"                       > .dockerignore
  echo "bin"                       >> .dockerignore
  echo "src/main"                  >> .dockerignore
  echo "src/test"                  >> .dockerignore
  echo "target/scala-*/classes"    >> .dockerignore
  echo "target/streams"            >> .dockerignore
}

processOtherTags() {
  for otherTag in "$@" ; do
    echo "Tagging image $image:$firstTag with $otherTag..."
    docker tag $image:$firstTag $image:$otherTag
  done
}

if [ "$1" == "-?" ] || [ "$1" == "-h" ] ||  [ "$1" == "--h" ] || [ "$1" == "help" ] || [ "$1" == "-help" ] || [ "$1" == "--help" ]
then
  printHelp
  exit 0
fi

#get the image name
image=$(<DockerImageName)
if [ -z "$image" ]
then
  echo
  echo "Cannot find an image name in the required file 'DockerImageName'."
  printHelp
  exit 1
fi

if [ $len == 0 ]
then
  echo
  echo "No tags provided, assuming at single tag of 'latest'."
  firstTag=latest
else
  firstTag=${tags[0]}
fi

createDockerIgnoreFile

echo "Creating image $image:$firstTag..."
docker build -f Dockerfile -t $image:$firstTag .

if [ $len -gt 1 ]
then
  otherTags=("${tags[@]:1:$len}")
  processOtherTags "${otherTags[@]}"
fi

docker images $image