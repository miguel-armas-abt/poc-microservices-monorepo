#!/bin/bash

WRENCH_SYMBOL="\xE2\x9C\xA8"
MANIFESTS_FOLDER="./../../manifests"

if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <OPERATION>"
    exit 1
fi

OPERATION=$1

for folder in "$MANIFESTS_FOLDER"/*/; do
    app_name=$(basename "$folder")
    echo -e "${WRENCH_SYMBOL} ........................ $app_name"

    if [[ $OPERATION == "apply"* ]]; then
      kubectl apply -f "$folder" -n restaurant
    fi

    if [[ $OPERATION == "delete"* ]]; then
      kubectl delete -f "$folder" -n restaurant
    fi
done
