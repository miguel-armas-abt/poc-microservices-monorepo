#!/bin/bash

source ./../commons.sh
DOCKERFILE="./resources/Dockerfile"
DOCKER_COMPOSE_FILE="./resources/docker-compose.yml"

wait_for_container() {
  local container_name=$1
  local max_retries=60
  local retries=0

  until [ "$(docker inspect -f '{{.State.Health.Status}}' "$container_name" 2>/dev/null)" == "healthy" ]; do
      if [ $retries -ge $max_retries ]; then
        echo -e "${RED}timeout${NC}"
        return 1
      fi

      arrow_loader "Waiting for container"

      retries=$((retries + 1))
      sleep 0.5
  done

  return 0
}

validate_operation() {
  local operation=$1

  local valid_operations=("build" "up" "stop" "delete" "wait")

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

    local repository="miguelarmasabt"
    local image="poc-jenkins"
    local tag="v1.0.1"

    validate_operation "$operation"

    connect_minikube="docker network connect minikube $image"
    disconnect_minikube="docker network disconnect minikube $image"

    command="clear"

    if [[ $operation == "build" ]]; then
      command="docker build -f $DOCKERFILE -t $repository/$image:$tag --no-cache ."
    fi

    if [[ $operation == "up" ]]; then
      #-d: in background
      command="docker-compose -f $DOCKER_COMPOSE_FILE up -d"
      eval "$connect_minikube"
    fi

    if [[ $operation == "stop" ]]; then
      eval "$disconnect_minikube"
      command="docker-compose -f $DOCKER_COMPOSE_FILE stop"
    fi

    if [[ $operation == "delete" ]]; then
      eval "$disconnect_minikube"
      command="docker-compose -f $DOCKER_COMPOSE_FILE down -v"
    fi

    if [[ $operation == "wait" ]]; then
      container_name="$repository/$image:$tag"
      wait_for_container "$container_name"
    fi

    echo "$(get_timestamp) .......... $command" >> "./../../$LOG_FILE"
    eval "$command"
}

operation=$1
process_operation "$operation"