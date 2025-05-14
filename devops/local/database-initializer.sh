#!/bin/bash
set -e

source ./../commons.sh

init_mysql() {
  mysql_path="$DATABASE_PATH/mysql-db"
  values_file="$mysql_path/values.yaml"

  inidb_value=$(yq '.initdb.value' "$values_file")
  password=$(yq '.secrets.MYSQL_ROOT_PASSWORD' "$values_file")

  mysql="$MYSQL_HOME/bin/mysql"
  command="$mysql -u root -p$password < $mysql_path/mysql-db.$inidb_value"
  print_log_and_eval "$command"
}

init_mysql