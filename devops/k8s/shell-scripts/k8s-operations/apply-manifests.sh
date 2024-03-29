#!/bin/bash

source ./../../../environment/common-script.sh
K8S_LOG_FILE=./../../../$K8S_LOG_FILE
MANIFESTS_FOLDER="./../../manifests"

echo "# K8S executions started" > "$K8S_LOG_FILE"

if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <OPERATION>"
    exit 1
fi

OPERATION=$1

for folder in "$MANIFESTS_FOLDER"/*/; do
    app_name=$(basename "$folder")
    echo -e "${WRENCH_SYMBOL} ........................................ $app_name"

    EXECUTION_COMMAND=""
    if [[ $OPERATION == "apply"* ]]; then
      EXECUTION_COMMAND="kubectl apply -f "$folder" -n restaurant"
    fi

    if [[ $OPERATION == "delete"* ]]; then
      EXECUTION_COMMAND="kubectl delete -f "$folder" -n restaurant"
    fi

    echo "$(get_timestamp) .......... $app_name .......... $EXECUTION_COMMAND" >> "$K8S_LOG_FILE"
    eval "$EXECUTION_COMMAND"
done
