#!/bin/bash

source ./../commons.sh

scraping_keycloak() {
  java_home=$(./../local/local-value-searcher.sh "java_home")
  export JAVA_HOME=$java_home
  java="$java_home/bin/java"

  component_name="keycloak-scraping-v1"
  component_path="./../web-scraping/$component_name"

  command="$java -jar ./target/$component_name-0.0.1-SNAPSHOT.jar"
  cd $component_path
  echo "$(get_timestamp) .......... $command" >> "./../../$LOG_FILE"
  eval $command
}

handle_deploy() {
  ./compose-file-processor.sh template
  ./keycloak-processor.sh up
  scraping_keycloak
}

handle_deploy