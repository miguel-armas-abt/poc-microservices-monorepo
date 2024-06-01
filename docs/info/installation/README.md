# INSTALACIÓN DE TECNOLOGÍAS

[← Regresar Local](./../../../devops/local/README.md) <br>
[← Regresar Jenkins](./../../../devops/jenkins/README.md)

<br>Guarde los binarios en directorios con nombres sin espaciados para evitar inconsistencias. Por ejemplo:
```javascript
  C:
  └───dev-environment
      ├───go\go1.21.4\bin
      ├───grafana\grafana-v10.4.2
      ├───java\jdk-17\bin
      ├───kafka\bin
      ├───loki
      │     ├───loki
      │     └───promtail
      ├───maven\apache-maven-3.9.6\bin
      ├───mysql\mysql-8.2.0\bin
      ├───postgresql\postgresql-16.1\bin
      ├───prometheus\prometheus-2.51.1
      ├───redis\redis-3.2.100
      └───zipkin
```

### Instalar GO
> Descargar para `Microsoft Windows` e instalar con ayuda del wizard.
> <br>**Ruta**: `C:\dev-environment\go\go1.21.4`
> <br>https://go.dev/dl/

### Instalar Java
> Descargar `zip` y reservar los binarios.
> <br>**Ruta**: `C:\dev-environment\java\jdk-17`
> <br>https://jdk.java.net/archive/

### Instalar Maven
> Descargar `Binary zip archive` y reservar los binarios.
> <br>**Ruta**: `C:\dev-environment\maven\apache-maven-3.9.6`
> <br>https://maven.apache.org/download.cgi

### Instalar Redis
> Descargar `Redis-x64` en `.zip` y reservar los binarios.
> <br>**Ruta**: `C:\dev-environment\redis\Redis-x64-3.0.504`
> <br>https://github.com/microsoftarchive/redis/releases

### Instalar Kafka
> Descargar `Binary downloads` en `.tgz` y reservar los binarios.
> <br>**Ruta**: `C:\dev-environment\kafka`
> <br>https://github.com/microsoftarchive/redis/releases

### Instalar PostgreSQL
> Descargar para `Win x86-64` y reservar los binarios.
> <br>**Ruta**: `C:\dev-environment\postgresql\postgresql-16.1`
> <br>https://www.enterprisedb.com/download-postgresql-binaries
>
> Ingresar a los binarios e instalar (solicitará el ingreso del password=`qwerty`):
> ```
> cd bin
> initdb.exe -U postgres -A password -E utf8 -W -D C:\dev-environment\postgresql-16.1\data
> ```

### Instalar MySQL
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

### Instalar Prometheus
> Descargar para `Windows`
> <br>**Ruta**: `C:\dev-environment\prometheus\prometheus-2.51.1`
> <br>https://prometheus.io/download/
> <br>
> - Configurar el archivo `prometheus.yml` con los servicios que desea monitorear
> - Ejecutar el archivo `prometheus.exe`
> - Puerto de escucha: `9090`

### Instalar Grafana
> Descargar para `Windows`
> <br>**Ruta**: `C:\dev-environment\grafana\grafana-v10.4.2`
> <br>https://grafana.com/grafana/download?platform=windows
> <br>
> - Ejecutar el archivo `\bin\grafana-server.exe`
> - Puerto de escucha: `3000`

### Instalar Zipkin
> Descargar para `Java` (latest release)
> <br>**Ruta**: `C:\dev-environment\zipkin`
> <br>https://zipkin.io/pages/quickstart
> <br>
> - Ejecutar el archivo .jar: `java -jar zipkin-server-3.3.0-exec.jar`
> - Puerto de escucha: `9411`

### Instalar Loki
> - Descargar `loki-windows-amd64.exe.zip` y `promtail-windows-amd64.exe.zip` desde la release de su preferencia.
> <br>https://github.com/grafana/loki/releases/
> <br>
> ![Loki Release](./images/loki-release.png)
>
> - Descargar el archivo de configuración para cada uno de los ejecutables. Para ello, reemplace la versión en los siguientes enlaces.
>   - loki: https://raw.githubusercontent.com/grafana/loki/v2.9.8/cmd/loki/loki-local-config.yaml
>   - promtail: https://raw.githubusercontent.com/grafana/loki/v2.9.8/clients/cmd/promtail/promtail-local-config.yaml
>
> **Rutas**:
> ```javascript
>   loki
>   ├───loki
>   │   ├───loki-windows-amd64.exe
>   │   └───loki-local-config.yaml
>   └───prompail
>       ├───promtail-windows-amd64.exe
>       └───promtail-local-config.yaml
> ```
>
> - Ejecutar Loki: `.\loki-windows-amd64.exe --config.file=loki-local-config.yaml`
> - Ejecutar Promptail: `.\promtail-windows-amd64.exe --config.file=promtail-local-config.yaml`
>
> **Informativo** - Instrucciones oficiales: https://grafana.com/docs/loki/latest/setup/install/local/


### Instalar Ngrok
> Ngrok permite realizar port forward de una URL local hacia una URL pública.
> - Ingrese a https://ngrok.com/ y haga login
> - Descargue el archivo ejecutable `ngrok.exe`
> - Ejecute solo la primera vez el comando provisto por Ngrok para autenticarse.
> ```shell script 
>  ngrok config add-authtoken <ngrok-auth-token>
> ```
> - Realice port forward de su URL local.
> ```shell script 
>  ngrok http http://localhost:8080
> ```