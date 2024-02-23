#!/bin/bash

if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <HOST_MOUNT_PATH>"
    exit 1
fi

APP_NAME=$1
HOST_MOUNT_PATH=$2

# Read the template file and store it in a variable
template=$(<./templates/db.persistent-volume.template.yaml)

# Replace the placeholder in the template with the provided APP_NAME
template="${template//APP_NAME/$APP_NAME}"
template="${template//HOST_MOUNT_PATH/$HOST_MOUNT_PATH}"

# Write the resulting ConfigMap to a file
OUTPUT_DIR="./../databases/$APP_NAME"
if [ ! -d "$OUTPUT_DIR" ]; then
    mkdir -p "$OUTPUT_DIR"
fi
echo -e "$template" > "$OUTPUT_DIR/pv-$APP_NAME.yaml"

echo "Persistent Volume generated successfully: pv-$APP_NAME.yaml"