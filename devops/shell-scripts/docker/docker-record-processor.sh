#!/bin/bash

source ./../commons.sh
BACKEND_PATH="./../../../application/backend"

validate_operation() {
  local operation=$1

  local valid_operations=("build")

  for valid_operation in "${valid_operations[@]}"; do
    if [[ "$operation" == "$valid_operation" ]]; then
      return 0
    fi
  done

  echo "El valor de la operación debe coincidir con: ${valid_operations[*]}"
  return 1
}

process_record() {
  local operation=$1
  local component_name=$2
  local component_type=$3

  validate_operation "$operation"

  #ignore docker build
  if [[ $component_type == "business" ]] || [[ $component_type == "platform" ]] ; then

    component_path="$BACKEND_PATH/$component_type/$component_name"
    dockerfile="$component_path/Dockerfile"

    values_file="$component_path/values.yaml"

    repository=$(yq '.image.repository' "$values_file")
    tag_version=$(yq '.image.tag' "$values_file")

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