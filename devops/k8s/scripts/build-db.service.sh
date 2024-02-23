#!/bin/bash

# Check if the correct number of arguments are provided
if [ "$#" -ne 4 ]; then
    echo "Usage: $0 <APP_NAME> <CLUSTER_IP> <PORT> <NODE_PORT>"
    exit 1
fi

APP_NAME=$1
CLUSTER_IP=$2
PORT=$3
NODE_PORT=$4

# Read the template file and store it in a variable
template=$(<./templates/db.service.template.yaml)

# Replace the placeholder in the template with the provided APP_NAME
template="${template//APP_NAME/$APP_NAME}"
template="${template//CLUSTER_IP/$CLUSTER_IP}"
template="${template//NODE_PORT/$NODE_PORT}"
template="${template//PORT/$PORT}"

# Write the resulting ConfigMap to a file
OUTPUT_DIR="./../databases/$APP_NAME"
if [ ! -d "$OUTPUT_DIR" ]; then
    mkdir -p "$OUTPUT_DIR"
fi
echo -e "$template" > "$OUTPUT_DIR/svc-$APP_NAME.yaml"

echo "Service generated successfully: svc-$APP_NAME.yaml"