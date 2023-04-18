# DevOps
Esta guía le ayudará a configurar y orquestar los servicios de BBQ

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

- `bbq-parent-v1`: Maven Parent Module para servicios de BBQ implementados con Spring Boot
- `bbq-support-v1`: Proyecto no ejecutable que centraliza configuraciones y utilidades requeridas por los servicios de BBQ implementados con Spring Boot
- `business-services`: Directorio que contiene los servicios de negocio
- `infrastructure-services`: Directorio que contiene los servicios de infraestructura

Compile los proyectos `bbq-parent-v1` y `bbq-support-v1` antes que los servicios de negocio e infraestructura, ya que 
tienen dependencia.
```shell script
mvnw clean install
```

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
| `GET`       | `http://localhost:8888/business-menu-option-v1/dev` | Username: `admin`, Password: `123` |

# 4. Despliegue local
Inicie los servicios de infraestructura antes que los servicios de negocio.
1. registry-discovery-server-v1
2. config-server-v1
3. api-gateway-v1

> **Importante**: Considere en su ambiente local las dependencias de cada servicio de negocio, por ejemplo MySQL, PostgreSQL,
> Kafka, Redis, etc.

# 5. Orquestación con Docker Compose
## 5.1. Construir imágenes
Servicios de infraestructura:
```shell script
docker build -t bbq-images/registry-discovery-server-v1:0.0.1-SNAPSHOT ./services/infrastructure-services/registry-discovery-server-v1
docker build -t bbq-images/config-server-v1:0.0.1-SNAPSHOT ./services/infrastructure-services/config-server-v1
docker build -t bbq-images/api-gateway-v1:0.0.1-SNAPSHOT ./services/infrastructure-services/api-gateway-v1
```

Servicios de negocio:
```shell script
docker build -t bbq-images/business-menu-option-v1:0.0.1-SNAPSHOT ./services/business-services/business-menu-option-v1
docker build -t bbq-images/business-dining-room-order-v1:0.0.1-SNAPSHOT ./services/business-services/business-dining-room-order-v1
docker build -f ./services/business-services/business-menu-option-v2/src/main/docker/Dockerfile.jvm -t bbq-images/business-menu-option-v2:0.0.1-SNAPSHOT ./services/business-services/business-menu-option-v2
```

## 5.2. Iniciar orquestación:
```shell script
docker-compose -f ./devops/docker-compose/docker-compose.yml up -d --force-recreate
```

## 5.3. Eliminar orquestación:
```shell script
docker-compose -f ./devops/docker-compose/docker-compose.yml down -v
```

# 6. Orquestación con Kubernetes
Enceder el clúster de Kubernetes de Minikube
```shell script
minikube start
```

## 6.1. Construir imágenes
Las imágenes de nuestros servicios deben estar disponibles en el clúster de Kubernetes de Minikube, para ello 
establecemos el entorno de Docker de Minikube en nuestra shell y seguidamente construimos las imágenes en la misma
sesión de la shell.

Servicios de infraestructura:
```shell script
Invoke-Expression ((minikube docker-env) -join "`n")
docker build -t bbq-images/registry-discovery-server-v1:0.0.1-SNAPSHOT ./services/infrastructure-services/registry-discovery-server-v1
docker build -t bbq-images/config-server-v1:0.0.1-SNAPSHOT ./services/infrastructure-services/config-server-v1
docker build -t bbq-images/api-gateway-v1:0.0.1-SNAPSHOT ./services/infrastructure-services/api-gateway-v1
```

Servicios de negocio:
```shell script
Invoke-Expression ((minikube docker-env) -join "`n")
docker build -t bbq-images/business-menu-option-v1:0.0.1-SNAPSHOT ./services/business-services/business-menu-option-v1
docker build -t bbq-images/business-dining-room-order-v1:0.0.1-SNAPSHOT ./services/business-services/business-dining-room-order-v1
docker build -f ./services/business-services/business-menu-option-v2/src/main/docker/Dockerfile.jvm -t bbq-images/business-menu-option-v2:0.0.1-SNAPSHOT ./services/business-services/business-menu-option-v2
```

A continuación, abrimos una shell en Minikube y revisamos que las imágenes hayan sido creadas.
```shell script
minikube ssh
docker images
```

## 6.2. Iniciar orquestación:
```shell script
kubectl apply -f ./devops/k8s/mysql_db/
kubectl apply -f ./devops/k8s/postgres_db/
kubectl apply -f ./devops/k8s/registry-discovery-server-v1/
```

### 6.3. Eliminar orquestación:
```shell script
kubectl delete -f ./devops/k8s/mysql_db/
kubectl delete -f ./devops/k8s/postgres_db/
kubectl delete -f ./devops/k8s/registry-discovery-server-v1/
```

## 5.4. Conexión a las bases de datos
Podemos utilizar DBeaver para conectarnos a las diferentes bases de datos relacionales
### 5.4.1. MYSQL
| Parámetro         | Valor en orquestación con Docker Compose       | Valor en orquestación K8S                                           |   
|-------------------|------------------------------------------------|---------------------------------------------------------------------|
| Server Host       | `localhost`                                    | `localhost`                                                         |
| Port              | `3306`                                         | Puerto generado por Minikube: `minikube service --url svc-mysql-db` |
| Database          | `db_menu_options?allowPublicKeyRetrieval=true` | `db_menu_options?allowPublicKeyRetrieval=true`                      |
| Nombre de usuario | `root` o  `bbq_user`                           | `root` o  `bbq_user`                                                |
| Contraseña        | `qwerty`                                       | `qwerty`                                                            |

### 5.4.2. PostgreSQL
- Activar la opción `Show all database` de la pestaña `PostgreSQL`

| Parámetro         | Valor en orquestación con Docker Compose   | Valor en orquestación K8S                                              |   
|-------------------|--------------------------------------------|------------------------------------------------------------------------|
| Connect by        | `HOST`                                     | `HOST`                                                                 |
| Host              | `localhost`                                | `localhost`                                                            |
| Port              | `5432`                                     | Puerto generado por Minikube: `minikube service --url svc-postgres-db` |
| Database          | `db_dining_room_orders`                    | `db_dining_room_orders`                                                |
| Nombre de usuario | `postgres` o  `bbq_user`                   | `postgres` o  `bbq_user`                                               |
| Contraseña        | `qwerty`                                   | `qwerty`                                                               |







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