#!/bin/bash

if [ "$#" -ne 7 ]; then
    echo "Usage: $0 <VARIABLES_PATH> <TEMPLATES_PATH> <SERVICE_TEMPLATE> <CONTROLLER_TEMPLATE> <K8S_PARAMETERS_CSV> <CONTAINERS_CSV> <MANIFESTS_PATH>"
    exit 1
fi

VARIABLES_PATH=$1
TEMPLATES_PATH=$2
SERVICE_TEMPLATE=$3
CONTROLLER_TEMPLATE=$4
K8S_PARAMETERS_CSV=$5
CONTAINERS_CSV=$6
MANIFESTS_PATH=$7

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
          ./builders/config-map-builder.sh "$APP_NAME" "$DATA_FILE" "$TEMPLATES_PATH" false "$MANIFESTS_PATH" null
          ./builders/service-builder.sh "$APP_NAME" "$CONTAINER_PORT" "$NODE_PORT" "$SERVICE_TEMPLATE" "$MANIFESTS_PATH" null
          ./builders/controller-builder.sh "$APP_NAME" "$CONTAINER_PORT" "$DOCKER_IMAGE" "$DATA_FILE" "$CONTROLLER_TEMPLATE" "$CONTROLLER_TYPE" "$REPLICA_COUNT" "$MANIFESTS_PATH" false

        fi

      fi
      done < "$K8S_PARAMETERS_CSV"

  fi
done < "$CONTAINERS_CSV"