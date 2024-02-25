#!/bin/bash

CHECK_SYMBOL="\033[0;32m\xE2\x9C\x94\033[0m"

ARGUMENTS_CSV=./scripts/docker-compose-arguments.csv
DOCKER_COMPOSE_TEMPLATE=./scripts/templates/docker-compose.template.yml
SERVICE_TEMPLATE=./scripts/templates/service.template.yml
VARIABLES_PATH=./../environment

docker_compose_template=$(<"$DOCKER_COMPOSE_TEMPLATE")

services=""
firstline=true

while IFS=',' read -r APP_NAME DOCKER_IMAGE DEPENDENCIES HOST_PORT CONTAINER_PORT VOLUMES || [ -n "$APP_NAME" ]; do
    # Ignore headers
    if $firstline; then
        firstline=false
        continue
    fi

    service_template=$(<"$SERVICE_TEMPLATE")
    service_template="${service_template//APP_NAME/$APP_NAME}"
    service_template="${service_template//DOCKER_IMAGE/$DOCKER_IMAGE}"
    service_template="${service_template//HOST_PORT/$HOST_PORT}"
    service_template="${service_template//CONTAINER_PORT/$CONTAINER_PORT}"

    #depends_on (replace DEPENDENCIES string)
    formatted_dependencies=""
    if [ "$DEPENDENCIES" != null ]; then
      # divide string by ';'
      IFS=';' read -r -a parts <<< "$DEPENDENCIES"
      first=true
      for part in "${parts[@]}"; do
        if $first; then
          formatted_dependencies+="     - $part\n"
          first=false
        else
          formatted_dependencies+="      - $part\n"
        fi
      done
      formatted_dependencies="depends_on:\n $formatted_dependencies"
    fi
    formatted_dependencies=$(echo -e "$formatted_dependencies" | sed '/^$/d') #remove extra empty line
    service_template="${service_template//DEPENDENCIES/$formatted_dependencies}"

    #environment (replace VARIABLES string)
    env_directory=""
    ENV_FILE=""
    if [ "$VOLUMES" != null ]; then
      env_directory="databases"
      ENV_FILE="$VARIABLES_PATH/$env_directory/$APP_NAME/$APP_NAME.env"
    else
      env_directory="apps"
      ENV_FILE="$VARIABLES_PATH/$env_directory/$APP_NAME.env"
    fi
    formatted_variables=$(sed 's/^/      - /' "$ENV_FILE" | sed 's/=/=/' | tr '\n' '\n')
    service_template="${service_template//VARIABLES/"environment: \n$formatted_variables"}"

    #volumes (replace VOLUMES string)
    formatted_volumes=""
    if [ "$VOLUMES" != null ]; then
      # divide string by ';'
      IFS=';' read -r -a parts <<< "$VOLUMES"
      first=true
      for part in "${parts[@]}"; do
        if $first; then
          formatted_volumes+="     - $part\n"
          first=false
        else
          formatted_volumes+="      - $part\n"
        fi
      done
      formatted_volumes="volumes:\n $formatted_volumes"
    fi
    formatted_volumes=$(echo -e "$formatted_volumes" | sed '/^$/d') #remove extra empty line
    service_template="${service_template//VOLUMES/$formatted_volumes}"

    services+="$service_template"$'\n\n'

done < <(sed 's/\r//g' "$ARGUMENTS_CSV")

docker_compose_template="${docker_compose_template//SERVICES/$services}"
OUTPUT_FILE="docker-compose.yml"
echo -e "$docker_compose_template" > "$OUTPUT_FILE"
echo -e "${CHECK_SYMBOL} created: $OUTPUT_FILE"
