#!/bin/bash

source ./../parameters/00_local_path_variables.sh
echo "$(date +"%F %T"): Database creation execution script started" > "$LOG_FILE"

$MYSQL_COMMAND < ./../../environment/databases/mysql-db/init/mysql-db.initdb.sql >> "$LOG_FILE" 2>&1

# export POSTGRES_USER="postgres"
# export POSTGRES_DB="postgres"
# PGPASSWORD="$PGPASSWORD" $POSTGRESQL_COMMAND -d $POSTGRES_DB -f ../environment/databases/postgres-db/init/postgre-db.initdb.sh

cat "$LOG_FILE"