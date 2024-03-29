#!/bin/bash

CHECK_SYMBOL="\033[0;32m\xE2\x9C\x94\033[0m"
PV_TEMPLATE="persistent-volume.template.yaml"
PVC_TEMPLATE="persistent-volume-claim.template.yaml"

if [ "$#" -ne 5 ]; then
    echo "Usage: $0 <APP_NAME> <HOST_MOUNT_PATH> <TEMPLATES_PATH> <PERSISTENT_TYPE> <MANIFESTS_PATH>"
    exit 1
fi

APP_NAME=$1
HOST_MOUNT_PATH=$2
TEMPLATES_PATH=$3
PERSISTENT_TYPE=$4
MANIFESTS_PATH=$5

template=""
suffix="PV"
if [ "$PERSISTENT_TYPE" = "PV" ]; then
  template=$(<"$TEMPLATES_PATH"/$PV_TEMPLATE)
  template="${template//APP_NAME/$APP_NAME}"
  template="${template//HOST_MOUNT_PATH/$HOST_MOUNT_PATH}"
  suffix=pv
fi
if [ "$PERSISTENT_TYPE" = "PVC" ]; then
  template=$(<"$TEMPLATES_PATH"/$PVC_TEMPLATE)
  template="${template//APP_NAME/$APP_NAME}"
  suffix=pvc
fi

OUTPUT_DIR="$MANIFESTS_PATH/$APP_NAME"
OUTPUT_FILE="$suffix-$APP_NAME.yaml"

if [ ! -d "$OUTPUT_DIR" ]; then
    mkdir -p "$OUTPUT_DIR"
fi
echo -e "$template" > "$OUTPUT_DIR/$OUTPUT_FILE"
echo -e "${CHECK_SYMBOL} created: $OUTPUT_FILE"