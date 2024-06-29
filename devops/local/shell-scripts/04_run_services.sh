#!/bin/bash

source ./../parameters/00_local_path_variables.sh
SERVICES_CSV=./../parameters/04_services-to-run.csv

declare -A application_map
application_map["BUSINESS"]="$BUSINESS_ABSOLUTE_PATH"
application_map["INFRASTRUCTURE"]="$INFRASTRUCTURE_ABSOLUTE_PATH"

build_go_run_command() {
  local app_name=$1

  execution_command="$GO_PATH run main.go"
  echo "$execution_command"
}

build_java_run_command() {
  local app_name=$1
  local application_path=$2

  execution_command="$JAVA_COMMAND -jar $application_path/target/$app_name-0.0.1-SNAPSHOT.jar"
  echo "$execution_command"
}

run_service() {
  local file=$1
  local app_name=$2
  local application_path=$3

  execution_command=""
  if [ "$file" == "go.mod" ]; then
    execution_command=$(build_go_run_command "$app_name")
  elif [ "$file" == "pom.xml" ]; then
    execution_command=$(build_java_run_command "$app_name" "$application_path")
  fi
  echo "$(get_timestamp) .......... $app_name .......... $execution_command" >> "$LOCAL_LOG_FILE"
  eval start "$execution_command"
}

process_csv_record() {
  local app_name=$1
  local type=$2

  IFS='|' read -r service_path <<< "${application_map[$type]}"
  application_path="$service_path/$app_name"
  cd "$application_path"

  for file in go.mod pom.xml; do
    if [ -f "$file" ]; then
      run_service "$file" "$app_name" "$application_path"
      break
    fi
  done
}

iterate_csv_records() {
  firstline=true
  while IFS=',' read -r app_name type || [ -n "$app_name" ]; do
    # Ignore headers
    if $firstline; then
        firstline=false
        continue
    fi

    # Ignore comments
    if [[ $app_name != "#"* ]]; then
      process_csv_record "$app_name" "$type"
    fi

  done < <(sed 's/\r//g' "$SERVICES_CSV")
}

echo "Services execution script started" > "$LOCAL_LOG_FILE"
iterate_csv_records
