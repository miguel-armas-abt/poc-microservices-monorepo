#!/bin/bash

source ./../parameters/00_local_path_variables.sh
PROJECTS_CSV=./../parameters/01_projects-to-compile.csv

echo "# Installation script started" > "$LOCAL_LOG_FILE"

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
    if [[ "$TYPE" == "INFRASTRUCTURE" ]]; then
      SERVICE_PATH="$INFRASTRUCTURE_PATH/$APP_NAME"
    elif [[ "$TYPE" == "BUSINESS" ]]; then
      SERVICE_PATH="$BUSINESS_PATH/$APP_NAME"
    elif [[ "$TYPE" == "COMMONS" ]]; then
      SERVICE_PATH="$COMMONS_PATH/$APP_NAME"
    fi
    cd "$SERVICE_PATH"

    EXECUTION_COMMAND="mvn clean install -Dmaven.home=\"$MVN_HOME_PATH\" -Dmaven.repo.local=\"$MVN_REPOSITORY_PATH\""
    echo "$(get_timestamp) .......... $APP_NAME .......... $EXECUTION_COMMAND" >> "$LOCAL_LOG_FILE"
    eval "$EXECUTION_COMMAND"

  fi

done < <(sed 's/\r//g' "$PROJECTS_CSV")