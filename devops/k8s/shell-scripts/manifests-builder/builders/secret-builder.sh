#!/bin/bash

source ./../../../../common-script.sh
K8S_LOG_FILE="./../../../../$K8S_LOG_FILE"

CHECK_SYMBOL="\033[0;32m\xE2\x9C\x94\033[0m"
SECRET_TEMPLATE="./../templates/secret.template.yaml"
ENVIRONMENT_FILES_PATH="./../../../../environment"

build_secrets() {
  local app_name=$1

  secrets=$(<"$SECRET_TEMPLATE")
  secrets="${secrets//@app_name/$app_name}"

  # Iterate over the environment variables and append them to the secrets
  for key in "${!secrets_map[@]}"; do
      secrets+="\n  $key: ${secrets_map[$key]}"
  done

  echo "$secrets"
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
  output_file="secret-$app_name.yaml"

  create_folder "$output_dir"
  template=$(build_secrets "$app_name")
  echo -e "$template" > "$output_dir/$output_file"
  echo -e "${CHECK_SYMBOL} created: $output_file"
}

if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <manifests_path> <app_name>"
    exit 1
fi

manifests_path=$1
app_name=$2

secret_file="$ENVIRONMENT_FILES_PATH/$app_name/secret-$app_name.env"

if [[ -f "$secret_file" && -s "$secret_file" ]]; then #if file exists and isn't empty
  # Read the secrets file and store it in an associative array
  declare -A secrets_map
  while IFS='=' read -r key value || [ -n "$key" ]; do
    # Ignore comments
    if [[ $key != "#"* ]]; then
      # Convert values to base 64
      value=$(echo -n "$value" | base64)
      secrets_map["$key"]="$value"
    fi
  done < "$secret_file"

  execution_command="create_file $manifests_path $app_name"
  echo "$(get_timestamp) .......... $app_name .......... ./secret-builder.sh ${execution_command#create_file }" >> "$K8S_LOG_FILE"
  eval "$execution_command"
else
  echo "$(get_timestamp) .......... $app_name  .......... [secret-builder.sh] Secrets file not found" >> "$K8S_LOG_FILE"
fi