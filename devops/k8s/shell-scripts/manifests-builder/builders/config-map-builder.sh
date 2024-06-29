#!/bin/bash

source ./../../../../common-script.sh
K8S_LOG_FILE="./../../../../$K8S_LOG_FILE"

CHECK_SYMBOL="\033[0;32m\xE2\x9C\x94\033[0m"
CM_TEMPLATE="./../templates/config-map.template.yaml"
ENVIRONMENT_FILES_PATH="./../../../../environment"

build_data_from_environment_variables() {
  local app_name=$1
  local template=$2

  environment_file="$ENVIRONMENT_FILES_PATH/$app_name/$app_name.env"

  # Read the environment variables
  declare -A environment_variables_map
  while IFS='=' read -r key value || [ -n "$key" ]; do
    # Ignore comments
    if [[ $key != "#"* ]]; then
      key=$(echo "$key" | tr '[:upper:]' '[:lower:]' | tr '_' '-')
      trim_value=$(echo "$value" | sed 's/[[:space:]]*$//')
      environment_variables_map["$key"]="\"$trim_value\""
    fi
  done < "$environment_file"

  # Iterate over the environment variables and append them to the template
  for key in "${!environment_variables_map[@]}"; do
      template+="\n  $key: ${environment_variables_map[$key]}"
  done

  echo "$template"
}

build_data_from_initdb_file() {
  local app_name=$1
  local initdb_file_suffix=$2
  local template=$3

  initdb_file="$ENVIRONMENT_FILES_PATH/$app_name/init/$app_name.$initdb_file_suffix"

  data=""
  while IFS= read -r line || [[ -n "$line" ]]; do
      if [[ -z "$line" ]]; then
          continue
      fi
      formatted_line="    $line"
      data="$data$formatted_line\n"
  done < "$initdb_file"

  data=$(echo -e "$data" | sed '/^$/d') #remove extra empty line
  template="$template\n  $initdb_file_suffix: |-\n$data"
  echo "$template"
}

build_config_map() {
  local app_name=$1
  local initdb_file_suffix=$2

  template=$(<"$CM_TEMPLATE")
  template="${template//@app_name/$app_name}"

  if [[ "$initdb_file_suffix" != null* ]]; then
    template=$(build_data_from_initdb_file "$app_name" "$initdb_file_suffix" "$template")
  else
    template=$(build_data_from_environment_variables "$app_name" "$template")
  fi

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
  local initdb_file_suffix=$3

  output_dir="$manifests_path/$app_name"
  output_file="cm-$app_name.yaml"

  create_folder "$output_dir"
  template=$(build_config_map "$app_name" "$initdb_file_suffix")
  echo -e "$template" > "$output_dir/$output_file"
  echo -e "${CHECK_SYMBOL} created: $output_file"
}

if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <manifests_path> <app_name> <initdb_file_suffix>"
    exit 1
fi

manifests_path=$1
app_name=$2
initdb_file_suffix=$3

execution_command="create_file $manifests_path $app_name $initdb_file_suffix"
echo "$(get_timestamp) .......... $app_name .......... ./config-map-builder.sh ${execution_command#create_file }" >> "$K8S_LOG_FILE"
eval "$execution_command"