#!/bin/bash

source ./../../common-script.sh
DOCKER_LOG_FILE=./../../$DOCKER_LOG_FILE
IMAGES_ENABLED_CSV=./../images-to-build.csv
USER_PREFIX="miguelarmasabt"

declare -A application_map
application_map["BUSINESS"]="./../../../$BUSINESS_RELATIVE_PATH"
application_map["INFRASTRUCTURE"]="./../../../$INFRASTRUCTURE_RELATIVE_PATH"

build_service_version() {
  local tag_version=$1
  
  service_version=$(echo "$tag_version" | cut -d '.' -f 1)
  echo "$service_version"
}

process_csv_record() {
  local app_name=$1
  local tag_version=$2
  local type=$3
  local dockerfile_path=$4
  
  service_version=$(build_service_version "$tag_version")
  app_folder=$app_name-$service_version
  
  IFS='|' read -r service_path <<< "${application_map[$type]}"
  service_path="$service_path/$app_folder"
  
  docker_tag="-t $USER_PREFIX/$app_name:$tag_version $service_path"

  execution_command=""
  if [[ $dockerfile_path != "Default"* ]]; then
    docker_file="-f $service_path/$dockerfile_path"
    execution_command="docker build $docker_file $docker_tag"
  else
    execution_command="docker build $docker_tag"
  fi

  echo "$(get_timestamp) .......... $app_name .......... $execution_command" >> "$DOCKER_LOG_FILE"
  eval "$execution_command"
}

iterate_csv_records() {
  firstline=true
  while IFS=',' read -r app_name tag_version type dockerfile_path || [ -n "$app_name" ]; do
    # Ignore headers
    if $firstline; then
        firstline=false
        continue
    fi
  
    # Ignore comments
    if [[ $app_name != "#"* ]]; then
      process_csv_record "$app_name" "$tag_version" "$type" "$dockerfile_path"
    fi
  done < <(sed 's/\r//g' "$IMAGES_ENABLED_CSV")
}

echo "# Docker images construction started" > "$DOCKER_LOG_FILE"
iterate_csv_records







