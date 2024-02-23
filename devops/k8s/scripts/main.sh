#!/bin/bash

ENV_VARIABLES=./apps/variables
DB_SECRET_VARIABLES=./databases/variables
DB_DATA=./databases/data

#apps
./apps/api-gateway-v1.sh $ENV_VARIABLES
./apps/config-server-v1.sh $ENV_VARIABLES
./apps/menu-v1.sh $ENV_VARIABLES
./apps/menu-v2.sh $ENV_VARIABLES
./apps/product-v1.sh $ENV_VARIABLES
./apps/registry-discovery-server-v1.sh $ENV_VARIABLES
./apps/table-placement-v1.sh $ENV_VARIABLES

#databases
./databases/mysql-db.sh $DB_SECRET_VARIABLES $DB_DATA