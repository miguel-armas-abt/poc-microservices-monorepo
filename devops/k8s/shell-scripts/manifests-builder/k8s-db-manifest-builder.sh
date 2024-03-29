#!/bin/bash

if [ "$#" -ne 8 ]; then
    echo "Usage: $0 <RESOURCES_PATH> <SECRET_TEMPLATE> <TEMPLATES_PATH> <SERVICE_TEMPLATE> <CONTROLLER_TEMPLATE> <K8S_PARAMETERS_CSV> <CONTAINERS_CSV> <MANIFESTS_PATH>"
    exit 1
fi

RESOURCES_PATH=$1
SECRET_TEMPLATE=$2
TEMPLATES_PATH=$3
SERVICE_TEMPLATE=$4
CONTROLLER_TEMPLATE=$5
K8S_PARAMETERS_CSV=$6
CONTAINERS_CSV=$7
MANIFESTS_PATH=$8

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
    while IFS=',' read -r APP_NAME SUB_PATH_INIT_DB CLUSTER_IP NODE_PORT CONTROLLER_TYPE REPLICA_COUNT || [ -n "$APP_NAME" ]; do
      # Ignore headers
      if $firstline; then
          firstline=false
          continue
      fi

      # Ignore comments
      if [[ $APP_NAME != "#"* ]]; then

        # container name equals to k8s app
        if [[ $APP_NAME == "$CONTAINER_NAME" ]]; then

          ENV_FILE="$RESOURCES_PATH/$APP_NAME/$APP_NAME.env"
          extensionFile="${SUB_PATH_INIT_DB##*.}"
          INIT_DB_FILE="$RESOURCES_PATH/$APP_NAME/init/$APP_NAME.initdb.$extensionFile"
          MOUNT_PATH_DATA=/var/lib/$APP_NAME/data
          MOUNT_PATH_INIT_DB=/docker-entrypoint-initdb.d/$SUB_PATH_INIT_DB
          HOST_MOUNT_PATH=\"/mnt/data/\"

          ./builders/controller-builder.sh "$APP_NAME" "$CONTAINER_PORT" "$DOCKER_IMAGE" null "$CONTROLLER_TEMPLATE" "$CONTROLLER_TYPE" "$REPLICA_COUNT" "$MANIFESTS_PATH" true "$MOUNT_PATH_DATA" "$MOUNT_PATH_INIT_DB" "$SUB_PATH_INIT_DB"
          ./builders/persistent-builder.sh "$APP_NAME" $HOST_MOUNT_PATH "$TEMPLATES_PATH" PV "$MANIFESTS_PATH"
          ./builders/persistent-builder.sh "$APP_NAME" null "$TEMPLATES_PATH" PVC "$MANIFESTS_PATH"
          ./builders/service-builder.sh "$APP_NAME" "$CONTAINER_PORT" "$NODE_PORT" "$SERVICE_TEMPLATE" "$MANIFESTS_PATH" "$CLUSTER_IP"
          ./builders/config-map-builder.sh "$APP_NAME" "$INIT_DB_FILE" "$TEMPLATES_PATH" true "$MANIFESTS_PATH" "$SUB_PATH_INIT_DB"
          ./builders/secret-builder.sh "$APP_NAME" "$ENV_FILE" "$SECRET_TEMPLATE" "$MANIFESTS_PATH"
        fi

      fi

    done < "$K8S_PARAMETERS_CSV"

  fi
done < "$CONTAINERS_CSV"