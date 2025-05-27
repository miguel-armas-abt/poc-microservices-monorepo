#!/bin/bash
set -e

source ./../commons.sh
source ./../variables.env

DOCKER_COMPOSE_TEMPLATE=./templates/docker-compose.yml
SERVICE_TEMPLATE=./templates/service.yml
DOCKER_COMPOSE_FILE="./docker-compose.yml";

build_dependencies() {
  local dependencies=$1

  if [ "$dependencies" != "none" ]; then
    formatted_dependencies=$(echo "$dependencies" | awk -F';' '{for(i=1;i<=NF;i++) printf "      - %s\n", $i}')
    echo -e "depends_on:\n$formatted_dependencies"
  fi
}

build_variables() {
  local values_file=$1
  local result=""

  configMaps=$(yq '.configMaps // {}' "$values_file")
  secrets=$(yq '.secrets // {}' "$values_file")

  for key in $(echo "$configMaps" | yq 'keys | .[]'); do
    value=$(echo "$configMaps" | yq ".\"$key\"")
    result+="      - $(echo "$key" | tr '-' '_' | tr '[:lower:]' '[:upper:]')=$value\n"
  done

  for key in $(echo "$secrets" | yq 'keys | .[]'); do
    value=$(echo "$secrets" | yq ".\"$key\"")
    result+="      - $(echo "$key" | tr '-' '_' | tr '[:lower:]' '[:upper:]')=$value\n"
  done

  echo -e "$result"
}

build_volumes() {
  local volumes=$1

  if [ "$volumes" != "none" ]; then
    formatted_volumes=$(echo "$volumes" | awk -F';' '{for(i=1;i<=NF;i++) printf "      - %s\n", $i}')
    echo -e "volumes:\n$formatted_volumes"
  fi
}

process_csv_record() {
  local template_accumulator=$1
  local component_name=$2
  local component_type=$3

  component_path="$BACKEND_PATH/$component_type/$component_name"
  values_file="$component_path/values.yaml"

  repository=$(yq '.container.image.repository' "$values_file")
  tag_version=$(yq '.container.image.tag' "$values_file")
  docker_image="$repository:$tag_version"
  host_port=$(yq '.docker.hostPort' "$values_file")
  container_port=$(yq '.container.port' "$values_file")
  dependencies=$(yq '.docker.dependencies // "none"' "$values_file")
  volumes=$(yq '.docker.volumes // "none"' "$values_file")

  formatted_service=$(<"$SERVICE_TEMPLATE")
  formatted_service="${formatted_service//@component_path/$component_path}"
  formatted_service="${formatted_service//@app_name/$component_name}"
  formatted_service="${formatted_service//@docker_image/$docker_image}"
  formatted_service="${formatted_service//@host_port/$host_port}"
  formatted_service="${formatted_service//@container_port/$container_port}"

  dependencies=$(build_dependencies "$dependencies")
  formatted_service="${formatted_service//@dependencies/$dependencies}"

  variables=$(build_variables "$values_file")
  if [ -n "$variables" ]; then
    formatted_service="${formatted_service//@variables/"environment:\n$variables"}"
  else
    formatted_service="${formatted_service//@variables/""}"
  fi

  volumes=$(build_volumes "$volumes")
  formatted_service="${formatted_service//@volumes/$volumes}"

  template_accumulator+="$formatted_service\n\n"
  echo "$template_accumulator"
}

iterate_csv_records() {
  local template_accumulator=""

  firstline=true
  while IFS=',' read -r component_name component_type || [ -n "$component_name" ]; do
    # Ignore headers
    if $firstline; then
        firstline=false
        continue
    fi

    # Ignore comments and parents
    if [[ $component_name != "#"* ]] && [[ $component_type != "$PARENT_TYPE" ]]; then
      template_accumulator=$(process_csv_record "$template_accumulator" "$component_name" "$component_type")
    fi

  done < <(sed 's/\r//g' "$COMPONENTS_CSV")
  echo "$template_accumulator"
}

build_compose_file() {
  docker_compose_template=$(<"$DOCKER_COMPOSE_TEMPLATE")
  services=$(iterate_csv_records)
  docker_compose_template="${docker_compose_template//@services/$services}"
  docker_compose_template=$(echo "$docker_compose_template" | sed '/^[[:space:]]*$/d') #delete empty lines

  echo -e "$docker_compose_template" > "$DOCKER_COMPOSE_FILE"
  echo -e "${CHECK_SYMBOL} created: $DOCKER_COMPOSE_FILE"
}

build_compose_file