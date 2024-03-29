#!/bin/bash

source ./../parameters/00_local_path_variables.sh
echo "Database creation execution script started" > "$LOCAL_LOG_FILE"

EXECUTION_COMMAND="$MYSQL_COMMAND < ./../../environment/databases/mysql-db/init/mysql-db.initdb.sql"
echo "$(get_timestamp) .......... mysql-db .......... $EXECUTION_COMMAND" >> "$LOCAL_LOG_FILE"
eval "$EXECUTION_COMMAND"

# export POSTGRES_USER="postgres"
# export POSTGRES_DB="postgres"
# PGPASSWORD="$PGPASSWORD" $POSTGRESQL_COMMAND -d $POSTGRES_DB -f ../environment/databases/postgres-db/init/postgre-db.initdb.sh