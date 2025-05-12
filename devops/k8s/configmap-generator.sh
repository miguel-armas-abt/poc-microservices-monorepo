#!/bin/bash
set -e

source ./../commons.sh
source ./../variables.env

CONFIG_MAP_TEMPLATE="./templates/configmap-db.yaml"
DATABASE_HELM_PATH="./helm/database"

search_initdb() {
  local dbms_name=$1
  local values_file="$DATABASE_PATH/$dbms_name/values.yaml"

    if [[ ! -f $values_file ]]; then
      echo -e "${RED}file $values_file not found${NC}"
      return 1
    fi

    initdb_value=$(yq '.initdb.value' "$values_file")

    if [[ -z "$initdb_value" ]]; then
      echo "${RED}value initdb.value not found in $values_file${NC}"
      return 1
    fi

    echo "$initdb_value"
}

get_template() {
  local dbms_name=$1

  template=$(<"$CONFIG_MAP_TEMPLATE")
  template="${template//@app_name/$dbms_name}"
  echo "$template"
}

build_config_map() {
  local dbms_name=$1

  suffix=$(search_initdb "$dbms_name")
  template=$(get_template "$dbms_name")

  initdb_file="$DATABASE_PATH/$dbms_name/$dbms_name.$suffix"

  data=""
  while IFS= read -r line || [[ -n "$line" ]]; do
      if [[ -z "$line" ]]; then
          continue
      fi
      formatted_line="    $line"
      data="$data$formatted_line\n"
  done < "$initdb_file"

  data=$(echo -e "$data" | sed '/^$/d') #remove extra empty line
  template="$template\n  $suffix: |-\n$data"
  echo "$template"
}

create_file() {
  local dbms_name=$1

  template=$(build_config_map "$dbms_name")
  echo -e "$template" > "$DATABASE_HELM_PATH/templates/configmap.yaml"
  echo -e "${CHECK_SYMBOL} created configmap for: $dbms_name"
}

dbms_name=$1
create_file "$dbms_name"