#!/bin/bash

source ./../commons.sh
BACKEND_PATH="./../../../application/backend"
JAVA_TEMPLATE="./templates/Jenkinsfile-java"
GENERIC_TEMPLATE="./templates/Jenkinsfile-generic"
SETTINGS_SCRAPING_COMPONENT="jenkins-settings-scraping-v1"

process_record() {
  local component_name=$1
  local component_type=$2

  component_path="$BACKEND_PATH/$component_type/$component_name"
  mkdir -p "$component_path"

  settings_scraping_file="./../web-scraping/$SETTINGS_SCRAPING_COMPONENT/src/main/resources/application.yaml"
  k8s_credential_id=$(yq '.configuration.k8s.credentials.id' "$settings_scraping_file")
  k8s_cloud_server_url=$(yq '.configuration.k8s.cloud.forwardedServerUrl' "$settings_scraping_file")

  template=""
  if [[ -f "$component_path/pom.xml" ]]; then
    template="$JAVA_TEMPLATE"
  else
    template="$GENERIC_TEMPLATE"
  fi

  jenkinsfile=$(<"$template")
  jenkinsfile="${jenkinsfile//@component_type/$component_type}"
  jenkinsfile="${jenkinsfile//@component_name/$component_name}"
  jenkinsfile="${jenkinsfile//@k8s_credential_id/$k8s_credential_id}"
  jenkinsfile="${jenkinsfile//@k8s_cloud_server_url/$k8s_cloud_server_url}"

  echo "$jenkinsfile" > "$component_path/Jenkinsfile"
  echo -e "${CHECK_SYMBOL} $component_name"
}

component_name=$1
component_type=$2
process_record "$component_name" "$component_type"