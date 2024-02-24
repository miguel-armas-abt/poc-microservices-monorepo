#!/bin/bash

if [ "$#" -ne 4 ] && [ "$#" -ne 5 ]; then
    echo "Usage: $0 <APP_NAME> <PORT> <NODE_PORT> <SERVICE_TEMPLATE> <CLUSTER_IP>"
    exit 1
fi

APP_NAME=$1
PORT=$2
NODE_PORT=$3
SERVICE_TEMPLATE=$4
CLUSTER_IP=$5

# Replace occurrences
template=$(<./scripts/templates/"$SERVICE_TEMPLATE")
template="${template//APP_NAME/$APP_NAME}"
template="${template//CLUSTER_IP/$CLUSTER_IP}"
template="${template//NODE_PORT/$NODE_PORT}"
template="${template//PORT/$PORT}"

OUTPUT_DIR="./apps/$APP_NAME"
OUTPUT_FILE="svc-$APP_NAME.yaml"

if [ ! -d "$OUTPUT_DIR" ]; then
    mkdir -p "$OUTPUT_DIR"
fi
echo -e "$template" > "$OUTPUT_DIR/$OUTPUT_FILE"
echo "created: $OUTPUT_FILE"