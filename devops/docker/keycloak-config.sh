#!/bin/bash

source ./../commons.sh
source ./../variables.env

DOCKER_COMPOSE_FILE="./docker-compose.yml";

replace_rs256_key() {
  secret_file="$BACKEND_PATH/$PLATFORM_TYPE/$AUTH_ADAPTER_NAME/tmp-secret.yaml"
  value=$(yq e '.rs256' "$secret_file")
  property_name="KEYCLOAK_KEY_RS256"

  sed -i "s/$property_name=.*/$property_name=$value/" "$DOCKER_COMPOSE_FILE"

  if [ $? -eq 0 ]; then
    echo -e "${CHECK_SYMBOL} secret rs256 modified: $value"
  else
    echo -e "${RED}error modifying rs256 secret${NC}" >&2
    return 1
  fi
}

scraping_keycloak() {
  component_path="./../$KEYCLOAK_SCRAPING"
  run_target_jar "$KEYCLOAK_SCRAPING" "$component_path"
}

handle() {
  ./docker-commands.sh "wait-container" "$KEYCLOAK_CONTAINER_NAME"
  scraping_keycloak
  replace_rs256_key
  sleep "$KEYCLOAK_DELAY_AFTER_CONFIG"
  ./docker-commands.sh "recreate-container" "$AUTH_ADAPTER_NAME"
}

handle