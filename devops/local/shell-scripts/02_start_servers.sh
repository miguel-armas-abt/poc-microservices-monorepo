#!/bin/bash

source ./../parameters/00_local_path_variables.sh
SERVERS_CSV=./../parameters/02_servers-to-start.csv

declare -A server_map
server_map["kafka"]="$KAFKA_PORT|$KAFKA_PATH|.\\\\bin\\\\windows\\\\kafka-server-start.bat config\\\\server.properties"
server_map["zookeeper"]="$ZOOKEEPER_PORT|$KAFKA_PATH|.\\\\bin\\\\windows\\\\zookeeper-server-start.bat config\\\\zookeeper.properties"
server_map["redis"]="$REDIS_PORT|$REDIS_PATH|./redis-server &"
server_map["postgres-db"]="$POSTGRESQL_PORT|$POSTGRESQL_PATH|./pg_ctl -D $POSTGRESQL_LOG -l logging_postgresql.log start"
server_map["mysql-db"]="$MYSQL_PORT|$MYSQL_PATH|./mysqld --console"
server_map["prometheus"]="$PROMETHEUS_PORT|$PROMETHEUS_PATH|./prometheus.exe"
server_map["zipkin"]="$ZIPKIN_PORT|$ZIPKIN_PATH|$JAVA_COMMAND -jar ./zipkin-server-3.3.0-exec.jar"
server_map["grafana"]="$GRAFANA_PORT|$GRAFANA_PATH|./grafana-server.exe"
server_map["loki"]="$LOKI_PORT|$LOKI_PATH|./loki-windows-amd64.exe --config.file=loki-local-config.yaml"
server_map["promtail"]="$PROMTAIL_PORT|$PROMTAIL_PATH|./promtail-windows-amd64.exe --config.file=promtail-local-config.yaml"

is_port_in_use() {
  local server_port=$1
  
  netstat -an | grep LISTEN | grep -q ":$server_port "
  return $?
}

validate_kafka() {
  local server_name=$1
  local port_in_use=$2
  
  if [[ "$server_name" == "kafka" && "$port_in_use" == "1" && -d "$KAFKA_TMP_PATH" ]]; then
    rm -rf "$KAFKA_TMP_PATH"
    echo "$(get_timestamp) .......... /tmp folder was removed" >> "$LOCAL_LOG_FILE"
  fi
}

execute_server() {
  local port_in_use=$1
  local server_path=$2
  local execution_command=$3
  local server_port=$4

  if [ "$port_in_use" == "1" ]; then
    cd "$server_path" || exit
    eval start "$execution_command"
  else
    echo "$(get_timestamp) .......... $server_name .......... port $server_port is currently in use" >> "$LOCAL_LOG_FILE"
  fi
}

process_csv_record() {
  local server_name=$1

  IFS='|' read -r server_port server_path execution_command <<< "${server_map[$server_name]}"

  is_port_in_use "$server_port"
  port_in_use=$?

  validate_kafka "$server_name" "$port_in_use"

  echo "$(get_timestamp) .......... $server_name .......... $execution_command" >> "$LOCAL_LOG_FILE"

  execute_server "$port_in_use" "$server_path" "$execution_command" "$server_port"
}

iterate_csv_records() {
  firstline=true
  while IFS=',' read -r server_name || [ -n "$server_name" ]; do
    # Ignore headers
    if $firstline; then
      firstline=false
      continue
    fi

    # Ignore comments
    if [[ $server_name != "#"* ]]; then
      process_csv_record "$server_name"
    fi

  done < <(sed 's/\r//g' "$SERVERS_CSV")
}

echo "# Servers execution script started" > "$LOCAL_LOG_FILE"
iterate_csv_records