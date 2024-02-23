#!/bin/bash

if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <SECRET_VARIABLES_PATH> <INIT_DB_PATH>"
    exit 1
fi

SECRET_VARIABLES_PATH=$1
INIT_DB_PATH=$2

#deployment
APP_NAME=mysql-db
CONTAINER_NAME=mysql
DOCKER_IMAGE=mysql:latest
CONTAINER_PORT=3306
MOUNT_PATH_DATA=/var/lib/$APP_NAME/data
MOUNT_PATH_INIT_DB=/docker-entrypoint-initdb.d/initdb.sql
SUB_PATH_INIT_DB=initdb.sql

#persistent-volume
HOST_MOUNT_PATH=\"/mnt/data/\"

#service
CLUSTER_IP=10.96.1.3
PORT=3306
NODE_PORT=30006

#secret
ENV_FILE=$SECRET_VARIABLES_PATH/$APP_NAME.env

#config-map
INIT_DB_FILE=$INIT_DB_PATH/$APP_NAME.init-db.yml

./build-db.deployment.sh $APP_NAME $CONTAINER_NAME $DOCKER_IMAGE $CONTAINER_PORT $MOUNT_PATH_DATA $MOUNT_PATH_INIT_DB $SUB_PATH_INIT_DB
./build-db.persistent-volume.sh $APP_NAME $HOST_MOUNT_PATH
./build-db.persistent-volume-claim.sh $APP_NAME
./build-db.service.sh $APP_NAME $CLUSTER_IP $PORT $NODE_PORT
./build-db.secret.sh $ENV_FILE $APP_NAME
./build-db.config-map.sh $APP_NAME $INIT_DB_FILE