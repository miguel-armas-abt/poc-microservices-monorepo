#!/bin/bash

source ./../commons.sh

validate_operation() {
  local operation=$1

  local valid_operations=("build-in-minikube")

  for valid_operation in "${valid_operations[@]}"; do
    if [[ "$operation" == "$valid_operation" ]]; then
      return 0
    fi
  done

  echo "El valor de la operación debe coincidir con: ${valid_operations[*]}"
  return 1
}

process_operation() {
      local operation=$1

      validate_operation "$operation"

      command=""

      if [[ $operation == "build-in-minikube" ]]; then
        eval $(minikube docker-env --shell bash)
        cd ./../docker/
        command="./docker-csv-processor.sh build"
      fi

      echo "$(get_timestamp) .......... $command" >> "./../../$LOG_FILE"
      eval "$command"
}

operation=$1
process_operation "$operation"