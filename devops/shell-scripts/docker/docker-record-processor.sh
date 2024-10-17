#!/bin/bash

source ./../commons.sh
BACKEND_PATH="./../../../application/backend"

process_record() {
  local operation=$1
  local component_name=$2
  local component_type=$3

  #ignore docker build
  if [[ $component_type == "business" ]] || [[ $component_type == "platform" ]] ; then

    component_path="$BACKEND_PATH/$component_type/$component_name"
    dockerfile="$component_path/Dockerfile"

    values_file="$component_path/values.yaml"
    repository=$(awk '/repository:/ {print $2}' "$values_file" | sed 's/"//g')
    tag_version=$(awk '/tag:/ {print $2}' "$values_file" | sed 's/"//g')

    command=""

    if [[ $operation == "build" ]]; then
      command="docker build -f $dockerfile -t $repository:$tag_version $component_path"
    fi

    echo "$(get_timestamp) .......... $command" >> "./../../$LOG_FILE"
    eval "$command"
  fi
}

operation=$1
component_name=$2
component_type=$3
process_record "$operation" "$component_name" "$component_type"