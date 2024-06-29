#!/bin/bash

source ./../../../../common-script.sh
K8S_LOG_FILE="./../../../../$K8S_LOG_FILE"

CHECK_SYMBOL="\033[0;32m\xE2\x9C\x94\033[0m"
PV_TEMPLATE="./../templates/persistent-volume.template.yaml"

build_persistent_volume() {
  local app_name=$1

  persistent_volume=$(<"$PV_TEMPLATE")
  persistent_volume="${persistent_volume//@app_name/$app_name}"

  host_mount_path=\"/mnt/data/\"
  persistent_volume="${persistent_volume//@host_mount_path/$host_mount_path}"

  echo "$persistent_volume"
}

create_folder() {
  local output_dir=$1

  if [ ! -d "$output_dir" ]; then
    mkdir -p "$output_dir"
  fi
}

create_file() {
  local manifests_path=$1
  local app_name=$2

  output_dir="$manifests_path/$app_name"
  output_file="pv-$app_name.yaml"

  create_folder "$output_dir"
  template=$(build_persistent_volume "$app_name")
  echo -e "$template" > "$output_dir/$output_file"
  echo -e "${CHECK_SYMBOL} created: $output_file"
}

if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <manifests_path> <app_name>"
    exit 1
fi

manifests_path=$1
app_name=$2

execution_command="create_file $manifests_path $app_name"
echo "$(get_timestamp) .......... $app_name .......... ./persistent-volume-builder.sh ${execution_command#create_file }" >> "$K8S_LOG_FILE"
eval "$execution_command"