@echo off

call ./00_local_path_variables.bat @REM recover local path variables

echo %DATE% %TIME%: Database creation execution script started > "%LOG_FILE%"

%MYSQL_COMMAND% < .\..\environment\databases\mysql-db\init\mysql-db.initdb.sql >> "%LOG_FILE%" 2>&1

@REM Pending to learn how execute PostgreSQL
@REM export POSTGRES_USER="postgres"
@REM export POSTGRES_DB="postgres"
@REM PGPASSWORD="$PGPASSWORD" $POSTGRESQL_COMMAND -d $POSTGRES_DB -f ./../environment/databases/postgres-db/init/postgre-db.initdb.sh

type "%LOG_FILE%"