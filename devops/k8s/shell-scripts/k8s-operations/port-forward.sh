#!/bin/bash

source ./../../../environment/common-script.sh
K8S_LOG_FILE=./../../../$K8S_LOG_FILE
MANIFESTS_FOLDER="./../../manifests"
CONTAINERS_CSV="./../../../environment/docker/containers-to-run.csv"

for folder in "$MANIFESTS_FOLDER"/*/; do
    app_name=$(basename "$folder")
    echo -e "${CHECK_SYMBOL} $app_name"

    #loop - read csv
    firstline=true
    while IFS=',' read -r CONTAINER_NAME DOCKER_IMAGE DEPENDENCIES HOST_PORT CONTAINER_PORT VOLUMES || [ -n "$CONTAINER_NAME" ]; do
      # Ignore headers
      if $firstline; then
          firstline=false
          continue
      fi
    
      # Ignore comments
      if [[ $CONTAINER_NAME != "#"* ]]; then
        
        # container name equals to folder name
        if [[ "$CONTAINER_NAME" == "$app_name" ]]; then
          execution_command="kubectl port-forward svc/"$app_name" "$HOST_PORT":"$CONTAINER_PORT" -n restaurant"
          echo "$(get_timestamp) .......... $app_name .......... $execution_command" >> "$K8S_LOG_FILE"
          start bash -c "echo -ne \"\\033]0;$app_name\\007\";$execution_command" #set title in the emergent windows
        fi
        
      fi
      
    done < "$CONTAINERS_CSV"
done