#!/bin/bash

source ./../parameters/00_local_path_variables.sh
SERVICES_CSV=./../parameters/04_services-to-start.csv

echo "Services execution script started" > "$LOCAL_LOG_FILE"

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

    #get service path
    if [[ "$TYPE" == "INF" ]]; then
      SERVICE_PATH="$INFRASTRUCTURE_PATH/$APP_NAME"
    elif [[ "$TYPE" == "BS" ]]; then
      SERVICE_PATH="$BUSINESS_PATH/$APP_NAME"
    fi

    cd "$SERVICE_PATH"

    #verify service type
    EXECUTION_COMMAND=""
    for file in go.mod pom.xml; do
        if [ -f "$file" ]; then
            if [ "$file" == "go.mod" ]; then
              EXECUTION_COMMAND="$GO_PATH run main.go"
            elif [ "$file" == "pom.xml" ]; then
              EXECUTION_COMMAND="$JAVA_COMMAND -jar $SERVICE_PATH/target/$APP_NAME-0.0.1-SNAPSHOT.jar"
            fi
            break
        fi
    done

    echo "$(get_timestamp) .......... $APP_NAME .......... $EXECUTION_COMMAND" >> "$LOCAL_LOG_FILE"
    eval start "$EXECUTION_COMMAND"

  fi

done < <(sed 's/\r//g' "$SERVICES_CSV")