> 📌 Utilice una shell compatible con Unix (PowerShell o Git bash) para ejecutar scripts `.sh`

# DESPLIEGUE LOCAL

> 📋 **Pre requisitos** ([Revisar anexos](#anexos))
> - Instalar las siguientes tecnologías:
>   <br>`Java 17+`, `Maven 3.9.1+`, `GO 1.21+`, `Kafka & Zookeeper`, `MySQL`, `PostgreSQL`

> 📄 **Editar archivo** - Rutas de instalación (Java, Maven, GO, etc)
> ```shell script 
> notepad ./parameters/00_local_path_variables.sh
> ```

> 📄 **Editar archivo** - [Lista de proyectos a compilar](./parameters/README.md)
> ```shell script 
> notepad ./parameters/01_projects-to-compile.csv
> ```
> ▶️ **Compilar proyectos**
> ```shell script 
> cd ./shell-scripts
> ./01_compile_projects.sh
> ```

> 📄 **Editar archivo** - [Lista de servidores a iniciar (kafka, mysql-db, etc)](./parameters/README.md)
> ```shell script 
> notepad ./parameters/02_servers-to-start.csv
> ```
> ▶️ **Iniciar servidores**
> ```shell script 
> cd ./shell-scripts
> ./02_start_servers.sh
> ```

> ▶️ **Crear bases de datos**
> ```shell script 
> cd ./shell-scripts
> ./03_create_database.sh
> ```

> 📄 **Editar archivo** - [Lista de servicios a iniciar](./parameters/README.md)
> ```shell script 
> notepad ./parameters/04_services-to-start.csv
> ```
> ▶️ **Iniciar servicios**
> ```shell script 
> cd ./shell-scripts
> ./04_start_services.sh
> ```


---

# Anexos

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
> Iniciar el servidor e iniciar sesión con el password por defecto:
> ```
> net start mysql80
> mysql -u root
> ```
> Cambiar el password:
> ```
> ALTER USER 'root'@'localhost' IDENTIFIED BY 'qwerty';
> ```
