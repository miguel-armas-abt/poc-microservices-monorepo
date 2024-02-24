#!/bin/bash

if [ "$#" -ne 7 ]; then
    echo "Usage: $0 <RESOURCES_PATH> <SECRET_TEMPLATE> <PV_TEMPLATE> <PVC_TEMPLATE> <CONFIG_MAP_TEMPLATE> <SERVICE_TEMPLATE> <CONTROLLER_TEMPLATE>"
    exit 1
fi

RESOURCES_PATH=$1
SECRET_TEMPLATE=$2
PV_TEMPLATE=$3
PVC_TEMPLATE=$4
CONFIG_MAP_TEMPLATE=$5
SERVICE_TEMPLATE=$6
CONTROLLER_TEMPLATE=$7

#read csv
firstline=true
while IFS=',' read -r APP_NAME CONTAINER_NAME IMAGE PORT SUB_PATH_INIT_DB CLUSTER_IP NODE_PORT CONTROLLER_TYPE || [ -n "$APP_NAME" ]; do
  # Ignore headers
  if $firstline; then
      firstline=false
      continue
  fi

  ENV_FILE="$RESOURCES_PATH/$APP_NAME.env"
  INIT_DB_FILE="$RESOURCES_PATH/$APP_NAME.init-db.yml"
  MOUNT_PATH_DATA=/var/lib/$APP_NAME/data
  MOUNT_PATH_INIT_DB=/docker-entrypoint-initdb.d/$SUB_PATH_INIT_DB
  HOST_MOUNT_PATH=\"/mnt/data/\"

  ./controller-builder.sh "$APP_NAME" "$PORT" "$IMAGE" null "$CONTROLLER_TEMPLATE" "$CONTROLLER_TYPE" true "$MOUNT_PATH_DATA" "$MOUNT_PATH_INIT_DB" "$SUB_PATH_INIT_DB" "$CONTAINER_NAME"
  ./persistent-builder.sh "$APP_NAME" $HOST_MOUNT_PATH "$PV_TEMPLATE" PV
  ./persistent-builder.sh "$APP_NAME" null "$PVC_TEMPLATE" PVC
  ./service-builder.sh "$APP_NAME" "$PORT" "$NODE_PORT" "$SERVICE_TEMPLATE" "$CLUSTER_IP"
  ./config-map-builder.sh "$APP_NAME" "$INIT_DB_FILE" "$CONFIG_MAP_TEMPLATE" true
  ./secret-builder.sh "$APP_NAME" "$ENV_FILE" "$SECRET_TEMPLATE"

done < ./databases/k8s-db-arguments.csv




