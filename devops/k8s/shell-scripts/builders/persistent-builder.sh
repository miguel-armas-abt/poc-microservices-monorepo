#!/bin/bash

CHECK_SYMBOL="\033[0;32m\xE2\x9C\x94\033[0m"

if [ "$#" -ne 4 ]; then
    echo "Usage: $0 <APP_NAME> <HOST_MOUNT_PATH> <PERSISTENT_TEMPLATE> <PERSISTENT_TYPE>"
    exit 1
fi

APP_NAME=$1
HOST_MOUNT_PATH=$2
PERSISTENT_TEMPLATE=$3
PERSISTENT_TYPE=$4

template=$(<./templates/$PERSISTENT_TEMPLATE)
template="${template//APP_NAME/$APP_NAME}"

suffix="PV"
if [ "$PERSISTENT_TYPE" = "PV" ]; then
  template="${template//HOST_MOUNT_PATH/$HOST_MOUNT_PATH}"
  suffix=pv
fi
if [ "$PERSISTENT_TYPE" = "PVC" ]; then
  suffix=pvc
fi

OUTPUT_DIR="./../manifests/$APP_NAME"
OUTPUT_FILE="$suffix-$APP_NAME.yaml"

if [ ! -d "$OUTPUT_DIR" ]; then
    mkdir -p "$OUTPUT_DIR"
fi
echo -e "$template" > "$OUTPUT_DIR/$OUTPUT_FILE"
echo -e "${CHECK_SYMBOL} created: $OUTPUT_FILE"