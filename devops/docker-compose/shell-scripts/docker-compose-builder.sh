#!/bin/bash

source ./../../common-script.sh
DOCKER_LOG_FILE=./../../$DOCKER_LOG_FILE

ENVIRONMENT_VARIABLES_PATH=./../../environment
PARAMETERS_CSV=./../../docker/containers-to-run.csv
DOCKER_COMPOSE_TEMPLATE=./templates/docker-compose.template.yml
SERVICE_TEMPLATE=./templates/service.template.yml

build_dependencies() {
  local dependencies=$1

  formatted_dependencies=""
  if [ "$dependencies" != "null" ]; then
    # divide string by ';'
    IFS=';' read -r -a parts <<< "$dependencies"
    first=true
    for part in "${parts[@]}"; do
      if $first; then
        formatted_dependencies+="     - $part\n"
        first=false
      else
        formatted_dependencies+="      - $part\n"
      fi
    done
    formatted_dependencies="depends_on:\n $formatted_dependencies"
  fi

  formatted_dependencies=$(echo -e "$formatted_dependencies" | sed '/^$/d') #remove extra empty line
  echo "$formatted_dependencies"
}

build_variables() {
  local app_name=$1

  env_file="$ENVIRONMENT_VARIABLES_PATH/$app_name/$app_name.env"
  formatted_variables=$(grep -v '^#' "$env_file" | sed 's/^/      - /' | sed 's/=/=/' | tr '\n' '\n')
  echo "$formatted_variables"
}

build_volumes() {
  local volumes=$1

  formatted_volumes=""
  if [ "$volumes" != "null" ]; then
    # divide string by ';'
    IFS=';' read -r -a parts <<< "$volumes"
    first=true
    for part in "${parts[@]}"; do
      if $first; then
        formatted_volumes+="     - $part\n"
        first=false
      else
        formatted_volumes+="      - $part\n"
      fi
    done
    formatted_volumes="volumes:\n $formatted_volumes"
  fi
  formatted_volumes=$(echo -e "$formatted_volumes" | sed '/^$/d') #remove extra empty line
  echo "$formatted_volumes"
}

process_csv_record() {
  local formatted_services_accumulator=$1

  local app_name=$2
  local docker_image=$3
  local dependencies=$4
  local host_port=$5
  local container_port=$6
  local volumes=$7

  formatted_service=$(<"$SERVICE_TEMPLATE")
  formatted_service="${formatted_service//@app_name/$app_name}"
  formatted_service="${formatted_service//@docker_image/$docker_image}"
  formatted_service="${formatted_service//@host_port/$host_port}"
  formatted_service="${formatted_service//@container_port/$container_port}"

  dependencies=$(build_dependencies "$dependencies")
  formatted_service="${formatted_service//@dependencies/$dependencies}"

  variables=$(build_variables "$app_name")
  formatted_service="${formatted_service//@variables/"environment: \n$variables"}"

  volumes=$(build_volumes "$volumes")
  formatted_service="${formatted_service//@volumes/$volumes}"

  echo "$(get_timestamp) .......... $app_name .......... Image: $docker_image" >> "$DOCKER_LOG_FILE"
  echo "$(get_timestamp) .......... $app_name .......... Host port: $host_port" >> "$DOCKER_LOG_FILE"
  echo "$(get_timestamp) .......... $app_name .......... Container port: $container_port" >> "$DOCKER_LOG_FILE"
  echo "$(get_timestamp) .......... $app_name .......... Dependencies: $dependencies" >> "$DOCKER_LOG_FILE"
  echo "$(get_timestamp) .......... $app_name .......... Volumes: $volumes" >> "$DOCKER_LOG_FILE"

  formatted_services_accumulator+="$formatted_service\n\n"
  echo "$formatted_services_accumulator"
}

iterate_csv_records() {
  local services_accumulator=$1

  firstline=true
  while IFS=',' read -r app_name docker_image dependencies host_port container_port volumes || [ -n "$app_name" ]; do
    # Ignore headers
    if $firstline; then
        firstline=false
        continue
    fi

    # Ignore comments
    if [[ $app_name != "#"* ]]; then
      services_accumulator=$(process_csv_record "$services_accumulator" "$app_name" "$docker_image" "$dependencies" "$host_port" "$container_port" "$volumes")
    fi

  done < <(sed 's/\r//g' "$PARAMETERS_CSV")
  echo "$services_accumulator"
}

echo "# Docker Compose construction started" > "$DOCKER_LOG_FILE"

docker_compose_template=$(<"$DOCKER_COMPOSE_TEMPLATE")
services=""
services=$(iterate_csv_records "$services")
docker_compose_template="${docker_compose_template//@services/$services}"

output_file="./../docker-compose.yml"
echo -e "$docker_compose_template" > "$output_file"
echo -e "${CHECK_SYMBOL} created: $output_file"