> 📌 Puede utilizar `#` para comentar las líneas que desea ignorar.

# PARÁMETROS K8S

> 📄 **k8s-app-manifests.csv** - Lista de manifiestos de tipo APP
>   - `APP_NAME`: Nombre del servicio.
>   - `NODE_PORT`: Puerto de escucha del nodo k8s (entre 30000 y 32767).
>   - `CONTROLLER_TYPE`: Tipo de controlador (`STS`: Statefulset, `DEP`: Deployment).
>   - `REPLICA_COUNT`: Número de réplicas.

> 📄 **k8s-db-manifests.csv** - Lista de manifiestos de tipo DB
>   - `APP_NAME`: Nombre del servicio.
>   - `SUB_PATH_INIT_DB`: Nombre del fichero de inicialización de BD.
>   - `CLUSTER_IP`: IP del Clúster que aceptará las peticiones.
>   - `NODE_PORT`: Puerto de escucha del nodo k8s (entre 30000 y 32767).
>   - `CONTROLLER_TYPE`: Tipo de controlador (`STS`: Statefulset, `DEP`: Deployment).
>   - `REPLICA_COUNT`: Número de réplicas.