#!/bin/bash

source ./../commons.sh
BACKEND_PATH="./../../../application/backend"

run_go() {
  local component_path=$1

  if [[ -f "$component_path/go.mod" ]]; then
    go_home=$(./local-value-searcher.sh "go_home")
    go="$go_home/bin/go"

    command="$go run main.go"
    cd $component_path
    echo "$(get_timestamp) .......... $command" >> "./../../$LOG_FILE"
    start bash -c "echo -ne \"\\033]0;$component_name\\007\";$command" #set title in the emergent windows
  fi
}

run_java() {
  local component_name=$1
  local component_path=$2

  if [[ -f "$component_path/pom.xml" ]]; then
    java_home=$(./local-value-searcher.sh "java_home")
    export JAVA_HOME=$java_home
    java="$java_home/bin/java"

    command="$java -jar ./target/$component_name-0.0.1-SNAPSHOT.jar"
    cd $component_path
    echo "$(get_timestamp) .......... $command" >> "./../../$LOG_FILE"
    start bash -c "echo -ne \"\\033]0;$component_name\\007\";$command" #set title in the emergent windows
  fi
}

run_component() {
  local component_name=$1
  local component_type=$2

  component_path="$BACKEND_PATH/$component_type/$component_name"

  if [[ $component_type == "business" ]] || [[ $component_type == "platform" ]] ; then
    command=$(run_java $component_name $component_path)
    command=$(run_go $component_path)
  fi
}

component_name=$1
component_type=$2
run_component "$component_name" "$component_type"