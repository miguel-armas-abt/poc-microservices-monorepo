#!/bin/bash

if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <HOST_MOUNT_PATH>"
    exit 1
fi

APP_NAME=$1

# Read the template file and store it in a variable
template=$(<./templates/db.persistent-volume-claim.template.yaml)

# Replace the placeholder in the template with the provided APP_NAME
template="${template//APP_NAME/$APP_NAME}"

# Write the resulting ConfigMap to a file
OUTPUT_DIR="./../databases/$APP_NAME"
if [ ! -d "$OUTPUT_DIR" ]; then
    mkdir -p "$OUTPUT_DIR"
fi
echo -e "$template" > "$OUTPUT_DIR/pvc-$APP_NAME.yaml"

echo "Persistent Volume Claim generated successfully: pvc-$APP_NAME.yaml"