#!/bin/bash

source ./../commons.sh
BACKEND_PATH="./../../../application/backend"

search_dockerfile() {
  local component_path=$1

  dockerfile=$(find "$component_path" -maxdepth 1 -type f -iname 'Dockerfile*' | head -n 1)

  if [[ -z "$dockerfile" ]]; then
    echo "$(get_timestamp) Dockerfile not found in $component_path" >> "./../../$LOG_FILE"
    return 1
  fi
  echo "$dockerfile"
}

process_record() {
  local component_name=$1
  local component_type=$2

  if [[ $component_type == "business" ]] || [[ $component_type == "platform" ]] ; then

    component_path="$BACKEND_PATH/$component_type/$component_name"

    dockerfile=$(search_dockerfile "$component_path") || return 1

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