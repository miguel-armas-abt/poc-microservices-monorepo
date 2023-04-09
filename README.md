# DevOps
Esta guía le ayudará a desplegar y orquestar los servicios de BBQ

- [1. Clonar repositorio](#1-clonar-repositorio)
- [2. Compilar código fuente](#2-compilar-código-fuente)
- [3. Configurar Config Server](#3-configurar-config-server)
- [4. Despliegue local](#4-despliegue-local)
- [5. Despliegue con contenedores ](#5-despliegue-con-contenedores)

# 1. Clonar repositorio
El repositorio del proyecto tiene las siguientes ramas:
- `develop`: Contiene el código fuente en su versión de desarrollo
- `main`: Contiene la última versión estable del código fuente
- `config-server`: Contiene los archivos de configuración de los servicios

Crearemos un directorio `/bbq-project` y en su interior clonaremos las ramas `develop` y `config-server` con los nombres
`bbq-monorepo` y `bbq-config-server-repo` respectivamente:
```javascript
└───bbq-project
    ├───bbq-monorepo
    └───bbq-config-server-repo
```

```shell script
git clone -b develop <URL> bbq-monorepo
```
```shell script
git clone -b config-server <URL> bbq-config-server-repo
```
URL: <https://github.com/miguel-armas-abt/demo-microservices-bbq.git>

# 2. Compilar código fuente
El código fuente de los servicios está en el directorio `/services`:
- `bbq-parent-v1`: Maven Parent Module para servicios de BBQ implementados con Spring Boot
- `bbq-support-v1`: Proyecto no ejecutable que centraliza configuraciones y utilidades requeridas por los servicios de BBQ implementados con Spring Boot
- `business-services`: Directorio que contiene los servicios de negocio
- `infrastructure-services`: Directorio que contiene los servicios de infraestructura

```javascript
└───bbq-project
    └───bbq-monorepo
        └───services
            ├───bbq-parent-v1
            ├───bbq-support-v1
            ├───business-services
            │   └─── ...
            └───infrastructure-services
                ├───api-gateway-v1
                ├───config-server-v1
                ├───registry-discovery-server-v1
                └─── ...
```

Considere compilar `bbq-parent-v1` y `bbq-support-v1` antes que los servicios de negocio e infraestructura, ya que 
podrían tener dependencia.
```shell script
mvnw clean install
```

> **Importante**: Los servicios de negocio e infraestructura, implementados con Spring Boot, utilizan el plugin 
> `dockerfile-maven-plugin` para construir automáticamente las imágenes de Docker al finalizar la fase `package` de Maven. 
> 
> Este proceso requiere que el Docker Engine esté activo, de otro modo obtendrá un error. En tal sentido usted puede tomar
> alguna de las siguientes acciones mientras trabaja localmente:
> 
> 1. Encender el Docker Engine
> 2. Comentar el plugin `dockerfile-maven-plugin` en el archivo `pom.xml`

# 3. Configurar Config Server
## 3.1. Apuntar al repositorio de archivos de configuración
La implementación del servidor de configuraciones está en el siguiente directorio:
```javascript
└───bbq-project
    └───bbq-monorepo
        └───services
            └───infrastructure-services
                └───config-server-v1
```

El proyecto `config-server-v1` se debe conectar localmente a nuestro repositorio de archivos de configuración (rama `config-server`)
y para ello modificaremos la propiedad `spring.cloud.config.server.absolute-path` del archivo `application.yaml`.

> `absolute-path: C:\\\\dev-workspace\\\\bbq-project\\\\bbq-config-server-repo\\\\`

## 3.2. Probar funcionamiento del Config Server
El servidor de configuraciones (`config-server-v1`) depende del servidor de registro y descubrimiento 
(`registry-discovery-server-v1`), así que para validar el funcionamiento del servidor de configuraciones compilaremos 
el código fuente de ambos proyectos y ejecutaremos ambas aplicaciones en conjunto.

A continuación, podemos ejecutar la siguiente petición para validar que se están recuperando las propiedades de los 
servicios correctamente.

| HTTP Method | URI                                                 | Header Basic Auth                  |
|-------------|-----------------------------------------------------|------------------------------------|
| `GET`       | `http://localhost:8888/business/menu-option-v1-dev` | Username: `admin`, Password: `123` |

# 4. Despliegue local
Inicie los servicios de infraestructura antes que los servicios de negocio.
1. registry-discovery-server-v1
2. config-server-v1
3. api-gateway-v1

> **Importante**: Considere en su ambiente local las dependencias de cada servicio de negocio, por ejemplo MySQL, PostgreSQL,
> Kafka, Redis, etc.

# 5. Despliegue con contenedores

## 5.1. Construir imágenes de Docker
### 5.1.1. Servicios con Spring Boot
Los servicios implementados con Spring Boot hacen uso del plugin `dockerfile-maven-plugin` para la construcción
automática de las imágenes durante la fase `package` de Maven.

Ejecute el siguiente comando sobre cada proyecto de Spring Boot para construir sus respectivas imágenes :
```shell script
mvnw clean package
```

### 5.1.2. Servicios con Quarkus
El framework de Quarkus provee una serie de archivos Dockerfile de acuerdo al tipo de compilación que se requiera, 
ubicados en el directorio `src/main/docker/`. Para este caso se ha configurado el uso del archivo `Dockerfile.jvm`.

Ejecute el siguiente comando sobre cada proyecto de Quarkus para construir sus respectivas imágenes :
- Reemplazar `<service-name>` por el nombre del servicio
```shell script
docker build -f src/main/docker/Dockerfile.jvm -t bbq-images/<service-name>:0.0.1-SNAPSHOT .
```

## 5.2. Orquestación con Docker Compose

### Iniciar servicios:
```shell script
docker-compose -f ./devops/docker-compose/docker-compose.yml up -d --force-recreate
```

### Eliminar servicios:
```shell script
docker-compose -f ./devops/docker-compose/docker-compose.yml down -v
```

## 5.3. Orquestación con Kubernetes

## 5.4. Conexión a las bases de datos
Podemos utilizar DBeaver para conectarnos a las diferentes bases de datos relacionales
### 5.4.1. MYSQL
| Parámetro         | Valor                                          | 
|-------------------|------------------------------------------------|
| Server Host       | `localhost`                                    | 
| Database          | `db_menu_options?allowPublicKeyRetrieval=true` | 
| Nombre de usuario | `root` o  `bbq_user`                           | 
| Contraseña        | `qwerty`                                       |

### 5.4.2. PostgreSQL
- Activar la opción `Show all database` de la pestaña `PostgreSQL`

| Parámetro         | Valor                    | 
|-------------------|--------------------------|
| Connect by        | `HOST`                   | 
| Host              | `localhost`              | 
| Database          | `db_dining_room_orders`  | 
| Port              | `5432`                   |
| Nombre de usuario | `postgres` o  `bbq_user` | 
| Contraseña        | `qwerty`                 |







##########
Afinar lo siguiente:
## 4. Despliegue local
> 1. Desplegar registry-discovery-server, config-server y api-gateway
> 2. Desplegar el proveedor de autenticación Keycloak
> - docker-compose -f docker-compose.yml up -d keycloak-server
> - Ingresar con las credenciales (username=admin, password=admin) en `http://localhost:8091`
> - **Realm**: Crear un realm con nombre bbq-management
> - **Realm**: Ubicar la llave pública RS256 del realm creado y reemplazar la propiedad keycloak.certs-id del application.yaml de auth-adapter
> - **Realm**: Cambiar el tiempo de expiración del token a 30' (Access Token Lifespan)
> - **User**: Crear user (username=admin, password=admin, temporary=off)
> - **Roles**: Crear rol (rolename=partners)
> - **User**: Agregar rol creado al usuario
> - **Client**: Crear cliente (clientid=front-bbq-app, client-protocol=openid-connect)
> - **Client**: Actualizar la propiedad Valid Redirect URIs=*
> - Configurar el realm, el usuario y sus roles (rol=partners)
> 3. Desplegar auth-adapter
> 4. Desplegar business-menu-option, business-dining-room-order, business-invoice, business-payment

### Consideraciones
> - Para omitir la autenticación a través de Keycloak, comentar todas las ocurrencias del filtro AuthenticatorFiltering
    > en la propiedad spring.cloud.gateway.routes.<id>.filters del archivo application.yaml de api-gateway. De esta manera
    > no se aplicará el filtro de autenticación

### Mejoras
> - Faltan pruebas unitarias
> - Generar las peticiones automáticas del token en Postman
> - Copiar manejo de excepcion externa de atlas
> - Crear los servicios en quarkus
> - Revisar los Dockerfiles. Tienen diferente esctructura a los del curso
> - Revisar el nombre consult-menu-options. No es un nombre funcional, sino tecnico
>
>
Para el desplieuge en local de los servicios desarrollados con Quarkus hay que tener las siguientes consideraciones:
- Tener instalado Native Tools Command Prompt
- Tener instalado GraalVM (variables de entorno GRAALVM_HOME, JAVA_HOME=%GRAALVM_HOME%. verificar con 'echo JAVA_HOME')
- Tener instalado Maven (variable de entorno MAVEN_HOME)