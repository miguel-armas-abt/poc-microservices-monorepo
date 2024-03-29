#!/bin/bash

source ./../../environment/common-script.sh

# specify your Java version
JAVA_PATH="C:/dev-environment/java/jdk-11.0.2/bin/java"

# specify your Maven configuration
MVN_HOME_PATH="C:/dev-environment/maven/apache-maven-3.9.1"
MVN_REPOSITORY_PATH="C:/Users/User/.m2/repository"

# specify your GO version
GO_PATH="C:/dev-environment/go/go1.21.4/bin/go"

# specify your Kafka configuration (keep tmp path at default value)
KAFKA_PATH="C:/dev-environment/kafka"
KAFKA_TMP_PATH="C:/tmp"
KAFKA_PORT="9092"
ZOOKEEPER_PORT="2181"

# specify your Redis config
REDIS_PATH="C:/dev-environment/redis/Redis-x64-3.0.504"
REDIS_PORT="6379"

# specify your PostgreSQL config
POSTGRESQL_PATH="C:/dev-environment/postgresql-16.1/bin"
POSTGRESQL_LOG="C:/dev-environment/postgresql-16.1/data"
POSTGRESQL_PORT="5432"
PGPASSWORD="qwerty"
POSTGRESQL_COMMAND="$POSTGRESQL_PATH/psql.exe -U postgres"

# specify your MySQL config
MYSQL_PATH="C:/dev-environment/mysql-8.2.0/bin"
MYSQL_PORT="3306"
MYSQL_ROOT_PASSWORD="qwerty"
MYSQL_COMMAND="$MYSQL_PATH/mysql -u root -p$MYSQL_ROOT_PASSWORD"

# specify your workspace path
BBQ_PATH="C:/dev-workspace/bbq/bbq-monorepo"
BUSINESS_PATH="$BBQ_PATH/$BS_PATH"
INFRASTRUCTURE_PATH="$BBQ_PATH/$INF_PATH"
LOCAL_LOG_FILE="$BBQ_PATH/devops/$LOCAL_LOG_FILE"