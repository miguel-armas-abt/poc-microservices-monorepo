#!/bin/bash

source ./../commons.sh

DOCKER_COMPOSE_FILE="./../../docker-compose.yml";

validate_operation() {
  local operation=$1

  local valid_operations=("up" "stop" "delete")

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

    if [[ $operation == "up" ]]; then
      #-d: in background
      command="docker-compose -f $DOCKER_COMPOSE_FILE up -d"
    fi

    if [[ $operation == "stop" ]]; then
      command="docker-compose -f $DOCKER_COMPOSE_FILE stop"
    fi

    if [[ $operation == "delete" ]]; then
      #-v: delete volumes
      command="docker-compose -f $DOCKER_COMPOSE_FILE down -v"
    fi

    echo "$(get_timestamp) .......... $command" >> "./../../$LOG_FILE"
    eval "$command"
}

operation=$1
process_operation "$operation"