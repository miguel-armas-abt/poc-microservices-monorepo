# Despliegue local

> 📋 **Pre requisitos**
> - Instalar las siguientes tecnologías (Revisar anexo):
>   - Java 11+
>   - GO 1.21+
>   - Kafka & Zookeeper
>   - Maven 3.9+
>   - MySQL
>   - PostgreSQL
>   - Redis
> - ⚙️ Editar las variables del archivo `./parameters/00_local_path_variables.sh` de acuerdo a su espacio de trabajo.

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


---

# Anexo

<br>Guarde los binarios en directorios con nombres sin espaciados para evitar inconsistencias con los scripts. Por ejemplo:
```javascript
  C:
  └───dev-environment
      ├───go\go1.21.4\bin
      ├───java\jdk-17\bin
      ├───kafka\bin
      ├───maven\apache-maven-3.9.6\bin
      ├───mysql\mysql-8.2.0\bin
      ├───postgresql\postgresql-16.1\bin
      └───redis\redis-3.2.100
```

> ### Instalar GO
> Descargar para `Microsoft Windows` e instalar con ayuda del wizard.
> <br>**Ruta**: `C:\dev-environment\go\go1.21.4`
> <br>https://go.dev/dl/

> ### Instalar Java
> Descargar `zip` y reservar los binarios.
> <br>**Ruta**: `C:\dev-environment\java\jdk-17`
> <br>https://jdk.java.net/archive/

> ### Instalar Maven
> Descargar `Binary zip archive` y reservar los binarios.
> <br>**Ruta**: `C:\dev-environment\maven\apache-maven-3.9.6`
> <br>https://maven.apache.org/download.cgi

> ### Instalar Redis
> Descargar `Redis-x64` en `.zip` y reservar los binarios.
> <br>**Ruta**: `C:\dev-environment\redis\Redis-x64-3.0.504`
> <br>https://github.com/microsoftarchive/redis/releases

> ### Instalar Kafka
> Descargar `Binary downloads` en `.tgz` y reservar los binarios.
> <br>**Ruta**: `C:\dev-environment\kafka`
> <br>https://github.com/microsoftarchive/redis/releases

> ### Instalar PostgreSQL
> Descargar para `Win x86-64` y reservar los binarios.
> <br>**Ruta**: `C:\dev-environment\postgresql\postgresql-16.1`
> <br>https://www.enterprisedb.com/download-postgresql-binaries
>
> Ingresar a los binarios e instalar (solicitará el ingreso del password=`qwerty`):
> ```
> cd bin
> initdb.exe -U postgres -A password -E utf8 -W -D C:\dev-environment\postgresql-16.1\data
> ```

> ### Instalar MySQL
> Descargar `Windows (x86, 64-bit), ZIP Archive` y reservar los binarios.
> <br>**Ruta**: `C:\dev-environment\mysql\mysql-8.2.0`
> <br>https://dev.mysql.com/downloads/mysql/
>
> Ingresar a los binarios e instalar:
> ```
> cd bin
> mysqld --initialize
> mysqld --install "mysql80"
> ```
> Inciar el servidor:
> ```
> net start mysql80
> ```
> Cambiar el password:
> ```
> ALTER USER 'root'@'localhost' IDENTIFIED BY 'qwerty';
> ```
