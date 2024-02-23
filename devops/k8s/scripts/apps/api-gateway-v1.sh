#!/bin/bash

if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <VARIABLES_PATH>"
    exit 1
fi

VARIABLES_PATH=$1
APP_NAME=api-gateway-v1
ENV_FILE=$VARIABLES_PATH/$APP_NAME.env
PORT=8010
NODE_PORT=30010
IMAGE=miguelarmasabt/api-gateway:v1.0.1

./build-app.config-map.sh $ENV_FILE $APP_NAME
./build-app.service.sh $APP_NAME $PORT $NODE_PORT
./build-app.deployment.sh $ENV_FILE $APP_NAME $IMAGE $PORT
