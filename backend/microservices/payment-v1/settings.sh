#!/bin/bash
set -e

get_compile_command() {
  command="mvn clean install -Dmaven.home=\"$MAVEN_HOME\" -Dmaven.repo.local=\"$MAVEN_REPOSITORY\""
  echo "$command"
}

get_docker_build_image_command() {
  values_file="./values.yaml"
  repository=$(yq '.image.repository' "$values_file")
  tag_version=$(yq '.image.tag' "$values_file")

  echo "docker build -t $repository:$tag_version -f ./Dockerfile ."
}

case "$1" in
  compile_command) get_compile_command ;;
  docker_build_image_command) get_docker_build_image_command   ;;
  *) echo -e "Invalid option" >&2
esac