@echo off
set BASE_DIR=C:\dev-environment
set TMP_DIR=C:\tmp
set ZOOKEEPER_PORT=2181
set KAFKA_PORT=9092
set REDIS_PORT=6379
set POSTGRE_SQL_PORT=5432

if exist %TMP_DIR% (
    rmdir /s /q %TMP_DIR%
	echo.
	timeout /t 5 /nobreak >nul
)

:: validate zookeper
echo Checking Zookeeper port %ZOOKEEPER_PORT%...
netstat -an | find ":%ZOOKEEPER_PORT%" >nul
if %errorlevel% neq 0 (
    cd "%BASE_DIR%\kafka"
    start bin\windows\zookeeper-server-start.bat config\zookeeper.properties
    echo Zookeeper started.
    echo.
    timeout /t 5 /nobreak >nul
) else (
    echo Zookeeper server is already started.
    echo.
)

:: validate kafka
echo Checking Kafka port %KAFKA_PORT%...
netstat -an | find ":%KAFKA_PORT%" >nul
if %errorlevel% neq 0 (
    cd "%BASE_DIR%\kafka"
    start bin\windows\kafka-server-start.bat config\server.properties
    echo Kafka started.
    echo.
    timeout /t 5 /nobreak >nul
) else (
    echo Kafka server is already started.
    echo.
)

:: validate redis
echo Checking Redis port %REDIS_PORT%...
netstat -an | find ":%REDIS_PORT%" >nul
if %errorlevel% neq 0 (
    cd "%BASE_DIR%\redis-3.2.100"
    start redis-server
    echo Redis started.
    echo.
    timeout /t 5 /nobreak >nul
) else (
    echo Redis server is already started.
    echo.
)

:: validate postgresql
echo Checking PostgreSQL port %POSTGRE_SQL_PORT%...
netstat -an | find ":%POSTGRE_SQL_PORT%" >nul
if %errorlevel% neq 0 (
    cd "%BASE_DIR%\postgresql-16.1\bin"
    start pg_ctl -D "%BASE_DIR%\postgresql-16.1\data" -l archivo_de_registro start
    echo PostgreSQL started.
    echo.
    timeout /t 5 /nobreak >nul
) else (
    echo PostgreSQL server is already started.
    echo.
)
