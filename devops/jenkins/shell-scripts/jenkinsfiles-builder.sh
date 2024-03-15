#!/bin/bash

CHECK_SYMBOL="\033[0;32m\xE2\x9C\x94\033[0m"

JENKINSFILE_JAVA_TEMPLATE=./resources/Jenkinsfile-java-template
JENKINSFILE_COMPONENT_TEMPLATE=./resources/Jenkinsfile-component-template
JENKINSFILE_GO_TEMPLATE=./resources/Jenkinsfile-go-template
PARAMETERS_CSV=./resources/jenkinsfiles-parameters.csv
K8S_CLUSTER_TOKEN_NAME=k8s-cluster-token

echo "Enter the public url of k8s cluster:"
read K8S_CLUSTER_URL

while IFS=',' read -r APP_NAME TYPE || [ -n "$APP_NAME" ]; do
  # Ignore headers
  if $firstline; then
      firstline=false
      continue
  fi\

  jenkinsfile_template=""
  if [[ "$TYPE" == *"GO"* ]]; then
    jenkinsfile_template=$(<"$JENKINSFILE_GO_TEMPLATE")
  fi
  if [[ "$TYPE" == *"JAVA"* ]]; then
    jenkinsfile_template=$(<"$JENKINSFILE_JAVA_TEMPLATE")
  fi
  if [[ "$TYPE" == *"COMPONENT"* ]]; then
    jenkinsfile_template=$(<"$JENKINSFILE_COMPONENT_TEMPLATE")
  fi

  # Ignore comments
  if [[ $APP_NAME != "#"* ]]; then
    jenkinsfile_template="${jenkinsfile_template//APP_NAME/$APP_NAME}"
    jenkinsfile_template="${jenkinsfile_template//K8S_CLUSTER_TOKEN_NAME/$K8S_CLUSTER_TOKEN_NAME}"
    jenkinsfile_template="${jenkinsfile_template//K8S_CLUSTER_URL/$K8S_CLUSTER_URL}"
  fi

  OUTPUT_DIR="./../jenkinsfiles/$APP_NAME"
  OUTPUT_FILE="Jenkinsfile"

  if [ ! -d "$OUTPUT_DIR" ]; then
      mkdir -p "$OUTPUT_DIR"
  fi
  echo -e "$jenkinsfile_template" > "$OUTPUT_DIR/$OUTPUT_FILE"
  echo -e "${CHECK_SYMBOL} created: $APP_NAME"

done < <(sed 's/\r//g' "$PARAMETERS_CSV")