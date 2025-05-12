#!/bin/bash
set -e

source ./../commons.sh
source ./../variables.env

run_component() {
  local component_name=$1
  local component_type=$2

  component_path="$BACKEND_PATH/$component_type/$component_name"

  if [[ $component_type == "$BUSINESS_TYPE" ]] || [[ $component_type == "$PLATFORM_TYPE" ]] ; then
    run_target_jar "$component_name" "$component_path"
    run_main_go "$component_name" "$component_path"
  fi
}

component_name=$1
component_type=$2
run_component "$component_name" "$component_type"