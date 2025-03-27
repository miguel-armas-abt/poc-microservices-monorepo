#!/bin/bash

source ./../commons.sh
BACKEND_PATH="./../../../application/backend"

process_record() {
  local component_name=$1
  local component_type=$2

  if [[ $component_type == "business" ]] || [[ $component_type == "platform" ]] ; then

    component_path="$BACKEND_PATH/$component_type/$component_name"
    dockerfile="$component_path/Dockerfile"

    values_file="$component_path/values.yaml"

    repository=$(yq '.image.repository' "$values_file")
    tag_version=$(yq '.image.tag' "$values_file")

    command="docker build -f $dockerfile -t $repository:$tag_version $component_path"

    echo "$(get_timestamp) .......... $command" >> "./../../$LOG_FILE"
    eval "$command"
  fi
}

component_name=$1
component_type=$2
process_record "$component_name" "$component_type"