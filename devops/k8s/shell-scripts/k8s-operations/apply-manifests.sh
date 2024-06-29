#!/bin/bash

source ./../../../common-script.sh
K8S_LOG_FILE=./../../../$K8S_LOG_FILE
MANIFESTS_FOLDER="./../../manifests"

declare -A kubectl_operation_map
kubectl_operation_map["apply"]="kubectl apply -f"
kubectl_operation_map["delete"]="kubectl delete -f"

build_execution_command() {
  local kubectl_operation_key=$1
  local folder_path=$2

  IFS='|' read -r kubectl_operation <<< "${kubectl_operation_map[$kubectl_operation_key]}"
  execution_command="$kubectl_operation $folder_path -n restaurant"
  echo "$execution_command"
}

iterate_manifests_folders() {
  local kubectl_operation_key=$1

  for folder_path in "$MANIFESTS_FOLDER"/*/; do
    folder_name=$(basename "$folder_path")
    echo -e "${WRENCH_SYMBOL} ........................................ $folder_name"

    execution_command=$(build_execution_command "$kubectl_operation_key" "$folder_path")

    echo "$(get_timestamp) .......... $folder_name .......... $execution_command" >> "$K8S_LOG_FILE"
    eval "$execution_command"
  done
}

echo "# K8S executions started" > "$K8S_LOG_FILE"

if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <kubectl_operation_key>"
    exit 1
fi

kubectl_operation_key=$1
iterate_manifests_folders "$kubectl_operation_key"