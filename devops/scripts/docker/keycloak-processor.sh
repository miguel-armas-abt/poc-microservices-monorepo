#!/bin/bash

source ./../commons.sh

validate_operation() {
  local operation=$1

  local valid_operations=("up")

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

  command="docker-compose -f ./../../docker-compose.yml up -d --force-recreate keycloak-server"
  echo "$(get_timestamp) .......... $command" >> "./../../$LOG_FILE"
  eval "$command"
}

operation=$1
process_operation "$operation"