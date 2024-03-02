#!/bin/bash

if [ "$#" -ne 4 ]; then
    echo "Usage: $0 <VARIABLES_PATH> <CONFIG_MAP_TEMPLATE> <SERVICE_TEMPLATE> <CONTROLLER_TEMPLATE>"
    exit 1
fi

VARIABLES_PATH=$1
CONFIG_MAP_TEMPLATE=$2
SERVICE_TEMPLATE=$3
CONTROLLER_TEMPLATE=$4

#read csv
firstline=true
while IFS=',' read -r APP_NAME PORT NODE_PORT IMAGE CONTROLLER_TYPE || [ -n "$APP_NAME" ]; do
  # Ignore headers
  if $firstline; then
      firstline=false
      continue
  fi

  # Ignore comments
  if [[ $APP_NAME != "#"* ]]; then
    DATA_FILE="$VARIABLES_PATH/$APP_NAME.env"
    ./shell-scripts/config-map-builder.sh "$APP_NAME" "$DATA_FILE" "$CONFIG_MAP_TEMPLATE" false
    ./shell-scripts/service-builder.sh "$APP_NAME" "$PORT" "$NODE_PORT" "$SERVICE_TEMPLATE" null
    ./shell-scripts/controller-builder.sh "$APP_NAME" "$PORT" "$IMAGE" "$DATA_FILE" "$CONTROLLER_TEMPLATE" "$CONTROLLER_TYPE" false
  fi

done < ./shell-scripts/apps/k8s-app-parameters.csv
