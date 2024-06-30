#!/bin/bash

source ./../../../../common-script.sh
K8S_LOG_FILE="./../../../../$K8S_LOG_FILE"

K8S_PARAMETERS_CSV="./../../../parameters/k8s-manifests.csv"
CONTAINERS_CSV="./../../../../docker/containers-to-run.csv"
MANIFESTS_PATH="./../../../manifests"

build_if_is_database() {
  local app_name=$1
  local k8s_initdb_file_suffix=$2

  if [[ "$k8s_initdb_file_suffix" != null* ]]; then
    ./persistent-volume-builder.sh "$MANIFESTS_PATH" "$app_name"
    ./persistent-volume-claim-builder.sh "$MANIFESTS_PATH" "$app_name"
  fi
}

match() {
  local container_name=$1
  local container_image=$2
  local container_dependencies=$3
  local container_host_port=$4
  local container_port=$5
  local container_volumes=$6

  local k8s_app_name=$7
  local k8s_node_port=$8
  local k8s_replica_count=$9
  local k8s_initdb_file_suffix=${10}
  local k8s_cluster_ip=${11}
  local framework=${12}

  # container name equals to k8s app
  if [[ $k8s_app_name == "$container_name" ]]; then

    ./config-map-builder.sh "$MANIFESTS_PATH" "$k8s_app_name" "$k8s_initdb_file_suffix"
    ./deployment-builder.sh "$MANIFESTS_PATH" "$k8s_app_name" "$container_port" "$container_image" "$k8s_replica_count" "$k8s_initdb_file_suffix" "$framework"
    ./service-builder.sh "$MANIFESTS_PATH" "$k8s_app_name" "$container_port" "$k8s_node_port" "$k8s_cluster_ip"
    ./secret-builder.sh "$MANIFESTS_PATH" "$k8s_app_name"

    build_if_is_database "$k8s_app_name" "$k8s_initdb_file_suffix"
  fi
}

iterate_csv_k8s_params() {
  local container_name=$1
  local container_image=$2
  local container_dependencies=$3
  local container_host_port=$4
  local container_port=$5
  local container_volumes=$6
  
  firstline=true
  while IFS=',' read -r k8s_app_name k8s_node_port k8s_replica_count k8s_initdb_file_suffix k8s_cluster_ip framework || [ -n "$k8s_app_name" ]; do
    # Ignore headers
    if $firstline; then
        firstline=false
        continue
    fi
    
    # Ignore comments
    if [[ $k8s_app_name != "#"* ]]; then
      match "$container_name" "$container_image" "$container_dependencies" "$container_host_port" "$container_port" "$container_volumes" "$k8s_app_name" "$k8s_node_port" "$k8s_replica_count" "$k8s_initdb_file_suffix" "$k8s_cluster_ip" "$framework"
    fi
    done < "$K8S_PARAMETERS_CSV"
}

iterate_csv_containers() {
  containers_firstline=true
  
  while IFS=',' read -r container_name container_image container_dependencies container_host_port container_port container_volumes || [ -n "$container_name" ]; do

    # Ignore headers
    if $containers_firstline; then
        containers_firstline=false
        continue
    fi
    
    # Ignore comments
    if [[ $container_name != "#"* ]]; then
      iterate_csv_k8s_params "$container_name" "$container_image" "$container_dependencies" "$container_host_port" "$container_port" "$container_volumes"
    fi
  done < "$CONTAINERS_CSV"
}

echo "# Manifests builder script started" > "$K8S_LOG_FILE"
iterate_csv_containers