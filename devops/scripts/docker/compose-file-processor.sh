#!/bin/bash

source ./../commons.sh

DOCKER_COMPOSE_FILE="./../../docker-compose.yml";

validate_operation() {
  local operation=$1

  local valid_operations=("template" "up" "list" "recreate" "stop" "delete")

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

    if [[ $operation == "template" ]]; then
      command="./compose-file-generator.sh"
    fi

    if [[ $operation == "up" ]]; then
      #-d: detached mode
      command="docker-compose -f $DOCKER_COMPOSE_FILE up -d"
    fi

    if [[ $operation == "list" ]]; then
      command="docker-compose -f $DOCKER_COMPOSE_FILE ps --format \"table {{.ID}}\t{{.Names}}\t{{.Status}}\""
    fi

    if [[ $operation == "recreate" ]]; then
      #--force-recreate: recreate containers
      command="docker-compose -f $DOCKER_COMPOSE_FILE up -d --force-recreate"
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