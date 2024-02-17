@echo off
@REM specify your Java version
set JAVA_PATH=C:\dev-environment\java\jdk-11.0.2\bin\java

@REM specify your Maven configuration
set MVN_HOME_PATH=C:\dev-environment\maven\apache-maven-3.9.1
set MVN_REPOSITORY_PATH=C:\Users\User\.m2\repository

@REM specify your Go version
set GO_PATH=C:\dev-environment\go\go1.21.4\bin\go

@REM specify your Kafka configuration (keep tmp path at default value)
set KAFKA_PATH=C:\dev-environment\kafka
set KAFKA_TMP_PATH=C:\tmp
set KAFKA_PORT=9092
set ZOOKEEPER_PORT=2181

@REM specify your Redis config
set REDIS_PATH=C:\dev-environment\redis-3.2.100
set REDIS_PORT=6379

@REM specify your PostgreSQL config
set POSTGRESQL_PATH=C:\dev-environment\postgresql-16.1\bin
set POSTGRESQL_LOG=C:\dev-environment\postgresql-16.1\data
set POSTGRESQL_PORT=5432

@REM specify your workspace path
set BBQ_PATH=C:\dev-workspace\bbq\bbq-monorepo
set BUSINESS_PATH=%BBQ_PATH%\application\backend\business
set INFRASTRUCTURE_PATH=%BBQ_PATH%\application\backend\infrastructure
set LOG_FILE=%BBQ_PATH%\devops\local\output.log