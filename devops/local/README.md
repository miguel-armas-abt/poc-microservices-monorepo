> 📌 Utilice una shell compatible con Unix (PowerShell o Git bash) para ejecutar scripts `.sh`

# DESPLIEGUE LOCAL

[← Ir a Principal](./../../README.md) <br>
[← Ir a Docker Compose](./../docker-compose/README.md) <br>
[← Ir a K8S](./../k8s/README.md)

> ✅ **Pre requisitos**
> Instalar las siguientes tecnologías:
>   - [Java 17+](https://github.com/miguel-armas-abt/technical-resources/blob/main/02_backend/java/jdk/install/README.md)
>   - [Maven 3.9.1+](https://github.com/miguel-armas-abt/technical-resources/blob/main/02_backend/java/dependency-management/maven/install/README.md)
>   - [GO 1.21+](https://github.com/miguel-armas-abt/technical-resources/blob/main/02_backend/go/install/README.md)
>   - [Kafka](https://github.com/miguel-armas-abt/technical-resources/blob/main/02_backend/kafka/install/README.md)
>   - [MySQL](https://github.com/miguel-armas-abt/technical-resources/blob/main/01_database/mysql/install/README.md)
>   - [PostgreSQL](https://github.com/miguel-armas-abt/technical-resources/blob/main/01_database/postgresql/install/README.md)
>   - [Prometheus](https://github.com/miguel-armas-abt/technical-resources/blob/main/04_devops/observability/prometheus/install/README.md)
>   - [Zipkin](https://github.com/miguel-armas-abt/technical-resources/blob/main/04_devops/observability/zipkin/install/README.md)
>   - [Grafana](https://github.com/miguel-armas-abt/technical-resources/blob/main/04_devops/observability/grafana/install/README.md)
>   - [Loki](https://github.com/miguel-armas-abt/technical-resources/blob/main/04_devops/observability/loki/install/README.md)

📄 **Configurar entorno local** - Rutas de instalación (Java, Maven, GO, etc)
> ```shell script 
> notepad ./parameters/00_local_path_variables.sh
> ```

▶️ **Compilar proyectos**
> ```shell script 
> notepad ./parameters/01_projects-to-compile.csv
> ```
> ```shell script 
> cd ./shell-scripts
> ./01_compile_projects.sh
> ```

▶️ **Iniciar servidores**
> ```shell script 
> notepad ./parameters/02_servers-to-start.csv
> ```
> ```shell script 
> cd ./shell-scripts
> ./02_start_servers.sh
> ```

▶️ **Crear bases de datos**
> ```shell script 
> cd ./shell-scripts
> ./03_create_database.sh
> ```

▶️ **Ejecutar servicios**
> ```shell script 
> notepad ./parameters/04_services-to-run.csv
> ```
> ```shell script 
> cd ./shell-scripts
> ./04_run_services.sh
> ```

▶️ [Configurar Keycloak](./../../docs/info/keycloak/README.md)
