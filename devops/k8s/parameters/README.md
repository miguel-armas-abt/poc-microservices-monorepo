> 📌 Puede utilizar `#` para comentar las líneas que desea ignorar.

[← Regresar](./../README.md)

# PARÁMETROS K8S

> ### Lista de manifiestos de tipo APP
> 📄 `k8s-app-manifests.csv`

| Cabecera           | Descripción                                                  |   
|--------------------|--------------------------------------------------------------|
| `APP_NAME`         | Nombre del servicio.                                         |
| `NODE_PORT`        | Puerto de escucha del nodo k8s (entre 30000 y 32767).        |
| `CONTROLLER_TYPE`  | Tipo de controlador (`STS`: Statefulset, `DEP`: Deployment). |
| `REPLICA_COUNT`    | Número de réplicas.                                          |

> ### Lista de manifiestos de tipo DB
> 📄 `k8s-db-manifests.csv`

| Cabecera           | Descripción                                                  |   
|--------------------|--------------------------------------------------------------|
| `APP_NAME`         | Nombre del servicio.                                         |
| `SUB_PATH_INIT_DB` | Nombre del fichero de inicialización de BD.                  |
| `CLUSTER_IP`       | IP del Clúster que aceptará las peticiones.                  |
| `NODE_PORT`        | Puerto de escucha del nodo k8s (entre 30000 y 32767).        |
| `CONTROLLER_TYPE`  | Tipo de controlador (`STS`: Statefulset, `DEP`: Deployment). |
| `REPLICA_COUNT`    | Número de réplicas.                                          |