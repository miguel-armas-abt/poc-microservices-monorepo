#!/bin/bash

if [ "$#" -ne 7 ]; then
    echo "Usage: $0 <APP_NAME> <CONTAINER_NAME> <DOCKER_IMAGE> <CONTAINER_PORT> <MOUNT_PATH_DATA> <MOUNT_PATH_INIT_DB> <SUB_PATH_INIT_DB>"
    exit 1
fi

APP_NAME=$1
CONTAINER_NAME=$2
DOCKER_IMAGE=$3
CONTAINER_PORT=$4
MOUNT_PATH_DATA=$5
MOUNT_PATH_INIT_DB=$6
SUB_PATH_INIT_DB=$7

# Read the template file and store it in a variable
template=$(<./templates/db.deployment.template.yaml)

# Replace the placeholder in the template with the provided APP_NAME
template="${template//APP_NAME/$APP_NAME}"
template="${template//CONTAINER_NAME/$CONTAINER_NAME}"
template="${template//DOCKER_IMAGE/$DOCKER_IMAGE}"
template="${template//CONTAINER_PORT/$CONTAINER_PORT}"
template="${template//MOUNT_PATH_DATA/$MOUNT_PATH_DATA}"
template="${template//MOUNT_PATH_INIT_DB/$MOUNT_PATH_INIT_DB}"
template="${template//SUB_PATH_INIT_DB/$SUB_PATH_INIT_DB}"

# Write the resulting ConfigMap to a file
OUTPUT_DIR="./../databases/$APP_NAME"
if [ ! -d "$OUTPUT_DIR" ]; then
    mkdir -p "$OUTPUT_DIR"
fi
echo -e "$template" > "$OUTPUT_DIR/dep-$APP_NAME.yaml"

echo "Deployment generated successfully: dep-$APP_NAME.yaml"