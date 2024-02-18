@echo off

call ./00_local_path_variables.bat @REM recover local path variables

echo %DATE% %TIME%: Insert data execution script started > "%LOG_FILE%"

@REM Insert db_products
%MYSQL_COMMAND% < .\sql\insert_db_products.sql >> "%LOG_FILE%"