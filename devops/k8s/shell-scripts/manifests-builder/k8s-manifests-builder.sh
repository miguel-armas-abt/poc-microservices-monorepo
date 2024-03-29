#!/bin/bash

#general
SECRET_TEMPLATE=secret.template.yaml
PV_TEMPLATE=persistent-volume.template.yaml
PVC_TEMPLATE=persistent-volume-claim.template.yaml
CONTAINERS_CSV=./../../../environment/docker/containers-to-run.csv
MANIFESTS_PATH=./../../manifests
PARAMETERS_PATH=./../../parameters
ENVIRONMENT_PATH=./../../../environment
TEMPLATES_PATH=./templates

#app
APP_VARIABLES_PATH=$ENVIRONMENT_PATH/apps
APP_SERVICE_TEMPLATE=app.service.template.yaml
APP_CONTROLLER_TEMPLATE=app.controller.template.yaml
K8S_APP_PARAMETERS_CSV=$PARAMETERS_PATH/k8s-app-manifests.csv
./k8s-app-manifest-builder.sh $APP_VARIABLES_PATH $TEMPLATES_PATH $APP_SERVICE_TEMPLATE $APP_CONTROLLER_TEMPLATE $K8S_APP_PARAMETERS_CSV $CONTAINERS_CSV $MANIFESTS_PATH

#database
DB_RESOURCES_PATH=$ENVIRONMENT_PATH/databases
DB_SERVICE_TEMPLATE=db.service.template.yaml
DB_CONTROLLER_TEMPLATE=db.deployment.template.yaml
K8S_DB_PARAMETERS_CSV=$PARAMETERS_PATH/k8s-db-manifests.csv
./k8s-db-manifest-builder.sh $DB_RESOURCES_PATH $SECRET_TEMPLATE $PV_TEMPLATE $PVC_TEMPLATE $TEMPLATES_PATH $DB_SERVICE_TEMPLATE $DB_CONTROLLER_TEMPLATE $K8S_DB_PARAMETERS_CSV $CONTAINERS_CSV $MANIFESTS_PATH