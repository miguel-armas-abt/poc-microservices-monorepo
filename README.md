# DevOps
Esta guía le ayudará a configurar y orquestar los servicios de BBQ

- [1. Clonar repositorio](#1-clonar-repositorio)
- [2. Compilar código fuente](#2-compilar-cdigo-fuente)
- [3. Despliegue local](#3-despliegue-local)
- [4. Orquestación con Docker Compose](#4-orquestacin-con-docker-compose)
- [5. Orquestación con Kubernetes](#5-orquestacin-con-kubernetes)
- [6. Conexión a las bases de datos](#6-conexin-a-las-bases-de-datos)

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
            │   ├───business-menu-option-v1
            │   ├───business-dining-room-order-v1
            │   └─── ...
            └───infrastructure-services
                ├───api-gateway-v1
                ├───config-server-v1
                ├───registry-discovery-server-v1
                └─── ...
```

- `bbq-parent-v1`: Maven Parent Module para los servicios de BBQ que fueron implementados con Spring Boot
- `bbq-support-v1`: Proyecto no ejecutable que centraliza configuraciones y utilidades requeridas por los servicios de BBQ que fueron implementados con Spring Boot
- `business-services`: Directorio que contiene los servicios de negocio
- `infrastructure-services`: Directorio que contiene los servicios de infraestructura

> **NOTA**: Compile los proyectos `bbq-parent-v1` y `bbq-support-v1` antes que cualquier otro servicio.

# 3. Despliegue local
Inicie los servicios de infraestructura antes que los servicios de negocio.
1. registry-discovery-server-v1
2. config-server-v1
3. api-gateway-v1

> **NOTA**: Considere en su ambiente local las dependencias de cada servicio de negocio, por ejemplo MySQL, PostgreSQL,
> Kafka, Redis, etc.

# 4. Orquestación con Docker Compose
## 4.1. Construir imágenes
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

## 4.2. Iniciar orquestación:
```shell script
docker-compose -f ./devops/docker-compose/docker-compose.yml up -d --force-recreate
```

## 4.3. Eliminar orquestación:
```shell script
docker-compose -f ./devops/docker-compose/docker-compose.yml down -v
```

# 5. Orquestación con Kubernetes
Enceder el clúster de Kubernetes de Minikube
```shell script
minikube start
```

## 5.1. Construir imágenes
Las imágenes de nuestros servicios deben estar disponibles en el clúster de Kubernetes de Minikube, para ello 
establecemos el entorno de Docker de Minikube en nuestra shell y seguidamente construimos las imágenes en la misma
sesión de la shell.

Servicios de infraestructura:
```shell script
docker build -t bbq-images/registry-discovery-server-v1:0.0.1-SNAPSHOT ./services/infrastructure-services/registry-discovery-server-v1
docker build -t bbq-images/config-server-v1:0.0.1-SNAPSHOT ./services/infrastructure-services/config-server-v1
docker build -t bbq-images/api-gateway-v1:0.0.1-SNAPSHOT ./services/infrastructure-services/api-gateway-v1
Invoke-Expression ((minikube docker-env) -join "`n")
```

Servicios de negocio:
```shell script
docker build -t bbq-images/business-menu-option-v1:0.0.1-SNAPSHOT ./services/business-services/business-menu-option-v1
docker build -t bbq-images/business-dining-room-order-v1:0.0.1-SNAPSHOT ./services/business-services/business-dining-room-order-v1
docker build -f ./services/business-services/business-menu-option-v2/src/main/docker/Dockerfile.jvm -t bbq-images/business-menu-option-v2:0.0.1-SNAPSHOT ./services/business-services/business-menu-option-v2
Invoke-Expression ((minikube docker-env) -join "`n")
```

A continuación, abrimos una shell en Minikube y revisamos que las imágenes hayan sido creadas.
```shell script
docker images
minikube ssh
```

## 5.2. Iniciar orquestación:
```shell script
kubectl apply -f ./devops/k8s/mysql_db/
kubectl apply -f ./devops/k8s/postgres_db/
kubectl apply -f ./devops/k8s/registry-discovery-server-v1/
kubectl apply -f ./devops/k8s/config-server-v1/
kubectl apply -f ./devops/k8s/business-menu-option-v1/
```

### 5.3. Eliminar orquestación:
```shell script
kubectl delete -f ./devops/k8s/mysql_db/
kubectl delete -f ./devops/k8s/postgres_db/
kubectl delete -f ./devops/k8s/registry-discovery-server-v1/
kubectl delete -f ./devops/k8s/config-server-v1/
kubectl delete -f ./devops/k8s/business-menu-option-v1/
```

# 6. Conexión a las bases de datos
Podemos utilizar DBeaver para conectarnos a las diferentes bases de datos relacionales
## 6.1. MYSQL
| Parámetro         | Valor en orquestación con Docker Compose       | Valor en orquestación K8S                                           |   
|-------------------|------------------------------------------------|---------------------------------------------------------------------|
| Server Host       | `localhost`                                    | `localhost`                                                         |
| Port              | `3306`                                         | Puerto generado por Minikube: `minikube service --url svc-mysql-db` |
| Database          | `db_menu_options?allowPublicKeyRetrieval=true` | `db_menu_options?allowPublicKeyRetrieval=true`                      |
| Nombre de usuario | `root` o  `bbq_user`                           | `root` o  `bbq_user`                                                |
| Contraseña        | `qwerty`                                       | `qwerty`                                                            |

## 6.2. PostgreSQL
- Activar la opción `Show all database` de la pestaña `PostgreSQL`

| Parámetro         | Valor en orquestación con Docker Compose   | Valor en orquestación K8S                                              |   
|-------------------|--------------------------------------------|------------------------------------------------------------------------|
| Connect by        | `HOST`                                     | `HOST`                                                                 |
| Host              | `localhost`                                | `localhost`                                                            |
| Port              | `5432`                                     | Puerto generado por Minikube: `minikube service --url svc-postgres-db` |
| Database          | `db_dining_room_orders`                    | `db_dining_room_orders`                                                |
| Nombre de usuario | `postgres` o  `bbq_user`                   | `postgres` o  `bbq_user`                                               |
| Contraseña        | `qwerty`                                   | `qwerty`                                                               |

# 7. Jenkins
## 7.1. Crer imagen y ejecutar contenedor
```shell script
docker-compose -f ./devops/jenkins/docker-compose-cicd.yml up -d --force-recreate
docker build -t bbq-images/jenkins:v1 --no-cache ./devops/jenkins 
```

## 7.2. Desbloquear
- Revisar los logs del contenedor y copiar el password en la pantalla de login `localhost:8080`
```shell script
docker logs <jenkins-container-id>
```
- Seleccionar la opción `Install suggested plugins` para instalar los plugins sugeridos.
- Crear la cuenta de administrador. Utilice `admin` y `qwerty` para el nombre de usuario y el password respectivamente.
- Dejar la configuración de la URL por defecto, `http://localhost:8080/`

## 7.3. GitHub Webhooks
Para conectar Jenkins con GitHub Webhook es necesario que nuestra instancia de Jenkins sea accesible desde internet.

### 7.3.1. URI del reverse proxy
A continuación, obtendremos la URI de un reverse proxy que redirija las peticiones de internet hacia nuestra instancia local.

- Cree una cuenta en `ngrok`, descargue la versión estable y descomprima el fichero .zip
- Acceda al directorio descomprimido y ejecute los siguientes comandos para conectar su cuenta y exponer el puerto `8080` hacia internet
```shell script
./ngrok http 8080
./ngrok config add-authtoken <ngrok-authtoken>
cd <ngrok-directory>
```
- Copie el valor de `Forwarding`. Por ejemplo, `https://95ee-179-6-212-27.ngrok-free.app`. 

### 7.3.2. Configurar repositorio
Para notificar a nuestra instancia de Jenkis sobre los cambios en el repositorio, configuraremos nuestro proxy reverse 
en la sección Webhooks (`Repository > Settigs > Webhooks`).
- **Payload URL**: `<URI del reverse proxy>/github-webhook/`
- **Content type**: `application/json`
- **Which events would you like to trigger this webhook?**: `Just push event`

### 7.3.3. Vincular Jenkins con GitHub Server
Accedemos a `Panel de Control > Administrar Jenkins > System > GitHub`. A continuación, seleccionamos la opción 
`Add GitHub Server` y agregamos los siguientes campos.
- **Name**: `miguel-armas-abt-server`
- **API URL**: `https://api.github.com`

Para el siguiente campo de tipo `Secret text` generaremos un token clásico en GitHub. Para ello accederemos a la opción
de GitHub `Account > Settings > Developer settings > Personal access tokens` y crearemos un nuevo token con acceso a `repo`.
- **Credentials**: `<github-classic-token>`

### 7.3.4. Activar hook en pipelines
Para que nuestros pipelines se ejecuten automáticamente cuando escuchen algún evento en el repositorio iremos a la sección
`Disparadores de ejecución` y seleccionaremos la opción `GitHub hook trigger for GITScm polling`.




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
> - SpringDoc
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

Actual:
El registry server descubre a los deployments y no a los servicios, por ello se genera un error en la conexión al config server