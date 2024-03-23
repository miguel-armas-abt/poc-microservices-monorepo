#!/bin/bash

source ./00_local_path_variables.sh
PROJECTS_CSV=./resources/01_projects-to-compile.csv

echo "$(date +"%F %T"): Installation script started" > "$LOG_FILE"

SERVICE_PATH=""
firstline=true
while IFS=',' read -r APP_NAME TYPE || [ -n "$APP_NAME" ]; do
  # Ignore headers
  if $firstline; then
      firstline=false
      continue
  fi

  # Ignore comments
  if [[ $APP_NAME != "#"* ]]; then
    if [[ "$TYPE" == "INF" ]]; then
      SERVICE_PATH="$INFRASTRUCTURE_PATH/$APP_NAME"
    elif [[ "$TYPE" == "BS" ]]; then
      SERVICE_PATH="$BUSINESS_PATH/$APP_NAME"
    fi
    cd "$SERVICE_PATH"
    mvn clean install -Dmaven.home="$MVN_HOME_PATH" -Dmaven.repo.local="$MVN_REPOSITORY_PATH"
    echo "$(date +"%F %T"): $APP_NAME executed" >> "$LOG_FILE"
  fi

done < <(sed 's/\r//g' "$PROJECTS_CSV")