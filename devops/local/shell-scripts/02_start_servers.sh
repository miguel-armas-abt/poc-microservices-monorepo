#!/bin/bash

source ./../parameters/00_local_path_variables.sh
SERVERS_CSV=./../parameters/02_servers-to-start.csv

echo "# Servers execution script started" > "$LOCAL_LOG_FILE"

firstline=true
while IFS=',' read -r SERVER_NAME || [ -n "$SERVER_NAME" ]; do

  # Ignore headers
  if $firstline; then
    firstline=false
    continue
  fi

  # Ignore comments
  if [[ $SERVER_NAME != "#"* ]]; then

    SERVER_PORT=""
    SERVER_PATH=""
    EXECUTION_COMMAND=""

    if [[ "$SERVER_NAME" == "kafka" ]]; then
      SERVER_PORT=$KAFKA_PORT
      SERVER_PATH=$KAFKA_PATH
      EXECUTION_COMMAND=".\\\\bin\\\\windows\\\\kafka-server-start.bat config\\\\server.properties"
    fi

    if [[ "$SERVER_NAME" == "zookeeper" ]]; then
      SERVER_PORT=$ZOOKEEPER_PORT
      SERVER_PATH=$KAFKA_PATH
      EXECUTION_COMMAND=".\\\\bin\\\\windows\\\\zookeeper-server-start.bat config\\\\zookeeper.properties"
    fi

    if [[ "$SERVER_NAME" == "redis" ]]; then
      SERVER_PORT=$REDIS_PORT
      SERVER_PATH=$REDIS_PATH
      EXECUTION_COMMAND="./redis-server &"
    fi

    if [[ "$SERVER_NAME" == "postgres-db" ]]; then
      SERVER_PORT=$POSTGRESQL_PORT
      SERVER_PATH=$POSTGRESQL_PATH
      EXECUTION_COMMAND="./pg_ctl -D "$POSTGRESQL_LOG" -l logging_postgresql.log start"
    fi

    if [[ "$SERVER_NAME" == "mysql-db" ]]; then
      SERVER_PORT=$MYSQL_PORT
      SERVER_PATH=$MYSQL_PATH
      EXECUTION_COMMAND="./mysqld --console"
    fi

    #verify port
    netstat -an | grep LISTEN | grep -q ":$SERVER_PORT "
    if [ $? -eq 0 ]; then
        port_in_use=true
    else
        port_in_use=false
    fi

    #kafka server
    if [[ "$SERVER_NAME" == "kafka" ]]; then
      if [[ "$port_in_use" == "false" &&  -d "$KAFKA_TMP_PATH" ]]; then
        rm -rf "$KAFKA_TMP_PATH"
        echo "$(get_timestamp) .......... /tmp folder was removed" >> "$LOCAL_LOG_FILE"
      fi
    fi

    echo "$(get_timestamp) .......... $SERVER_NAME .......... $EXECUTION_COMMAND" >> "$LOCAL_LOG_FILE"

    if [ "$port_in_use" == "false" ]; then
      cd "$SERVER_PATH" || exit
      eval start "$EXECUTION_COMMAND"
    else
      echo "$(get_timestamp) .......... $SERVER_NAME .......... port $SERVER_PORT is currently in use" >> "$LOCAL_LOG_FILE"
    fi

  fi
done < <(sed 's/\r//g' "$SERVERS_CSV")