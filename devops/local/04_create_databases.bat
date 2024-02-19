@echo off

call ./00_local_path_variables.bat @REM recover local path variables

set "MYSQL_DATABASES=db_menu_options db_payments db_products"
set "POSTGRESQL_DATABASES=db_invoices"

echo %DATE% %TIME%: Database creation execution script started > "%LOG_FILE%"

@REM Create bbq_user
%MYSQL_COMMAND% < .\sql\init-user-mysql.sql >> "%LOG_FILE%"
%POSTGRESQL_COMMAND% -f .\sql\init-user-postgresql.sql >> "%LOG_FILE%"

@REM Create databases for MySQL
echo %DATE% %TIME%: MYSQL databases >> "%LOG_FILE%"
for %%i in (%MYSQL_DATABASES%) do (
  %MYSQL_COMMAND%  -e "DROP DATABASE IF EXISTS %%i;" >> "%LOG_FILE%"
  %MYSQL_COMMAND%  -e "CREATE DATABASE IF NOT EXISTS %%i;" >> "%LOG_FILE%"
)
%MYSQL_COMMAND%  -e "SHOW DATABASES;" >> "%LOG_FILE%"

@REM Create databases for PostgreSQL
echo %DATE% %TIME%: POSTGRESQL databases >> "%LOG_FILE%"
for %%i in (%POSTGRESQL_DATABASES%) do (
  %POSTGRESQL_COMMAND%  -c "CREATE DATABASE %%i;" >> "%LOG_FILE%"
  %POSTGRESQL_COMMAND%  -c "GRANT ALL PRIVILEGES ON DATABASE %%i TO bbq_user;" >> "%LOG_FILE%"
)
%POSTGRESQL_COMMAND%  -c "SELECT datname FROM pg_database;" >> "%LOG_FILE%"