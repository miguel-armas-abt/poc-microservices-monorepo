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
  local helm_template=$4
  local namespace=$5

  if [[ $component_type == "database" ]]; then
    ./configmap-db-file-generator.sh "$component_name"
  fi

  helm_template_path="$HELM_PATH/$helm_template"
  values_path="$BACKEND_PATH/$component_type/$component_name/values.yaml"
  command=""

  if [[ $operation == "template" ]]; then
    manifests_destination_path="$HELM_PATH/manifests"
    create_folder "$manifests_destination_path"
    command="helm template $component_name $helm_template_path -f $values_path > $component_name.yaml"
  fi

  if [[ $operation == "install" ]]; then
    command="helm install $component_name $helm_template_path -f $values_path --namespace $namespace --create-namespace"
  fi

  if [[ $operation == "uninstall" ]]; then
    command="helm uninstall $component_name -n $namespace"
  fi

  echo "$(get_timestamp) .......... $command" >> "./../../$K8S_LOG_FILE"
  eval "$command"
}

operation=$1
component_name=$2
component_type=$3
helm_template=$4
namespace=$5
process_record "$operation" "$component_name" "$component_type" "$helm_template" "$namespace"