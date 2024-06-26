#!/bin/bash

source ./../parameters/00_local_path_variables.sh
PROJECTS_CSV=./../parameters/01_projects-to-compile.csv

declare -A application_map
application_map["BUSINESS"]="$BUSINESS_PATH"
application_map["COMMONS"]="$COMMONS_PATH"
application_map["INFRASTRUCTURE"]="$INFRASTRUCTURE_PATH"

process_csv_record() {
  local app_name=$1
  local type=$2

  IFS='|' read -r service_path <<< "${application_map[$type]}"
  cd "$service_path/$app_name"

  execution_command="mvn clean install -Dmaven.home=\"$MVN_HOME_PATH\" -Dmaven.repo.local=\"$MVN_REPOSITORY_PATH\""
  echo "$(get_timestamp) .......... $APP_NAME .......... $execution_command" >> "$LOCAL_LOG_FILE"
  eval "$execution_command"
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

  done < <(sed 's/\r//g' "$PROJECTS_CSV")
}

echo "# Compilation script started" > "$LOCAL_LOG_FILE"
iterate_csv_records
