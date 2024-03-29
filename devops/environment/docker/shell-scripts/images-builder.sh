#!/bin/bash

source ./../../common-script.sh
DOCKER_LOG_FILE=./../../../$DOCKER_LOG_FILE
IMAGES_ENABLED_CSV=./../images-to-build.csv

echo "# Docker images construction started" > "$DOCKER_LOG_FILE"

SERVICE_PATH=""
firstline=true
while IFS=',' read -r APP_NAME TAG_VERSION TYPE DOCKERFILE_PATH || [ -n "$APP_NAME" ]; do
  # Ignore headers
  if $firstline; then
      firstline=false
      continue
  fi

  # Ignore comments
  if [[ $APP_NAME != "#"* ]]; then

    VERSION=$(echo "$TAG_VERSION" | cut -d '.' -f 1)
    APP_FOLDER=$APP_NAME-$VERSION

    if [[ "$TYPE" == "INF" ]]; then
      SERVICE_PATH="./../../../../$INF_PATH/$APP_FOLDER"
    elif [[ "$TYPE" == "BS" ]]; then
      SERVICE_PATH="./../../../../$BS_PATH/$APP_FOLDER"
    fi

    USER_PREFIX=miguelarmasabt
    DOCKER_TAG="-t $USER_PREFIX/$APP_NAME:$TAG_VERSION $SERVICE_PATH"

    EXECUTION_COMMAND=""
    if [[ $DOCKERFILE_PATH != "Default"* ]]; then
      DOCKER_FILE="-f $SERVICE_PATH/$DOCKERFILE_PATH"
      EXECUTION_COMMAND="docker build $DOCKER_FILE $DOCKER_TAG"
    else
      EXECUTION_COMMAND="docker build $DOCKER_TAG"
    fi

    echo "$(get_timestamp) .......... $APP_NAME .......... $EXECUTION_COMMAND" >> "$DOCKER_LOG_FILE"
    eval "$EXECUTION_COMMAND"

  fi

done < <(sed 's/\r//g' "$IMAGES_ENABLED_CSV")