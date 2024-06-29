#!/bin/bash

source ./../../../common-script.sh
K8S_LOG_FILE=./../../../$K8S_LOG_FILE
MANIFESTS_FOLDER="./../../manifests"
CONTAINERS_CSV="./../../../docker/containers-to-run.csv"

execute() {
  local container_name=$1
  local folder_name=$2
  
  # container name equals to folder name
  if [[ "$container_name" == "$folder_name" ]]; then
    execution_command="kubectl port-forward svc/"$folder_name" "$host_port":"$container_port" -n restaurant"
    echo "$(get_timestamp) .......... $folder_name .......... $execution_command" >> "$K8S_LOG_FILE"
    start bash -c "echo -ne \"\\033]0;$folder_name\\007\";$execution_command" #set title in the emergent windows
  fi
}

iterate_csv_container() {
  local folder_name=$1

  firstline=true
  while IFS=',' read -r container_name docker_image dependencies host_port container_port volumes || [ -n "$container_name" ]; do
    # Ignore headers
    if $firstline; then
        firstline=false
        continue
    fi
    
    # Ignore comments
    if [[ $container_name != "#"* ]]; then
      execute "$container_name" "$folder_name"
    fi
  done < "$CONTAINERS_CSV"
}

iterate_manifests_folders() {
  for folder_path in "$MANIFESTS_FOLDER"/*/; do
    folder_name=$(basename "$folder_path")
    echo -e "${CHECK_SYMBOL} $folder_name"

    iterate_csv_container "$folder_name"
  done
}

iterate_manifests_folders