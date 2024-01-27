@echo off

set LOG_FILE=C:\dev-workspace\bbq\bbq-monorepo\devops\local\output.log
set ENVIRONMENT_PATH=C:\dev-environment
set TMP_PATH=C:\tmp

set KAFKA_PORT=9092
set KAFKA_NAME="kafka"
set KAFKA_PATH="%ENVIRONMENT_PATH%\kafka"

set ZOOKEEPER_PORT=2181
set ZOOKEEPER_NAME="zookeeper"

set REDIS_PORT=6379
set REDIS_NAME="redis"
set REDIS_PATH="%ENVIRONMENT_PATH%\redis-3.2.100"

set POSTGRESQL_PORT=5432
set POSTGRESQL_NAME="postgresql"
set POSTGRESQL_PATH="%ENVIRONMENT_PATH%\postgresql-16.1\bin"

echo %DATE% %TIME%: Servers execution script started > "%LOG_FILE%"

netstat -an | find ":%ZOOKEEPER_PORT%" >nul
if %errorlevel% equ 0 (
  goto :SkipTmpRemoval
)

netstat -an | find ":%KAFKA_PORT%" >nul
if %errorlevel% equ 0 (
  goto :SkipTmpRemoval
)

if exist %TMP_PATH% (
    rmdir /s /q %TMP_PATH%
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
    call start %POSTGRESQL_NAME% pg_ctl -D "%ENVIRONMENT_PATH%\postgresql-16.1\data" -l logging_postgresql.log start
    echo %DATE% %TIME%: %POSTGRESQL_NAME% executed >> "%LOG_FILE%"
) else (
    echo %DATE% %TIME%: %POSTGRESQL_NAME% is already started >> "%LOG_FILE%"
)