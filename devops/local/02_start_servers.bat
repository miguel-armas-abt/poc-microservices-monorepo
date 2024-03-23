@echo off

call ./00_local_path_variables.bat @REM recover local path variables

set KAFKA_NAME="kafka"
set ZOOKEEPER_NAME="zookeeper"
set REDIS_NAME="redis"
set POSTGRESQL_NAME="postgresql"
set MYSQL_NAME="mysql"

echo %DATE% %TIME%: Servers execution script started > "%LOG_FILE%"

netstat -an | find ":%ZOOKEEPER_PORT%" >nul
if %errorlevel% equ 0 (
  goto :SkipTmpRemoval
)

netstat -an | find ":%KAFKA_PORT%" >nul
if %errorlevel% equ 0 (
  goto :SkipTmpRemoval
)

if exist %KAFKA_TMP_PATH% (
    rmdir /s /q %KAFKA_TMP_PATH%
    echo %DATE% %TIME%: tmp directory was removed >> "%LOG_FILE%"
)

:SkipTmpRemoval

netstat -an | find ":%ZOOKEEPER_PORT%" >nul
if %errorlevel% neq 0 (
    cd "%KAFKA_PATH%"
    call start %ZOOKEEPER_NAME% bin\windows\zookeeper-server-start.bat config\zookeeper.properties
    echo %DATE% %TIME%: %ZOOKEEPER_NAME% executed >> "%LOG_FILE%"
) else (
    echo %DATE% %TIME%: %ZOOKEEPER_NAME% is already started >> "%LOG_FILE%"
)

netstat -an | find ":%KAFKA_PORT%" >nul
if %errorlevel% neq 0 (
    cd "%KAFKA_PATH%"
    call start %KAFKA_NAME% bin\windows\kafka-server-start.bat config\server.properties
    echo %DATE% %TIME%: %KAFKA_NAME% executed >> "%LOG_FILE%"
) else (
    echo %DATE% %TIME%: %KAFKA_NAME% is already started >> "%LOG_FILE%"
)

netstat -an | find ":%REDIS_PORT%" >nul
if %errorlevel% neq 0 (
    cd "%REDIS_PATH%"
    call start %REDIS_NAME% redis-server
    echo %DATE% %TIME%: %REDIS_NAME% executed >> "%LOG_FILE%"
) else (
    echo %DATE% %TIME%: %REDIS_NAME% is already started >> "%LOG_FILE%"
)

netstat -an | find ":%POSTGRESQL_PORT%" >nul
if %errorlevel% neq 0 (
    cd "%POSTGRESQL_PATH%"
    call start %POSTGRESQL_NAME% pg_ctl -D "%POSTGRESQL_LOG%" -l logging_postgresql.log start
    echo %DATE% %TIME%: %POSTGRESQL_NAME% executed >> "%LOG_FILE%"
) else (
    echo %DATE% %TIME%: %POSTGRESQL_NAME% is already started >> "%LOG_FILE%"
)

netstat -an | find ":%MYSQL_PORT%" >nul
if %errorlevel% neq 0 (
    cd "%MYSQL_PATH%"
    call start %MYSQL_NAME% mysqld --console
    echo %DATE% %TIME%: %MYSQL_NAME% executed >> "%LOG_FILE%"
) else (
    echo %DATE% %TIME%: %MYSQL_NAME% is already started >> "%LOG_FILE%"
)