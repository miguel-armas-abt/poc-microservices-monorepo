#!/bin/bash

CHECK_SYMBOL="\033[0;32m\xE2\x9C\x94\033[0m"

if [ "$#" -ne 4 ]; then
    echo "Usage: $0 <APP_NAME> <ENV_FILE> <SECRET_TEMPLATE> <MANIFESTS_PATH>"
    exit 1
fi

APP_NAME=$1
ENV_FILE=$2
SECRET_TEMPLATE=$3
MANIFESTS_PATH=$4

template=$(<./templates/"$SECRET_TEMPLATE")

# Read the environment variables file and store it in an associative array
declare -A env_vars
while IFS='=' read -r key value || [ -n "$key" ]; do
    # Convert values to base 64
    value=$(echo -n "$value" | base64)
    env_vars["$key"]="$value"
done < "$ENV_FILE"

template="${template//APP_NAME/$APP_NAME}"

# Iterate over the environment variables and append them to the template
for key in "${!env_vars[@]}"; do
    template+="\n  $key: ${env_vars[$key]}"
done

OUTPUT_DIR="$MANIFESTS_PATH/$APP_NAME"
OUTPUT_FILE="secret-$APP_NAME.yaml"

if [ ! -d "$OUTPUT_DIR" ]; then
    mkdir -p "$OUTPUT_DIR"
fi
echo -e "$template" > "$OUTPUT_DIR/$OUTPUT_FILE"
echo -e "${CHECK_SYMBOL} created: $OUTPUT_FILE"