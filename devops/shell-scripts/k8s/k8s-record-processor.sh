#!/bin/bash

source ./../commons.sh
BACKEND_PATH="./../../../application/backend"

validate_operation() {
  local operation=$1

  local valid_operations=("port-forward")

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

  values_file="$BACKEND_PATH/$component_type/$component_name/values.yaml"

  container_port=$(yq '.ports.containerPort' "$values_file")
  host_port=$(yq '.docker.hostPort' "$values_file")
  namespace=$(awk '/namespace:/ {print $2}' "$values_file" | sed 's/"//g')

  command=""

  if [[ $operation == "port-forward" ]]; then
    command="kubectl port-forward svc/$component_name $host_port:$container_port -n $namespace"
    start bash -c "echo -ne \"\\033]0;$component_name\\007\";$command" #set title in the emergent windows
  fi

  echo "$(get_timestamp) .......... $command" >> "./../../$LOG_FILE"
}

operation=$1
component_name=$2
component_type=$3
process_record "$operation" "$component_name" "$component_type"