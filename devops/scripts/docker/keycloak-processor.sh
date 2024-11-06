#!/bin/bash

source ./../commons.sh

DOCKER_COMPOSE_FILE="./../../docker-compose.yml"

process_operation() {
  command="docker-compose -f $DOCKER_COMPOSE_FILE up -d --force-recreate keycloak-server"
  echo "$(get_timestamp) .......... $command" >> "./../../$LOG_FILE"
  eval "$command"
}

process_operation