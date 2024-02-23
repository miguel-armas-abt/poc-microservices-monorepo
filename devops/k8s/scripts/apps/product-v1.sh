#!/bin/bash

if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <VARIABLES_PATH>"
    exit 1
fi

VARIABLES_PATH=$1
APP_NAME=product-v1
ENV_FILE=$VARIABLES_PATH/$APP_NAME.env
PORT=8017
NODE_PORT=30017
IMAGE=miguelarmasabt/product:v1.0.1

./build-app.config-map.sh $ENV_FILE $APP_NAME
./build-app.service.sh $APP_NAME $PORT $NODE_PORT
./build-app.deployment.sh $ENV_FILE $APP_NAME $IMAGE $PORT
