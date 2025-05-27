#!/bin/bash
set -e

source ./../commons.sh
source ./../variables.env

DOCKER_COMPOSE_FILE="./docker-compose.yml";

validate_operation() {
  local operation=$1

  local valid_operations=("up-compose" "stop-compose" "delete-compose" "recreate-container")

  for valid_operation in "${valid_operations[@]}"; do
    if [[ "$operation" == "$valid_operation" ]]; then
      return 0
    fi
  done

  echo "operation must be match: ${valid_operations[*]}"
  return 1
}

process_operation() {
    local operation=$1
    local container_name=$2
    validate_operation "$operation"

    command=""

    if [[ $operation == "up-compose" ]]; then
      #-d: in background
      command="docker-compose -f $DOCKER_COMPOSE_FILE up -d"
      print_log_and_eval "$command"
    fi

    if [[ $operation == "stop-compose" ]]; then
      command="docker-compose -f $DOCKER_COMPOSE_FILE stop"
      print_log_and_eval "$command"
    fi

    if [[ $operation == "delete-compose" ]]; then
      #-v: delete volumes
      command="docker-compose -f $DOCKER_COMPOSE_FILE down -v"
      print_log_and_eval "$command"
    fi

    if [[ $operation == "recreate-container" ]]; then
      command="docker-compose -f $DOCKER_COMPOSE_FILE up -d --force-recreate $container_name"
      print_log_and_eval "$command"
    fi
}

operation=$1
container_name=$2
process_operation "$operation" "$container_name"