#!/bin/bash

# Check if the correct number of arguments are provided
if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <ENV_FILE> <APP_NAME>"
    exit 1
fi

ENV_FILE=$1
APP_NAME=$2

# Read the template file and store it in a variable
template=$(<./templates/app.config-map.template.yaml)

# Read the environment variables file and store it in an associative array
declare -A env_vars
while IFS='=' read -r key value || [ -n "$key" ]; do
    # Convert key to uppercase and replace underscores with dashes
    key=$(echo "$key" | tr '[:upper:]' '[:lower:]' | tr '_' '-')
    env_vars["$key"]="$value"
done < "$ENV_FILE"

# Replace the placeholder in the template with the provided APP_NAME
template="${template//APP_NAME/cm-$APP_NAME}"

# Iterate over the environment variables and append them to the template
for key in "${!env_vars[@]}"; do
    template+="\n  $key: ${env_vars[$key]}"
done

# Write the resulting ConfigMap to a file
OUTPUT_DIR="./../apps/$APP_NAME"
if [ ! -d "$OUTPUT_DIR" ]; then
    mkdir -p "$OUTPUT_DIR"
fi
echo -e "$template" > "$OUTPUT_DIR/cm-$APP_NAME.yaml"

echo "ConfigMap generated successfully: cm-$APP_NAME.yaml"
