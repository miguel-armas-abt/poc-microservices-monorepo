#!/bin/bash

source ./../../../../common-script.sh
K8S_LOG_FILE="./../../../../$K8S_LOG_FILE"

CHECK_SYMBOL="\033[0;32m\xE2\x9C\x94\033[0m"
SERVICE_TEMPLATE="./../templates/service.template.yaml"

build_cluster_ip() {
  local cluster_ip=$1

  if [[ "$cluster_ip" != null* ]]; then
    cluster_ip="clusterIP: $cluster_ip\n"
  else
    cluster_ip=""
  fi

  echo "$cluster_ip"
}

build_template() {
  local app_name=$1
  local cluster_ip=$2
  local node_port=$3
  local container_port=$4

  service=$(<"$SERVICE_TEMPLATE")
  service="${service//@app_name/$app_name}"

  cluster_ip=$(build_cluster_ip "$cluster_ip")
  service="${service//@cluster_ip/$cluster_ip}"

  service="${service//@node_port/$node_port}"
  service="${service//@container_port/$container_port}"
  echo "$service"
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
  local container_port=$3
  local node_port=$4
  local cluster_ip=$5

  output_dir="$manifests_path/$app_name"
  output_file="svc-$app_name.yaml"

  create_folder "$output_dir"
  template=$(build_template "$app_name" "$cluster_ip" "$node_port" "$container_port")
  echo -e "$template" > "$output_dir/$output_file"
  echo -e "${CHECK_SYMBOL} created: $output_file"
}

if [ "$#" -ne 5 ]; then
    echo "Usage: $0 <manifests_path> <app_name> <container_port> <node_port> <cluster_ip>"
    exit 1
fi

manifests_path=$1
app_name=$2
container_port=$3
node_port=$4
cluster_ip=$5

execution_command="create_file $manifests_path $app_name $container_port $node_port $cluster_ip"
echo "$(get_timestamp) .......... $app_name .......... ./service-builder.sh ${execution_command#create_file }" >> "$K8S_LOG_FILE"
eval "$execution_command"