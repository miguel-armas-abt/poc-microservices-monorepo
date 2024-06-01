> 📌 Utilice una shell compatible con Unix (PowerShell o Git bash) para ejecutar scripts `.sh`

# DESPLIEGUE LOCAL

[← Regresar](./../../README.md)

> ✅ **Pre requisitos**
> - [Instalar las siguientes tecnologías:](./../../docs/info/installation/README.md)
>   <br>`Java 17+`, `Maven 3.9.1+`, `GO 1.21+`, `Kafka & Zookeeper`, `MySQL`, `PostgreSQL`, `Prometheus`, `Zipkin`, `Grafana`, `Loki & Promtail`

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

▶️ **Iniciar servicios**
> ```shell script 
> notepad ./parameters/04_services-to-start.csv
> ```
> ```shell script 
> cd ./shell-scripts
> ./04_start_services.sh
> ```