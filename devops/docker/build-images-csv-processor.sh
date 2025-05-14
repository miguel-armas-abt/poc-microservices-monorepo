#!/bin/bash
set -e

source ./../commons.sh
source ./../variables.env

search_dockerfile() {
  local component_path=$1

  dockerfile=$(find "$component_path" -maxdepth 1 -type f -iname 'Dockerfile*' | head -n 1)

  if [[ -z "$dockerfile" ]]; then
    echo -e "${RED}Dockerfile not found in $component_path${NC}" >&2
    return 1
  fi
  echo "$dockerfile"
}

process_record() {
  local component_name=$1
  local component_type=$2

  if [[ $component_type == "$BUSINESS_TYPE" ]] || [[ $component_type == "$PLATFORM_TYPE" ]] ; then

    component_path="$BACKEND_PATH/$component_type/$component_name"

    dockerfile=$(search_dockerfile "$component_path") || return 1

    values_file="$component_path/values.yaml"

    repository=$(yq '.image.repository' "$values_file")
    tag_version=$(yq '.image.tag' "$values_file")

    command="docker build -f $dockerfile -t $repository:$tag_version $component_path"

    print_log_and_eval "$command"
  fi
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
      process_record "$component_name" "$component_type"
    fi

  done < <(sed 's/\r//g' "$COMPONENTS_CSV")
}

iterate_csv_records