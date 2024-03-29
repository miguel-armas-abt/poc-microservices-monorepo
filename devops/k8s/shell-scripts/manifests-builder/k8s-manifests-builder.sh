#!/bin/bash

#general
CONTAINERS_CSV=./../../../environment/docker/containers-to-run.csv
MANIFESTS_PATH=./../../manifests
PARAMETERS_PATH=./../../parameters
ENVIRONMENT_PATH=./../../../environment
TEMPLATES_PATH=./templates

#app
APP_VARIABLES_PATH=$ENVIRONMENT_PATH/apps
K8S_APP_PARAMETERS_CSV=$PARAMETERS_PATH/k8s-app-manifests.csv
./k8s-app-manifest-builder.sh $APP_VARIABLES_PATH $TEMPLATES_PATH $K8S_APP_PARAMETERS_CSV $CONTAINERS_CSV $MANIFESTS_PATH

#database
DB_RESOURCES_PATH=$ENVIRONMENT_PATH/databases
K8S_DB_PARAMETERS_CSV=$PARAMETERS_PATH/k8s-db-manifests.csv
./k8s-db-manifest-builder.sh $DB_RESOURCES_PATH $TEMPLATES_PATH $K8S_DB_PARAMETERS_CSV $CONTAINERS_CSV $MANIFESTS_PATH