#!/bin/bash

source ./../../common-script.sh

COMPONENTS_CSV="./../../../application/components.csv"
BACKEND_PATH="./../../../application/backend"
DOCKER_COMPOSE_TEMPLATE=./templates/docker-compose.yml
SERVICE_TEMPLATE=./templates/service.yml

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

  environment_file="$ENVIRONMENT_VARIABLES_PATH/$app_name/$app_name.env"
  secrets_file="$ENVIRONMENT_VARIABLES_PATH/$app_name/secret-$app_name.env"

  formatted_variables=""
  if [[ -f "$environment_file" && -s "$environment_file" ]]; then
    formatted_variables+=$(grep -v '^#' "$environment_file" | sed 's/^/      - /')
  fi

  if [[ -f "$secrets_file" && -s "$secrets_file" ]]; then
    formatted_variables+=$(grep -v '^#' "$secrets_file" | sed 's/^/      - /')
  fi

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
  local template_accumulator=$1
  local component_name=$2
  local component_type=$3

  component_path="$BACKEND_PATH/$component_type/$component_name"
  values_file="$component_path/values.yaml"
  repository=$(awk '/repository:/ {print $2}' "$values_file" | sed 's/"//g')
  tag_version=$(awk '/tag:/ {print $2}' "$values_file" | sed 's/"//g')
  image="$repository:$tag_version"

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

  template_accumulator+="$formatted_service\n\n"
  echo "$template_accumulator"
}

iterate_csv_records() {
  local template_accumulator=""

  firstline=true
  while IFS=',' read -r component_name component_type helm_template namespace || [ -n "$component_name" ]; do
    # Ignore headers
    if $firstline; then
        firstline=false
        continue
    fi

    # Ignore comments
    if [[ $app_name != "#"* ]]; then
      template_accumulator=$(process_csv_record "$template_accumulator" "$component_name" "$component_type")
    fi

  done < <(sed 's/\r//g' "$COMPONENTS_CSV")
  echo "$template_accumulator"
}

echo "# Docker Compose construction started" > "$DOCKER_LOG_FILE"

docker_compose_template=$(<"$DOCKER_COMPOSE_TEMPLATE")
services=$(iterate_csv_records)
docker_compose_template="${docker_compose_template//@services/$services}"

output_file="./../docker-compose.yml"
echo -e "$docker_compose_template" > "$output_file"
echo -e "${CHECK_SYMBOL} created: $output_file"