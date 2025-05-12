#!/bin/bash
set -e

source ./../commons.sh
source ./../variables.env

compile_java() {
  local component_path=$1

  if [[ -f "$component_path/pom.xml" ]]; then
    command="mvn clean install -Dmaven.home=\"$MAVEN_HOME\" -Dmaven.repo.local=\"$MAVEN_REPOSITORY\""
    cd "$component_path"
    print_log_and_eval "$command"
  fi
}

compile_component() {
  local component_name=$1
  local component_type=$2

  component_path="$BACKEND_PATH/$component_type/$component_name"
  local original_dir
  original_dir="$(pwd)"

  if [[ $component_type == "$BUSINESS_TYPE" ]] || [[ $component_type == "$PARENT_TYPE" ]] || [[ $component_type == "$PLATFORM_TYPE" ]] ; then
    compile_java $component_path
  fi

  cd "$original_dir"
}

iterate_csv_records() {
  firstline=true
  while IFS=',' read -r component_name component_type || [ -n "$component_name" ]; do
    # Ignore headers
    if $firstline; then
        firstline=false
        continue
    fi

    # Ignore comments
    if [[ $component_name != "#"* ]]; then
      compile_component "$component_name" "$component_type"
    fi

  done < <(sed 's/\r//g' "$COMPONENTS_CSV")
}

iterate_csv_records