> 📌 Puede utilizar `#` para comentar las líneas que desea ignorar.

# PARÁMETROS K8S

[← Regresar K8S](./../README.md)

> ### Lista de manifiestos
> 📄 `k8s-manifests.csv`

| Cabecera             | Descripción                                           |   
|----------------------|-------------------------------------------------------|
| `APP_NAME`           | Nombre del servicio.                                  |
| `NODE_PORT`          | Puerto de escucha del nodo k8s (entre 30000 y 32767). |
| `REPLICA_COUNT`      | Número de réplicas.                                   |
| `INITDB_FILE_SUFFIX` | Nombre del fichero de inicialización de BD.           |
| `CLUSTER_IP`         | IP del Clúster que aceptará las peticiones.           |
| `FRAMEWORK`          | Framework del servicio (`spring`, `null`).            |
