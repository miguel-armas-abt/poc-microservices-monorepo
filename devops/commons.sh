#!/bin/bash

source ./../variables.env

#colors
RED='\033[0;31m'
NC='\033[0m' #no color
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'

#symbols
CHECK_SYMBOL="\033[0;32m\xE2\x9C\x94\033[0m"
WRENCH_SYMBOL="\xE2\x9C\xA8"

#timestamps
function get_timestamp {
    date +"%l:%M:%S:%3N%p"
}

function print_log_and_eval {
  local command=$1
  echo "$(get_timestamp) .......... $command" >> "$LOG_FILE"
  eval "$command"
}

function print_log {
  local command=$1
  echo "$(get_timestamp) .......... $command" >> "$LOG_FILE"
}

function arrow_loader {
  local message="$1"
  local arrows=("←" "↖" "↑" "↗" "→" "↘" "↓" "↙")

  if [[ -z "$ARROW_INDEX" ]]; then
    ARROW_INDEX=0
  fi

  printf "\r${CYAN}%s${NC} %s" "${arrows[ARROW_INDEX]}" "$message"
  ARROW_INDEX=$(( (ARROW_INDEX + 1) % ${#arrows[@]} ))
}

function is_container_running() {
  local container=$1
  local status

  status=$(docker inspect -f '{{.State.Running}}' "$container" 2>/dev/null) || {
    echo "false"
    return
  }

  print_log "$container is running: $status"

  if [[ "$status" == "true" ]]; then
    echo "true"
  else
    echo "false"
  fi
}

function run_target_jar() {
  local component_name=$1
  local component_path=$2

  local original_dir
  original_dir="$(pwd)"

  if [[ -f "$component_path/pom.xml" ]]; then
    export JAVA_HOME=$JAVA_HOME
    java="$JAVA_HOME/bin/java"

    local jar_path
    jar_path=$(ls "$component_path/target"/*.jar 2>/dev/null | head -n 1)

    if [[ -z "$jar_path" ]]; then
      echo -e "${RED}.jar not found for $component_path${NC}" >&2
      return 1
    fi

    local jar_file
    jar_file=$(basename "$jar_path")

    command="$java -jar ./target/$jar_file"
    print_log "$command"

    cd $component_path
    start bash -c "echo -ne \"\\033]0;$component_name\\007\";$command" #set title in the emergent windows
  fi

  cd "$original_dir"
}

function run_main_go() {
  local component_name=$1
  local component_path=$2

  local original_dir
  original_dir="$(pwd)"

  if [[ -f "$component_path/go.mod" ]]; then
    go="$GO_HOME/bin/go"

    command="$go run cmd/main.go"
    print_log "$command"

    cd $component_path
    start bash -c "echo -ne \"\\033]0;$component_name\\007\";$command" #set title in the emergent windows
  fi

  cd "$original_dir"
}