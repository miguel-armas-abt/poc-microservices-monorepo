#!/bin/bash

source ./../commons.sh
JENKINS_DOCKERFILE="./resources/Dockerfile"

build_jenkins_image() {
  local repository="miguelarmasabt"
  local tag_version="v2"

  command="docker build -f $JENKINS_DOCKERFILE -t $repository:$tag_version --no-cache"
  echo "$(get_timestamp) .......... $command" >> "./../../$LOG_FILE"
  eval "$command"
}



docker-compose -f docker-compose-cicd.yml up -d