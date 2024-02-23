#!/bin/bash

# Check if the correct number of arguments are provided
if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <APP_NAME> <INIT_DB_FILE>"
    exit 1
fi

APP_NAME=$1
INIT_DB_FILE=$2

# Read the template file and store it in a variable
template=$(<./templates/db.config-map.template.yaml)

# Replace the placeholder in the template with the provided APP_NAME
template="${template//APP_NAME/$APP_NAME}"

data=$(<"$INIT_DB_FILE")
template="$template\n$data"

# Write the resulting ConfigMap to a file
OUTPUT_DIR="./../databases/$APP_NAME"
if [ ! -d "$OUTPUT_DIR" ]; then
    mkdir -p "$OUTPUT_DIR"
fi
echo -e "$template" > "$OUTPUT_DIR/cm-$APP_NAME.yaml"

echo "Config Map generated successfully: cm-$APP_NAME.yaml"