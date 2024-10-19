#!/bin/bash

source ./../commons.sh

DATABASE_PATH="./../../../application/backend/database"

init_mysql() {
  mysql_path="$DATABASE_PATH/mysql-db"
  values_file="$mysql_path/values.yaml"

  inidb_value=$(yq '.initdb.value' "$values_file")
  password=$(yq '.secrets.MYSQL_ROOT_PASSWORD' "$values_file")

  mysql_home=$(./local-value-searcher.sh "mysql_home")
  mysql="$mysql_home/bin/mysql"

  command="$mysql -u root -p$password < $mysql_path/mysql-db.$inidb_value"

  echo "$(get_timestamp) .......... $command" >> "./../../$LOG_FILE"
  eval "$command"
}

init_mysql