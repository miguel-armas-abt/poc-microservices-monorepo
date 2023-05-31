# Despliegue
Esta guía le ayudará a configurar y desplegar los servicios de BBQ

- [1. Clonar repositorio](#1-clonar-repositorio)
- [2. Compilar codigo fuente](#2-compilar-codigo-fuente)
- [3. Despliegue local](#3-despliegue-local)
- [4. Orquestacion con Docker Compose](#4-orquestacion-con-docker-compose)
- [5. Orquestacion con Kubernetes](#5-orquestacion-con-kubernetes)
- [6. Conexion a las bases de datos](#6-conexion-a-base-de-datos)

# 1. Clonar repositorio
El repositorio del proyecto tiene las siguientes ramas:
- `feature/<feature-name>`: Contiene el código fuente en su versión de desarrollo
- `main`: Contiene la última versión estable del código fuente
- `config-server`: Contiene los archivos de configuración de los servicios

```shell script
git checkout -b feature/<feature-name>
cd bbq-monorepo 
git clone -b main <URL> bbq-monorepo
```
URL: <https://github.com/miguel-armas-abt/demo-microservices-bbq.git>

# 2. Compilar codigo fuente
El código fuente de los servicios web está en el directorio `/services`:

```javascript
    bbq-monorepo
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

- `bbq-parent-v1`: Proyecto `parent module` para los servicios web que fueron implementados con Spring Boot
- `bbq-support-v1`: Proyecto no ejecutable que centraliza las utilidades requeridas por los servicios web implementados con Spring Boot
- `business-services`: Directorio que contiene los servicios web de negocio
- `infrastructure-services`: Directorio que contiene los servicios web de infraestructura

> **NOTA**: Para que los servicios web de negocio e infraestructura compilen es necesario compilar previamente los proyectos `bbq-parent-v1` y `bbq-support-v1`.

# 3. Despliegue local
Para desplegar localmente los servicios web es necesario ejecutar previamente los servicios de infraestructura 
`registry-discovery-server-v1`, `config-server-v1` y `api-gateway-v1`. 

Los detalles de cada servicio web de negocio los encontrará en sus respectivos README.

# 4. Orquestacion con Docker Compose
## 4.1. Construir imágenes
Servicios de infraestructura:
```shell script
docker build -t miguelarmasabt/registry-discovery-server-v1:0.0.1-SNAPSHOT ./services/infrastructure-services/registry-discovery-server-v1
docker build -t miguelarmasabt/config-server-v1:0.0.1-SNAPSHOT ./services/infrastructure-services/config-server-v1
docker build -t miguelarmasabt/auth-adapter-v1:0.0.1-SNAPSHOT ./services/infrastructure-services/auth-adapter-v1
docker build -t miguelarmasabt/api-gateway-v1:0.0.1-SNAPSHOT ./services/infrastructure-services/api-gateway-v1
```

Servicios de negocio:
```shell script
docker build -t miguelarmasabt/business-menu-option-v1:0.0.1-SNAPSHOT ./services/business-services/business-menu-option-v1
docker build -t miguelarmasabt/business-dining-room-order-v1:0.0.1-SNAPSHOT ./services/business-services/business-dining-room-order-v1
docker build -f ./services/business-services/business-menu-option-v2/src/main/docker/Dockerfile.jvm -t miguelarmasabt/business-menu-option-v2:0.0.1-SNAPSHOT ./services/business-services/business-menu-option-v2
```

## 4.2. Iniciar orquestación
Para forzar la recreación de los servicios utilice el flag `--force-recreate`
```shell script
docker-compose -f ./devops/docker-compose/docker-compose.yml up -d
```

## 4.3. Detener orquestación
```shell script
docker-compose -f ./devops/docker-compose/docker-compose.yml stop
```

# 5. Orquestacion con Kubernetes
Si usted requiere asignar más memoria RAM a Docker Desktop, cree el archivo `.wslconfig` en la ruta de usuario
`C:\Users\<username>\ `, agregue el siguiente contenido y reinicie Docker Desktop.
```javascript
[wsl2]
memory=3072MB
processors=5
```

Encienda el clúster de Kubernetes de Minikube
```shell script
minikube start --memory=2816 --cpus=4
```

> **NOTA**: Desactive el filtro de autenticación en api-gateway-v1. Revise el README correspondiente para más información.

## 5.1. Construir imágenes
Las imágenes de nuestros servicios deben estar disponibles en el clúster de Kubernetes de Minikube, para ello 
establecemos el entorno de Docker de Minikube en nuestra shell y seguidamente construimos las imágenes en la misma
sesión de la shell.

Servicios de infraestructura:
```shell script
docker build -t miguelarmasabt/registry-discovery-server-v1:0.0.1-SNAPSHOT ./services/infrastructure-services/registry-discovery-server-v1
docker build -t miguelarmasabt/config-server-v1:0.0.1-SNAPSHOT ./services/infrastructure-services/config-server-v1
docker build -t miguelarmasabt/api-gateway-v1:0.0.1-SNAPSHOT ./services/infrastructure-services/api-gateway-v1
Invoke-Expression ((minikube docker-env) -join "`n")
```

Servicios de negocio:
```shell script
docker build -t miguelarmasabt/business-menu-option-v1:0.0.1-SNAPSHOT ./services/business-services/business-menu-option-v1
Invoke-Expression ((minikube docker-env) -join "`n")
```

A continuación, abrimos una shell de Minikube y revisamos que las imágenes hayan sido creadas.
```shell script
docker images
minikube ssh
```

## 5.2. Iniciar orquestación:
```shell script
kubectl apply -f ./devops/k8s/mysql_db/
kubectl apply -f ./devops/k8s/registry-discovery-server-v1/
kubectl apply -f ./devops/k8s/config-server-v1/
kubectl apply -f ./devops/k8s/api-gateway-v1/
kubectl apply -f ./devops/k8s/business-menu-option-v1/
```

Usted puede obtener la URL del servicio `api-gateway-v1` con el siguiente comando: `minikube service --url api-gateway-v1`

## 5.3. Eliminar orquestación:
```shell script
kubectl delete -f ./devops/k8s/mysql_db/
kubectl delete -f ./devops/k8s/registry-discovery-server-v1/
kubectl delete -f ./devops/k8s/config-server-v1/
kubectl delete -f ./devops/k8s/api-gateway-v1/
kubectl delete -f ./devops/k8s/business-menu-option-v1/
```

# 6. Conexion a base de datos
Utilice DBeaver para conectarse a las bases de datos relacionales.
## 6.1. MYSQL
| Parámetro         | Valor (Docker Compose)                         | Valor (Kubernetes)                                     |   
|-------------------|------------------------------------------------|--------------------------------------------------------|
| Server Host       | `localhost`                                    | `localhost`                                            |
| Port              | `3306`                                         | Puerto de Minikube: `minikube service --url bbq-mysql` |
| Database          | `db_menu_options?allowPublicKeyRetrieval=true` | `db_menu_options?allowPublicKeyRetrieval=true`         |
| Nombre de usuario | `root` o  `bbq_user`                           | `root` o  `bbq_user`                                   |
| Contraseña        | `qwerty`                                       | `qwerty`                                               |

## 6.2. PostgreSQL
- Ubique la pestaña `PostgreSQL` y active la opción `Show all database`.

| Parámetro         | Valor (Docker Compose)         | Valor (Kubernetes)                                        |   
|-------------------|--------------------------------|-----------------------------------------------------------|
| Connect by        | `HOST`                         | `HOST`                                                    |
| Host              | `localhost`                    | `localhost`                                               |
| Port              | `5432`                         | Puerto de Minikube: `minikube service --url bbq-postgres` |
| Database          | `db_dining_room_orders`        | `db_dining_room_orders`                                   |
| Nombre de usuario | `postgres` o  `bbq_user`       | `postgres` o  `bbq_user`                                  |
| Contraseña        | `qwerty`                       | `qwerty`                                                  |

# Puntos de mejora
> - [feat-0001] Automatizar la exportación e importación de la configuración de los pipelines de Jenkins
> - [feat-0002] Automatizar la exportación e importación de la configuración de Keycloak Server
> - [feat-0003] Refactorizar campo details de ApiException con patrón builder para lista
> - [feat-0004] Implementar pruebas unitarias en los servicios web de negocio
> - [feat-0005] Implementar SpringDoc en los servicios web de negocio
> - [feat-0006] Implementar frontend básico con Angular
> - [feat-0007] Agregar manifiesto de K8s para Keycloak Server y su adaptador de autenticación
> - [feat-0008] Aumentar el timeout para el consumo de los servicios clientes
> - [feat-0009] Deserealizar el objeto recuperado del topico invoice en el servicio business-payment-v1 e implementar los servicios REST
> - [feat-0010] Analizar los casos de uso y especificar los bounded contexts (diagrama)
> - [feat-0011] Implementar patrón strategy para seleccionar una implementación a partir de un selectorClass y uno o más flags
> - [feat-0012] Implementar anotaciones personalizadas
> - [feat-0013] Implementar Actuator para consultar propiedades y refrescarlas, aprovechando la anotación @RefreshScope 
> - [feat-0014] Integrar servicios de Quarkus con Spring Cloud (config-server-v1, registry-discovery-server-v1 y api-gateway-v1)
> - [feat-0015] Implementar un nuevo servicio con Go
> - [feat-0016] Integrar Kibana en el flujo de DevOps
> - [feat-0017] Revisar qué componentes utilizan una versión latest y asignarles una versión específica
> - [feat-0018] Modificar el timeout de las peticiones
