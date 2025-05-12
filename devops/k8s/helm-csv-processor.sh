#!/bin/bash
set -e

source ./../commons.sh
source ./../variables.env

HELM_PATH="./helm"
MANIFESTS_PATH="./k8s-manifests"

create_folder() {
  local output_dir=$1

  if [ ! -d "$output_dir" ]; then
    mkdir -p "$output_dir"
  fi
}

process_record() {
  local operation=$1
  local component_name=$2
  local component_type=$3

  if [[ $component_type == "$DATABASE_TYPE" ]]; then
    ./configmap-generator.sh "$component_name"
  fi

  values_file="$BACKEND_PATH/$component_type/$component_name/values.yaml"

  helm_template=$(yq '.helmTemplate' "$values_file")
  helm_template_path="$HELM_PATH/$helm_template"

  namespace=$(yq '.namespace' "$values_file")

  command=""

  if [[ $operation == "template" ]]; then
    create_folder "$MANIFESTS_PATH"
    command="helm template $component_name $helm_template_path -f $values_file > $MANIFESTS_PATH/$component_name.yaml"
  fi

  if [[ $operation == "install" ]]; then
    command="helm install $component_name $helm_template_path -f $values_file --namespace $namespace --create-namespace"
  fi

  if [[ $operation == "uninstall" ]]; then
    command="helm uninstall $component_name -n $namespace"
  fi

  print_log_and_eval "$command"
}

validate_operation() {
  local operation=$1

  local valid_operations=("template" "install" "uninstall")

  for valid_operation in "${valid_operations[@]}"; do
    if [[ "$operation" == "$valid_operation" ]]; then
      return 0
    fi
  done

  echo "operation must be match: ${valid_operations[*]}"
  return 1
}

iterate_csv_records() {
  local operation=$1

  validate_operation "$operation"

  firstline=true
  while IFS=',' read -r component_name component_type || [ -n "$component_name" ]; do
    # Ignore headers
    if $firstline; then
        firstline=false
        continue
    fi

    # Ignore comments
    if [[ $component_name != "#"* ]] && [[ $component_type != "$PARENT_TYPE" ]] ; then
      process_record "$operation" "$component_name" "$component_type"
    fi

  done < <(sed 's/\r//g' "$COMPONENTS_CSV")
}

operation=$1
iterate_csv_records "$operation"