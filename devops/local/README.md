# Despliegue local
> 📋 **Pre requisitos**
> - Instalar GO, Java 11+, Kafka, Zookeeper, Maven 3.9+, MySQL, PostgreSQL, Redis.
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
> - Editar las variables del archivo `./devops/local/00_local_path_variables.bat` de acuerdo a su espacio de trabajo.

> 🔨 **Compilar los proyectos**
> ```shell script 
> ./01_install_services.bat
> ```

> ▶️ **Iniciar servicios de infraestructura**
> ```shell script 
> ./02_start_infra_services.bat
> ```

> ▶️ **Iniciar servidores (Kafka, Redis, PostgreSQL, MySQL)**
> ```shell script 
> ./03_start_servers.bat
> ```

> 🔧 **Crear bases de datos**
> ```shell script 
> ./04_create_databases.bat
> ```

> ▶️ **Iniciar servicios de negocio**
> ```shell script 
> ./05_start_business_services.bat
> ```
