#!/bin/bash

source ./../../../../common-script.sh
K8S_LOG_FILE="./../../../../$K8S_LOG_FILE"

CHECK_SYMBOL="\033[0;32m\xE2\x9C\x94\033[0m"
DEPLOYMENT_TEMPLATE="./../templates/deployment.template.yaml"
ENVIRONMENT_FILES_PATH="./../../../../environment"
VOLUME_MOUNTS_TEMPLATE="./../templates/deployment.field.volume-mounts.template.yaml"
VOLUMES_TEMPLATE="./../templates/deployment.field.volumes.template.yaml"
ENV_FROM_TEMPLATE="./../templates/deployment.field.env-from.template.yaml"
PROBE_TEMPLATE="./../templates/deployment.field.probe.template.yaml"

build_mount_path_data() {
  local app_name=$1

  mount_path_data="/var/lib/$app_name/data"
  echo "$mount_path_data"
}

build_mount_path_initdb() {
  local initdb_file_suffix=$1

  mount_path_initdb="/docker-entrypoint-initdb.d/$initdb_file_suffix"
  echo "$mount_path_initdb"
}

build_volume_mounts() {
  local app_name=$1
  local initdb_file_suffix=$2

  volume_mounts_template=""
  if [[ "$initdb_file_suffix" != null* ]]; then
    volume_mounts_template=$(<"$VOLUME_MOUNTS_TEMPLATE")
    volume_mounts_template="${volume_mounts_template//@app_name/$app_name}"

    mount_path_data=$(build_mount_path_data "$app_name")
    volume_mounts_template="${volume_mounts_template//@mount_path_data/$mount_path_data}"

    mount_path_initdb=$(build_mount_path_initdb "$initdb_file_suffix")
    volume_mounts_template="${volume_mounts_template//@mount_path_initdb/$mount_path_initdb}"

    volume_mounts_template="${volume_mounts_template//@initdb_file_suffix/$initdb_file_suffix}"
  fi

  echo "$volume_mounts_template"
}

build_volumes() {
  local app_name=$1
  local initdb_file_suffix=$2

  volumes_template=""
  if [[ "$initdb_file_suffix" != null* ]]; then
    volumes_template=$(<"$VOLUMES_TEMPLATE")
    volumes_template="${volumes_template//@app_name/$app_name}"
  fi

  echo "$volumes_template"
}

build_env_from() {
  local app_name=$1

  secret_file="$ENVIRONMENT_FILES_PATH/$app_name/secret-$app_name.env"

  env_from_template=""
  if [[ -f "$secret_file" && -s "$secret_file" ]]; then #if file exists and isn't empty
    env_from_template=$(<"$ENV_FROM_TEMPLATE")
    env_from_template="${env_from_template//@app_name/$app_name}"
  else
    echo "$(get_timestamp) .......... $app_name  .......... [deployment-builder.sh] Secrets file not found" >> "$K8S_LOG_FILE"
  fi

  echo "$env_from_template"
}

build_env() {
  local app_name=$1
  local initdb_file_suffix=$2

  environment_file="$ENVIRONMENT_FILES_PATH/$app_name/$app_name.env"

  environment_variables=""
    if [[ -f "$environment_file" && -s "$environment_file" ]]; then #if file exists and isn't empty
      while IFS='=' read -r key value || [ -n "$key" ]; do
        # Ignore comments
        if [[ $key != "#"* ]]; then
          keyLower=$(echo "$key" | tr '_' '-')
          environment_variables+="\n            - name: $key\n              valueFrom:\n                configMapKeyRef:\n                  name: cm-$app_name\n                  key: ${keyLower,,}"
        fi
      done < "$environment_file"

      environment_variables="    env:$environment_variables"
    else
      echo "$(get_timestamp) .......... $app_name  .......... [deployment-builder.sh] Environment file not found" >> "$K8S_LOG_FILE"
    fi

  echo "$environment_variables"
}

build_health_probe() {
  local container_port=$1
  local framework=$2

  declare -A probe_path_map
  probe_path_map["spring"]="/actuator/health/readiness|/actuator/health/liveness"
  probe_path_map["quarkus"]="/q/health/ready|/q/health/live"

  probe=""
  if [[ "$framework" != null* ]]; then
    IFS='|' read -r readiness_probe_path liveness_probe_path <<< "${probe_path_map[$framework]}"

    probe=$(<"$PROBE_TEMPLATE")
    probe="${probe//@container_port/$container_port}"
    probe="${probe//@liveness_probe_path/$liveness_probe_path}"
    probe="${probe//@readiness_probe_path/$readiness_probe_path}"
  fi

  echo "$probe"
}

build_deployment() {
  local app_name=$1
  local container_image=$2
  local container_port=$3
  local replica_count=$4
  local initdb_file_suffix=$5
  local framework=$6

  template=$(<"$DEPLOYMENT_TEMPLATE")

  template="${template//@app_name/$app_name}"
  template="${template//@container_image/$container_image}"
  template="${template//@container_port/$container_port}"
  template="${template//@replica_count/$replica_count}"

  env_from=$(build_env_from "$app_name")
  template="${template//@env_from/$env_from}"

  volume_mounts=$(build_volume_mounts "$app_name" "$initdb_file_suffix")
  template="${template//@volume_mounts/$volume_mounts}"

  volumes=$(build_volumes "$app_name" "$initdb_file_suffix")
  template="${template//@volumes/$volumes}"

  environment_variables=$(build_env "$app_name" "$initdb_file_suffix")
  template="${template//@env/$environment_variables}"

  health_probe=$(build_health_probe "$container_port" "$framework")
  template="${template//@probe/$health_probe}"

  echo "$template"
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
  local container_image=$4
  local replica_count=$5
  local initdb_file_suffix=$6
  local framework=$7

  output_dir="$manifests_path/$app_name"
  output_file="dep-$app_name.yaml"

  create_folder "$output_dir"
  template=$(build_deployment "$app_name" "$container_image" "$container_port" "$replica_count" "$initdb_file_suffix" "$framework")
  echo -e "$template" > "$output_dir/$output_file"
  echo -e "${CHECK_SYMBOL} created: $output_file"
}

#validate input to app or database controller
if [ "$#" -ne 7 ]; then
    echo "Usage: $0 <manifests_path> <app_name> <container_port> <container_image> <replica_count> <initdb_file_suffix> <framework>"
    exit 1
fi

manifests_path=$1
app_name=$2
container_port=$3
container_image=$4
replica_count=$5
initdb_file_suffix=$6
framework=$7

execution_command="create_file $manifests_path $app_name $container_port $container_image $replica_count $initdb_file_suffix $framework"
echo "$(get_timestamp) .......... $app_name .......... ./deployment-builder.sh ${execution_command#create_file }" >> "$K8S_LOG_FILE"
eval "$execution_command"