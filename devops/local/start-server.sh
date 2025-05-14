#!/bin/bash
set -e

source ./../commons.sh
source ./../variables.env

get_port_from_values() {
  local yaml_file="$1"
  yq '.ports.containerPort // {}' "$yaml_file"
}

#java
export JAVA_HOME=$JAVA_HOME
java="$JAVA_HOME/bin/java"

#kafka
kafka_port=$(get_port_from_values "$RUNTIME_PATH/kafka-server/values.yaml")
kafka_command=".\\\\bin\\\\windows\\\\kafka-server-start.bat config\\\\server.properties"

#zookeeper
zookeeper_port=$(get_port_from_values "$RUNTIME_PATH/zookeeper-server/values.yaml")
zookeeper_command=".\\\\bin\\\\windows\\\\zookeeper-server-start.bat config\\\\zookeeper.properties"

#redis
redis_port=$(get_port_from_values "$RUNTIME_PATH/redis-server/values.yaml")
redis_command="./redis-server &"

#postgres-db
postgres_port=$(get_port_from_values "$DATABASE_PATH/postgres-db/values.yaml")
postgres_command="./pg_ctl -D $POSTGRES_HOME/data -l logging_postgresql.log start"

#mysql-db
mysql_port=$(get_port_from_values "$DATABASE_PATH/mysql-db/values.yaml")
mysql_command="./mysqld --console"

#prometheus
prometheus_port=$(get_port_from_values "$RUNTIME_PATH/prometheus/values.yaml")
prometheus_command="./prometheus.exe"

#zipkin
zipkin_port=$(get_port_from_values "$RUNTIME_PATH/zipkin/values.yaml")
zipkin_command="$java -jar ./zipkin-server-3.3.0-exec.jar"

#grafana
grafana_port=$(get_port_from_values "$RUNTIME_PATH/grafana/values.yaml")
grafana_command="./grafana-server.exe"

#loki
loki_port=$(get_port_from_values "$RUNTIME_PATH/loki/values.yaml")
loki_command="./loki-windows-amd64.exe --config.file=loki-local-config.yaml"

#promtail
promtail_port=$(get_port_from_values "$RUNTIME_PATH/promtail/values.yaml")
promtail_command="./promtail-windows-amd64.exe --config.file=promtail-local-config.yaml"

declare -A server_map
server_map["kafka"]="$kafka_port|$KAFKA_HOME|$kafka_command"
server_map["zookeeper"]="$zookeeper_port|$KAFKA_HOME|$zookeeper_command"
server_map["redis"]="$redis_port|$REDIS_HOME|$redis_command"
server_map["postgres-db"]="$postgres_port|$POSTGRES_HOME/bin|$postgres_command"
server_map["mysql-db"]="$mysql_port|$MYSQL_HOME/bin|$mysql_command"
server_map["prometheus"]="$prometheus_port|$PROMETHEUS_HOME|$prometheus_command"
server_map["zipkin"]="$zipkin_port|$ZIPKIN_HOME|$zipkin_command"
server_map["grafana"]="$grafana_port|$GRAFANA_HOME/bin|$grafana_command"
server_map["loki"]="$loki_port|$LOKI_HOME|$loki_command"
server_map["promtail"]="$promtail_port|$PROMTAIL_HOME|$promtail_command"

is_port_in_use() {
  local port=$1

  if netstat -an | grep LISTEN | grep -q ":$port "; then
    echo "true"
  else
    echo "false"
  fi
}

validate_zookeeper() {
  local server_name=$1

  if [[ "$server_name" == "zookeeper" && -d "$KAFKA_TMP" ]]; then
    rm -rf "$KAFKA_TMP"
    print_log "/tmp folder was removed"
  fi
}

validate_port_and_execute() {
  local server_name=$1
  local port_in_use=$2
  local server_path=$3
  local execution_command=$4
  local server_port=$5

  local original_dir
  original_dir="$(pwd)"

  if [ "$port_in_use" == "false" ]; then
    echo -e "${GREEN}$server_name in $server_port${NC}"
    cd "$server_path" || exit
    eval start "$execution_command"
  else
    echo -e "${RED}port $server_port is currently in use${NC}" >&2
    return 1
  fi

  cd "$original_dir"
}

execute_server() {
  local server_name=$1

  IFS='|' read -r server_port server_path execution_command <<< "${server_map[$server_name]}"

  validate_zookeeper "$server_name" "$port_in_use"
  print_log "$server_name .......... $execution_command"

  local port_in_use
  port_in_use=$(is_port_in_use "$server_port")

  validate_port_and_execute "$server_name" "$port_in_use" "$server_path" "$execution_command" "$server_port"
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
  read -rp "Ingrese el número de servidor: " selection

  if [[ "$selection" =~ ^[0-9]+$ ]]; then
    local server_names=("${!server_map[@]}")
    if ((selection >= 1 && selection <= ${#server_names[@]})); then
      execute_server "${server_names[selection - 1]}"
    else
      echo -e "${RED}server not found${NC}"
    fi
  else
    echo -e "${RED}invalid option${NC}"
  fi
}

select_server