#!/bin/bash

source ./../commons.sh
HELM_PATH="./../../helm"
BACKEND_PATH="./../../../application/backend"

create_folder() {
  local output_dir=$1

  if [ ! -d "$output_dir" ]; then
    mkdir -p "$output_dir"
  fi
}

process_record() {
  local operation=$1
  local component_name=$2
  local component_type=$3

  if [[ $component_type == "database" ]]; then
    ./file-generator.sh "$component_name"
  fi


  values_file="$BACKEND_PATH/$component_type/$component_name/values.yaml"

  helm_template=$(awk '/helmTemplate:/ {print $2}' "$values_file" | sed 's/"//g')
  helm_template_path="$HELM_PATH/$helm_template"

  namespace=$(awk '/namespace:/ {print $2}' "$values_file" | sed 's/"//g')

  command=""

  if [[ $operation == "template" ]]; then
    manifests_destination_path="./../../k8s-manifests"
    create_folder "$manifests_destination_path"
    command="helm template $component_name $helm_template_path -f $values_file > $manifests_destination_path/$component_name.yaml"
  fi

  if [[ $operation == "install" ]]; then
    command="helm install $component_name $helm_template_path -f $values_file --namespace $namespace --create-namespace"
  fi

  if [[ $operation == "uninstall" ]]; then
    command="helm uninstall $component_name -n $namespace"
  fi

  echo "$(get_timestamp) .......... $command" >> "./../../$LOG_FILE"
  eval "$command"
}

operation=$1
component_name=$2
component_type=$3
process_record "$operation" "$component_name" "$component_type"