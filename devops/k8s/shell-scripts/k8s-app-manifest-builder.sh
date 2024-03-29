#!/bin/bash

if [ "$#" -ne 4 ]; then
    echo "Usage: $0 <VARIABLES_PATH> <CONFIG_MAP_TEMPLATE> <SERVICE_TEMPLATE> <CONTROLLER_TEMPLATE>"
    exit 1
fi

VARIABLES_PATH=$1
CONFIG_MAP_TEMPLATE=$2
SERVICE_TEMPLATE=$3
CONTROLLER_TEMPLATE=$4

#loop - read csv
containers_firstline=true
while IFS=',' read -r CONTAINER_NAME DOCKER_IMAGE DEPENDENCIES HOST_PORT CONTAINER_PORT VOLUMES || [ -n "$CONTAINER_NAME" ]; do
  # Ignore headers
  if $containers_firstline; then
      containers_firstline=false
      continue
  fi

  # Ignore comments
  if [[ $CONTAINER_NAME != "#"* ]]; then

    #loop - read csv
    firstline=true
    while IFS=',' read -r APP_NAME NODE_PORT CONTROLLER_TYPE REPLICA_COUNT || [ -n "$APP_NAME" ]; do
      # Ignore headers
      if $firstline; then
          firstline=false
          continue
      fi

      # Ignore comments
      if [[ $APP_NAME != "#"* ]]; then

        # container name equals to k8s app
        if [[ $APP_NAME == "$CONTAINER_NAME" ]]; then

          DATA_FILE="$VARIABLES_PATH/$APP_NAME.env"
          ./builders/config-map-builder.sh "$APP_NAME" "$DATA_FILE" "$CONFIG_MAP_TEMPLATE" false
          ./builders/service-builder.sh "$APP_NAME" "$CONTAINER_PORT" "$NODE_PORT" "$SERVICE_TEMPLATE" null
          ./builders/controller-builder.sh "$APP_NAME" "$CONTAINER_PORT" "$DOCKER_IMAGE" "$DATA_FILE" "$CONTROLLER_TEMPLATE" "$CONTROLLER_TYPE" "$REPLICA_COUNT" false

        fi

      fi
      done < ./../parameters/k8s-app-parameters.csv

  fi
done < ./../../environment/docker/containers-to-run.csv


