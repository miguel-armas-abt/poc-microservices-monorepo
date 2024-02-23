#!/bin/bash
#Example: ./build-deployment.sh product-v1-variables.env product-v1 miguelarmasabt/product:v1.0.1 8017

# Check if the correct number of arguments are provided
if [ "$#" -ne 4 ]; then
    echo "Usage: $0 <ENV_FILE> <APP_NAME> <DOCKER_IMAGE> <CONTAINER_PORT>"
    exit 1
fi

ENV_FILE=$1
APP_NAME=$2
DOCKER_IMAGE=$3
CONTAINER_PORT=$4

# Read the template file and store it in a variable
template=$(<./templates/app.statefulset.template.yaml)

template="${template//APP_NAME/$APP_NAME}"
template="${template//DOCKER_IMAGE/$DOCKER_IMAGE}"
template="${template//CONTAINER_PORT/$CONTAINER_PORT}"

# Read the environment variables file and append them to the template
env_vars=""
while IFS='=' read -r key value || [ -n "$key" ]; do
    keyLower=$(echo "$key" | tr '_' '-')
    env_vars+="\n            - name: $key\n              valueFrom:\n                configMapKeyRef:\n                  name: cm-$APP_NAME\n                  key: ${keyLower,,}"
done < "$ENV_FILE"

# Insert environment variables section into the template
template="${template/ENVIRONMENT_VARIABLES/$env_vars}"

# Write the resulting Deployment to a file
OUTPUT_DIR="./../apps/$APP_NAME"
if [ ! -d "$OUTPUT_DIR" ]; then
    mkdir -p "$OUTPUT_DIR"
fi
echo -e "$template" > "$OUTPUT_DIR/sts-$APP_NAME.yaml"

echo "Statefulset generated successfully: sts-$APP_NAME.yaml"
