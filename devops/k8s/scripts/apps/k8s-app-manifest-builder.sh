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

  DATA_FILE="$VARIABLES_PATH/$APP_NAME.env"

  ./config-map-builder.sh "$APP_NAME" "$DATA_FILE" "$CONFIG_MAP_TEMPLATE" false
  ./service-builder.sh "$APP_NAME" "$PORT" "$NODE_PORT" "$SERVICE_TEMPLATE" null
  ./controller-builder.sh "$APP_NAME" "$PORT" "$IMAGE" "$DATA_FILE" "$CONTROLLER_TEMPLATE" "$CONTROLLER_TYPE" false

done < ./apps/k8s-app-arguments.csv
