# Despliegue local
> 📋 **Pre requisitos**
> <br>Instalar GO, Java 11+, Kafka, Zookeeper, Maven 3.9+, MySQL, PostgreSQL, Redis.
> <br>⚠️ **Importante:** Guarde los binarios en directorios con nombres sin espaciados para evitar inconsistencias con los scripts. Por ejemplo:
> ```javascript
>   C:
>   │───dev-environment
>   │   ├───go\go1.21.4\bin
>   │   ├───java\jdk-17\bin
>   │   ├───kafka\bin
>   │   ├───maven\apache-maven-3.9.6\bin
>   │   ├───mysql\mysql-8.2.0\bin
>   │   ├───postgresql\postgresql-16.1\bin
>   │   └───redis\redis-3.2.100
>   └───dev-workspace
>       └───bbq-monorepo
> ```
> 📄 Editar las variables del archivo `./parameters/00_local_path_variables.sh` de acuerdo a su espacio de trabajo.

💻 Utilice una shell compatible con Unix (PowerShell o Git bash) para ejecutar los siguientes comandos.

> ▶️ **Compilar los proyectos**
<br>📄 Edite `./parameters/01_projects-to-compile.csv` con los proyectos que desea compilar
<br>
> ```shell script 
> cd ./shell-scripts
> ./01_compile_projects.sh
> ```

> ▶️ **Iniciar servidores (Kafka, Redis, PostgreSQL, MySQL)**
<br>📄 Edite `./parameters/02_servers-to-start.csv` con los servidores que desea iniciar
> ```shell script 
> cd ./shell-scripts
> ./02_start_servers.sh
> ```

> ▶️ **Crear bases de datos**
> ```shell script 
> cd ./shell-scripts
> ./03_create_database.sh
> ```

> ▶️ **Iniciar servicios**
<br>📄 Edite `./parameters/04_services-to-start.csv` con los servicios que desea iniciar
> ```shell script 
> cd ./shell-scripts
> ./04_start_services.sh
> ```
