#!/bin/bash

if [ "$#" -ne 4 ] && [ "$#" -ne 5 ]; then
    echo "Usage: $0 <APP_NAME> <DATA_FILE> <CONFIG_MAP_TEMPLATE> <IS_DATABASE> <SUB_PATH_INIT_DB>"
    exit 1
fi

APP_NAME=$1
DATA_FILE=$2
CONFIG_MAP_TEMPLATE=$3
IS_DATABASE=$4
SUB_PATH_INIT_DB=$5

# Replace occurrences
template=$(<./scripts/templates/$CONFIG_MAP_TEMPLATE)
template="${template//APP_NAME/$APP_NAME}"

if [ "$IS_DATABASE" = true ]; then
  data=""
  while IFS= read -r line; do
      # add tab for each line
      formatted_line="    $line"
      data="$data$formatted_line\n"
  done < "$DATA_FILE"
  data=$(echo -e "$data" | sed '/^$/d') #remove extra empty line
  template="$template\n  $SUB_PATH_INIT_DB: |-\n$data"
else
    # Read the environment variables
    declare -A env_vars
    while IFS='=' read -r key value || [ -n "$key" ]; do
        key=$(echo "$key" | tr '[:upper:]' '[:lower:]' | tr '_' '-')
        env_vars["$key"]="$value"
    done < "$DATA_FILE"

    # Iterate over the environment variables and append them to the template
    for key in "${!env_vars[@]}"; do
        template+="\n  $key: ${env_vars[$key]}"
    done
fi

OUTPUT_DIR="./apps/$APP_NAME"
OUTPUT_FILE="cm-$APP_NAME.yaml"

if [ ! -d "$OUTPUT_DIR" ]; then
    mkdir -p "$OUTPUT_DIR"
fi
echo -e "$template" > "$OUTPUT_DIR/$OUTPUT_FILE"
echo "created: $OUTPUT_FILE"