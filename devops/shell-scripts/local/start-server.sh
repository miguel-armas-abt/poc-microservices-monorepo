#!/bin/bash

source ./../commons.sh

RUNTIME_PATH="./../../../application/backend/runtime"
DATABASE_PATH="./../../../application/backend/database"
LOCAL_CSV="./../../../application/local.csv"

#java
java_home=$(./local-value-searcher.sh "java_home")
export JAVA_HOME=$java_home
java="$java_home/bin/java"

#kafka
kafka_home=$(./local-value-searcher.sh "kafka_home")
kafka_tmp_folder=$(./local-value-searcher.sh "kafka_tmp")
kafka_port=$(yq '.ports.containerPort // {}' "$RUNTIME_PATH/kafka-server/values.yaml")
kafka_command=".\\\\bin\\\\windows\\\\kafka-server-start.bat config\\\\server.properties"

#zookeeper
zookeeper_port=$(yq '.ports.containerPort // {}' "$RUNTIME_PATH/zookeeper-server/values.yaml")
zookeeper_command=".\\\\bin\\\\windows\\\\zookeeper-server-start.bat config\\\\zookeeper.properties"

#redis
redis_home=$(./local-value-searcher.sh "redis_home")
redis_port=$(yq '.ports.containerPort // {}' "$RUNTIME_PATH/redis-server/values.yaml")
redis_command="./redis-server &"

#postgres-db
postgres_home=$(./local-value-searcher.sh "postgres_home")
postgres_port=$(yq '.ports.containerPort // {}' "$DATABASE_PATH/postgres-db/values.yaml")
postgres_command="./pg_ctl -D $postgres_home/data -l logging_postgresql.log start"

#mysql-db
mysql_home=$(./local-value-searcher.sh "mysql_home")
mysql_port=$(yq '.ports.containerPort // {}' "$DATABASE_PATH/mysql-db/values.yaml")
mysql_command="./mysqld --console"

#prometheus
prometheus_home=$(./local-value-searcher.sh "prometheus_home")
prometheus_port=$(yq '.ports.containerPort // {}' "$RUNTIME_PATH/prometheus/values.yaml")
prometheus_command="./prometheus.exe"

#zipkin
zipkin_home=$(./local-value-searcher.sh "zipkin_home")
zipkin_port=$(yq '.ports.containerPort // {}' "$RUNTIME_PATH/zipkin/values.yaml")
zipkin_command="$java -jar ./zipkin-server-3.3.0-exec.jar"

#grafana
grafana_home=$(./local-value-searcher.sh "grafana_home")
grafana_port=$(yq '.ports.containerPort // {}' "$RUNTIME_PATH/grafana/values.yaml")
grafana_command="./grafana-server.exe"

#loki
loki_home=$(./local-value-searcher.sh "loki_home")
loki_port=$(yq '.ports.containerPort // {}' "$RUNTIME_PATH/loki/values.yaml")
loki_command="./loki-windows-amd64.exe --config.file=loki-local-config.yaml"

#promtail
promtail_home=$(./local-value-searcher.sh "promtail_home")
promtail_port=$(yq '.ports.containerPort // {}' "$RUNTIME_PATH/promtail/values.yaml")
promtail_command="./promtail-windows-amd64.exe --config.file=promtail-local-config.yaml"

declare -A server_map
server_map["kafka"]="$kafka_port|$kafka_home|$kafka_command"
server_map["zookeeper"]="$zookeeper_port|$kafka_home|$zookeeper_command"
server_map["redis"]="$redis_port|$redis_home|$redis_command"
server_map["postgres-db"]="$postgres_port|$postgres_home/bin|$postgres_command"
server_map["mysql-db"]="$mysql_port|$mysql_home/bin|$mysql_command"
server_map["prometheus"]="$prometheus_port|$prometheus_home|$prometheus_command"
server_map["zipkin"]="$zipkin_port|$zipkin_home|$zipkin_command"
server_map["grafana"]="$grafana_port|$grafana_home/bin|$grafana_command"
server_map["loki"]="$loki_port|$loki_home|$loki_command"
server_map["promtail"]="$promtail_port|$promtail_home|$promtail_command"

is_port_in_use() {
  local server_port=$1
  
  netstat -an | grep LISTEN | grep -q ":$server_port "
  return $?
}

validate_zookeeper() {
  local server_name=$1
  local port_in_use=$2
  
  if [[ "$server_name" == "zookeeper" && -d "$kafka_tmp_folder" ]]; then
    rm -rf "$kafka_tmp_folder"
    echo "$(get_timestamp) .......... /tmp folder was removed" >> "./../../$LOG_FILE"
  fi
}

validate_port_and_execute() {
  local port_in_use=$1
  local server_path=$2
  local execution_command=$3
  local server_port=$4

  if [ "$port_in_use" == "1" ]; then
    cd "$server_path" || exit
    eval start "$execution_command"
  else
    echo "$(get_timestamp) .......... $server_name .......... port $server_port is currently in use" >> "./../../$LOG_FILE"
  fi
}

execute_server() {
  local server_name=$1

  IFS='|' read -r server_port server_path execution_command <<< "${server_map[$server_name]}"

  is_port_in_use "$server_port"
  port_in_use=$?

  validate_zookeeper "$server_name" "$port_in_use"

  echo "$(get_timestamp) .......... $server_name .......... $execution_command" >> "./../../$LOG_FILE"

  validate_port_and_execute "$port_in_use" "$server_path" "$execution_command" "$server_port"
}

list_servers() {
  echo -e "\n########## ${CYAN} Servidores disponibles ${NC}##########\n"
  local index=1
  for server in "${!server_map[@]}"; do
    echo "$index) $server"
    index=$((index + 1))
  done
}

select_server() {
  list_servers
  read -rp "Ingrese un número de servidor: " selection

  if [[ "$selection" =~ ^[0-9]+$ ]]; then
    local server_names=("${!server_map[@]}")
    if ((selection >= 1 && selection <= ${#server_names[@]})); then
      execute_server "${server_names[selection - 1]}"
    else
      echo -e "${RED}No se encontró ningún servidor.${NC}"
    fi
  else
    echo -e "${RED}Opción inválida.${NC}"
  fi
}

select_server