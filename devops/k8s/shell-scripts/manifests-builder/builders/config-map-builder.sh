#!/bin/bash

CHECK_SYMBOL="\033[0;32m\xE2\x9C\x94\033[0m"

if [ "$#" -ne 5 ] && [ "$#" -ne 6 ]; then
    echo "Usage: $0 <APP_NAME> <DATA_FILE> <CONFIG_MAP_TEMPLATE> <IS_DATABASE> <MANIFESTS_PATH> <SUB_PATH_INIT_DB>"
    exit 1
fi

APP_NAME=$1
DATA_FILE=$2
CONFIG_MAP_TEMPLATE=$3
IS_DATABASE=$4
MANIFESTS_PATH=$5
SUB_PATH_INIT_DB=$6

# Replace occurrences
template=$(<./templates/$CONFIG_MAP_TEMPLATE)
template="${template//APP_NAME/$APP_NAME}"

if [ "$IS_DATABASE" = true ]; then
  data=""
  while IFS= read -r line || [[ -n "$line" ]]; do
      if [[ -z "$line" ]]; then
          continue
      fi
      formatted_line="    $line"
      data="$data$formatted_line\n"
  done < "$DATA_FILE"
  data=$(echo -e "$data" | sed '/^$/d') #remove extra empty line
  template="$template\n  $SUB_PATH_INIT_DB: |-\n$data"

else
    # Read the environment variables
    declare -A env_vars
    while IFS='=' read -r key value || [ -n "$key" ]; do
        if [[ $key == \#* ]]; then
            continue
        fi
        key=$(echo "$key" | tr '[:upper:]' '[:lower:]' | tr '_' '-')
        env_vars["$key"]="$value"
    done < "$DATA_FILE"

    # Iterate over the environment variables and append them to the template
    for key in "${!env_vars[@]}"; do
        template+="\n  $key: ${env_vars[$key]}"
    done
fi

OUTPUT_DIR="$MANIFESTS_PATH/$APP_NAME"
OUTPUT_FILE="cm-$APP_NAME.yaml"

if [ ! -d "$OUTPUT_DIR" ]; then
    mkdir -p "$OUTPUT_DIR"
fi
echo -e "$template" > "$OUTPUT_DIR/$OUTPUT_FILE"
echo -e "${CHECK_SYMBOL} created: $OUTPUT_FILE"