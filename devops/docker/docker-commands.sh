#!/bin/bash
set -e

source ./../commons.sh
source ./../variables.env

DOCKER_COMPOSE_FILE="./docker-compose.yml";

wait_for_container() {
  local container_name=$1
  local max_retries=$CONTAINER_WAIT_RETRIES
  local retries=0

  until [[ "$(is_container_running "$container_name")" == "true" ]]; do
    if [ $retries -ge "$max_retries" ]; then
      printf "\n"
      echo -e "${RED}timeout waiting for $container_name${NC}"
      return 1
    fi

    arrow_loader "waiting for container $container_name"
    retries=$((retries + 1))
    sleep 0.5
  done

  sleep "$CONTAINER_STARTUP_DELAY"
  printf "\n"
  echo -e "${GREEN}$container_name is running${NC}"
  return 0
}

validate_operation() {
  local operation=$1

  local valid_operations=("up-compose" "stop-compose" "delete-compose" "recreate-container" "wait-container")

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

    if [[ $operation == "wait-container" ]]; then
      wait_for_container "$container_name"
    fi
}

operation=$1
container_name=$2
process_operation "$operation" "$container_name"