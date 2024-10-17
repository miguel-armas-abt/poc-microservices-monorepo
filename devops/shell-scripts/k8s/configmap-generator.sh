#!/bin/bash

source ./../commons.sh

CONFIG_MAP_TEMPLATE="./templates/configmap-db.yaml"
DATABASES_PATH="./../../../application/backend/database"
HELM_DATABASES_PATH="./helm/database"

search_initdb() {
  local dbms_name=$1
  local values_file="$DATABASES_PATH/$dbms_name/values.yaml"

    if [[ ! -f $values_file ]]; then
      echo "El archivo $values_file no existe."
      return 1
    fi

    initdb_value=$(grep 'initdb:' -A 1 "$values_file" | grep 'value:' | sed 's/.*value: //')

    if [[ -z "$initdb_value" ]]; then
      echo "No se encontró initdb.value en $values_file."
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

  initdb_file="$DATABASES_PATH/$dbms_name/$dbms_name.$suffix"

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
  echo -e "$template" > "$HELM_DATABASES_PATH/templates/configmap.yaml"
  echo -e "${CHECK_SYMBOL} created configmap for: $dbms_name"
}

dbms_name=$1
create_file "$dbms_name"