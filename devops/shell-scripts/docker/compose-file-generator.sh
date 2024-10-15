#!/bin/bash

source ./../commons.sh

COMPONENTS_CSV="./../../../application/components.csv"
BACKEND_PATH="./../../../application/backend"
DOCKER_COMPOSE_TEMPLATE=./templates/docker-compose.yml
SERVICE_TEMPLATE=./templates/service.yml

build_dependencies() {
  local dependencies=$1

  formatted_dependencies=""
  if [ "$dependencies" != "none" ]; then
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
  local values_file=$1
  local result=""

  process_section() {
    local section_name=$1
    declare -n section_map=$2
    local key_regex="([a-zA-Z0-9\-]+)"

    if grep -q "$section_name:" "$values_file"; then
      while IFS=":" read -r key value; do
        if [[ $key =~ ^[[:space:]]+($key_regex)$ ]]; then
          key="${BASH_REMATCH[1]}"
          key=$(echo "$key" | tr '-' '_' | tr '[:lower:]' '[:upper:]') #key
          value=$(echo "$value" | sed 's/^\s*//;s/^\s*\"//;s/\"\s*$//') #value
          section_map["$key"]="$value"
        fi
      done < <(awk "/$section_name:/{f=1; next} f && /^[[:space:]]/{print} !/^[[:space:]]/{f=0}" "$values_file")

      for key in "${!section_map[@]}"; do
        result+="      - ${key}=${section_map[$key]}\n"
      done
    fi
  }

  declare -A configMaps
  declare -A secrets

  process_section "configMaps" configMaps
  process_section "secrets" secrets

  echo -e "$result"
}

build_volumes() {
  local volumes=$1

  formatted_volumes=""
  if [ "$volumes" != "none" ]; then
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
  docker_image="$repository:$tag_version"
  host_port=$(awk '/hostPort:/ {print $2}' "$values_file" | sed 's/"//g')
  container_port=$(awk '/containerPort:/ {print $2}' "$values_file" | sed 's/"//g')
  dependencies=$(awk '/dependencies:/ {print $2}' "$values_file" | sed 's/"//g')
  volumes=$(awk '/volumes:/ {print $2}' "$values_file" | sed 's/"//g')

  formatted_service=$(<"$SERVICE_TEMPLATE")
  formatted_service="${formatted_service//@app_name/$component_name}"
  formatted_service="${formatted_service//@docker_image/$docker_image}"
  formatted_service="${formatted_service//@host_port/$host_port}"
  formatted_service="${formatted_service//@container_port/$container_port}"

  dependencies=$(build_dependencies "$dependencies")
  formatted_service="${formatted_service//@dependencies/$dependencies}"

  if grep -q 'secrets:' "$values_file" || grep -q 'configMaps:' "$values_file"; then
      variables=$(build_variables "$values_file")
      formatted_service="${formatted_service//@variables/"environment: \n$variables"}"
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

    # Ignore comments
    if [[ $app_name != "#"* ]]; then
      template_accumulator=$(process_csv_record "$template_accumulator" "$component_name" "$component_type")
    fi

  done < <(sed 's/\r//g' "$COMPONENTS_CSV")
  echo "$template_accumulator"
}

docker_compose_template=$(<"$DOCKER_COMPOSE_TEMPLATE")
services=$(iterate_csv_records)
docker_compose_template="${docker_compose_template//@services/$services}"

output_file="./../../docker-compose.yml"
echo -e "$docker_compose_template" > "$output_file"
echo -e "${CHECK_SYMBOL} created: $output_file"