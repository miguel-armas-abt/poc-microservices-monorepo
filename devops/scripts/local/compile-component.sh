#!/bin/bash

source ./../commons.sh
BACKEND_PATH="./../../../application/backend"

compile_java() {
  local component_path=$1

  if [[ -f "$component_path/pom.xml" ]]; then
    maven_home=$(./local-value-searcher.sh "maven_home")
    maven_repository=$(./local-value-searcher.sh "maven_repository")

    command="mvn clean install -Dmaven.home=\"$maven_home\" -Dmaven.repo.local=\"$maven_repository\""
    echo "$(get_timestamp) .......... $command" >> "./../../$LOG_FILE"
    cd "$component_path"
    eval $command
  fi
}

compile_component() {
  local component_name=$1
  local component_type=$2

  component_path="$BACKEND_PATH/$component_type/$component_name"

  if [[ $component_type == "business" ]] || [[ $component_type == "commons" ]] || [[ $component_type == "platform" ]] ; then
    compile_java $component_path
  fi
}

component_name=$1
component_type=$2
compile_component "$component_name" "$component_type"