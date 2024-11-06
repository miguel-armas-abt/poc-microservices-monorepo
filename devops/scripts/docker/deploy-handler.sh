#!/bin/bash

source ./../commons.sh

DOCKER_COMPOSE_FILE="./../../docker-compose.yml";
BACKEND_PATH="./../../../application/backend"

replace_rs256_key() {
  secret_file="$BACKEND_PATH/platform/auth-adapter-v1/tmp-secret.yaml"
  value=$(yq e '.rs256' "$secret_file")
  property_name="KEYCLOAK_KEY_RS256"

  sed -i "s/$property_name=.*/$property_name=$value/" "$DOCKER_COMPOSE_FILE"

  if [ $? -eq 0 ]; then
    echo -e "${CHECK_SYMBOL} secret rs256 modified: $value"
  else
    echo -e "${RED}error modifying RS256 secret${NC}"
    return 1
  fi
}

scraping_keycloak() {
  java_home=$(./../local/local-value-searcher.sh "java_home")
  export JAVA_HOME=$java_home
  java="$java_home/bin/java"

  component_name="keycloak-scraping-v1"
  component_path="./../web-scraping/$component_name"

  command="$java -jar ./target/$component_name-0.0.1-SNAPSHOT.jar"

  echo "$(get_timestamp) .......... $command" >> "./../../$LOG_FILE"
  cd $component_path
  eval $command
  cd "./../../docker/"
}

handle_deploy() {
  local need_generate_file=$1

  if [[ $need_generate_file == "-f" ]] || [[ $need_generate_file == "-file" ]] ; then
    ./compose-file-generator.sh
  fi

  ./keycloak-processor.sh up
  sleep 7 # wait until Keycloak starts
  scraping_keycloak
  replace_rs256_key
  ./compose-file-processor.sh up
}

need_generate_file=$1
handle_deploy "$need_generate_file"