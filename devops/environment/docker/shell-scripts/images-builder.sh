#!/bin/bash

source ./../../../local/parameters/00_local_path_variables.sh
IMAGES_ENABLED_CSV=./../images-to-build.csv

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

    if [[ $DOCKERFILE_PATH != "Default"* ]]; then
      DOCKER_FILE="-f $SERVICE_PATH/$DOCKERFILE_PATH"

      docker build $DOCKER_FILE $DOCKER_TAG
    else
      docker build $DOCKER_TAG
    fi

  fi

done < <(sed 's/\r//g' "$IMAGES_ENABLED_CSV")