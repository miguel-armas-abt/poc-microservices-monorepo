#!/bin/bash

#general
CONFIG_MAP_TEMPLATE=config-map.template.yaml
SECRET_TEMPLATE=secret.template.yaml
PV_TEMPLATE=persistent-volume.template.yaml
PVC_TEMPLATE=persistent-volume-claim.template.yaml

#app
APP_VARIABLES_PATH=./../../environment/apps
APP_SERVICE_TEMPLATE=app.service.template.yaml
APP_CONTROLLER_TEMPLATE=app.controller.template.yaml
./k8s-app-manifest-builder.sh $APP_VARIABLES_PATH $CONFIG_MAP_TEMPLATE $APP_SERVICE_TEMPLATE $APP_CONTROLLER_TEMPLATE

#database
DB_RESOURCES_PATH=./../../environment/databases
DB_SERVICE_TEMPLATE=db.service.template.yaml
DB_CONTROLLER_TEMPLATE=db.deployment.template.yaml
./k8s-db-manifest-builder.sh $DB_RESOURCES_PATH $SECRET_TEMPLATE $PV_TEMPLATE $PVC_TEMPLATE $CONFIG_MAP_TEMPLATE $DB_SERVICE_TEMPLATE $DB_CONTROLLER_TEMPLATE