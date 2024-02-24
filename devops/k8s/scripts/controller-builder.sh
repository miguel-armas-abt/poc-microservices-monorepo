#!/bin/bash

#validate input to app or database controller
if [ "$#" -ne 7 ] && [ "$#" -ne 11 ]; then
    echo "Usage: $0 <APP_NAME> <CONTAINER_PORT> <DOCKER_IMAGE> <ENV_FILE> <CONTROLLER_TEMPLATE> <CONTROLLER_TYPE> <IS_DATABASE> <MOUNT_PATH_DATA> <MOUNT_PATH_INIT_DB> <SUB_PATH_INIT_DB> <CONTAINER_NAME>"
    exit 1
fi

APP_NAME=$1
CONTAINER_PORT=$2
DOCKER_IMAGE=$3
ENV_FILE=$4
CONTROLLER_TEMPLATE=$5
CONTROLLER_TYPE=$6
IS_DATABASE=$7

MOUNT_PATH_DATA=$8
MOUNT_PATH_INIT_DB=$9
SUB_PATH_INIT_DB=${10}
CONTAINER_NAME=${11}

template=$(<./scripts/templates/$CONTROLLER_TEMPLATE)

kind=""
suffix=""
#if contains
if [[ "$CONTROLLER_TYPE" == *"DEP"* ]]; then
  kind=Deployment
  suffix=dep
  template="${template//SERVICE_NAME/""}"
fi

if [[ "$CONTROLLER_TYPE" == *"STS"* ]]; then
  kind=StatefulSet
  suffix=sts
  template="${template//SERVICE_NAME/"serviceName: $APP_NAME"}"
fi

template="${template//KIND_CONTROLLER/$kind}"
template="${template//APP_NAME/$APP_NAME}"
template="${template//DOCKER_IMAGE/$DOCKER_IMAGE}"
template="${template//CONTAINER_PORT/$CONTAINER_PORT}"

if [ "$IS_DATABASE" = true ]; then
  template="${template//MOUNT_PATH_DATA/$MOUNT_PATH_DATA}"
  template="${template//MOUNT_PATH_INIT_DB/$MOUNT_PATH_INIT_DB}"
  template="${template//SUB_PATH_INIT_DB/$SUB_PATH_INIT_DB}"
  template="${template//CONTAINER_NAME/$CONTAINER_NAME}"
else
  # Read the environment variables
  env_vars=""
  while IFS='=' read -r key value || [ -n "$key" ]; do
      keyLower=$(echo "$key" | tr '_' '-')
      env_vars+="\n            - name: $key\n              valueFrom:\n                configMapKeyRef:\n                  name: cm-$APP_NAME\n                  key: ${keyLower,,}"
  done < "$ENV_FILE"

  # Insert environment variables
  template="${template/ENVIRONMENT_VARIABLES/$env_vars}"
fi

OUTPUT_DIR="./apps/$APP_NAME"
OUTPUT_FILE="$suffix-$APP_NAME.yaml"

if [ ! -d "$OUTPUT_DIR" ]; then
    mkdir -p "$OUTPUT_DIR"
fi
echo -e "$template" > "$OUTPUT_DIR/$OUTPUT_FILE"
echo "created: $OUTPUT_FILE"
