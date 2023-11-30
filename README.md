# Despliegue
Esta guía le ayudará a configurar y desplegar los servicios de BBQ

- [1. Clonar repositorio](#1-clonar-repositorio)
- [2. Compilar codigo fuente](#2-compilar-codigo-fuente)
- [3. Despliegue local](#3-despliegue-local)
- [4. Orquestacion con Docker Compose](#4-orquestacion-con-docker-compose)
- [5. Orquestacion con Kubernetes](#5-orquestacion-con-kubernetes)
- [6. Conexion a las bases de datos](#6-conexion-a-base-de-datos)

# 1. Ramas
- `config-server`: Contiene los archivos de configuración de los servicios
- `feature/<feature-name>`: Contiene el código fuente en su versión de desarrollo
- `main`: Contiene la última versión estable del código fuente

# 2. Código fuente
El código fuente de los servicios web está en el directorio `/services`:

```javascript
    services
    ├───bbq-parent-v1
    ├───bbq-support-v1
    ├───business
    │   ├───menu-v1
    │   ├───table-placement-v1
    │   └─── ...
    └───infrastructure
        ├───api-gateway-v1
        ├───config-server-v1
        ├───registry-discovery-server-v1
        └─── ...
```


- `business`: Directorio que contiene los servicios web de negocio
- `infrastructure`: Directorio que contiene los servicios web de infraestructura
  - `bbq-parent-v1`: Proyecto de tipo `parent module` para servicios web implementados con Spring Boot
  - `bbq-support-v1`: Proyecto no ejecutable que centraliza las utilidades requeridas por los servicios web implementados con Spring Boot
  
Los puertos definidos para cada servicio web son los siguientes:

| Web service                   | Port   |
|-------------------------------|--------|
| registry-discovery-server-v1  | `8761` |
| config-server-v1              | `8888` |
| api-gateway-v1                | `8010` |
| auth-adapter-v1               | `8011` |
| menu-v1                       | `8012` |
| table-placement-v1            | `8013` |
| invoice-v1                    | `8014` |
| payment-v1                    | `8015` |
| menu-v2                       | `8016` |
| product-v1                    | `8017` |
| order-hub-v3                  | `8018` |

# 3. Despliegue local
- Compilar los proyectos `bbq-parent-v1` y `bbq-support-v1` antes que los demás
- Ejecutar los servicios de infraestructura indispensables
  - `registry-discovery-server-v1`
  - `config-server-v1`
  - `api-gateway-v1`
- Seguir las indicaciones de cada README

# 4. Orquestacion con Docker Compose
## 4.1. Construir imágenes
INFRAESTRUCTURA
```shell script
docker build -t miguelarmasabt/registry-discovery-server-v1:0.0.1-SNAPSHOT ./services/infrastructure/registry-discovery-server-v1
docker build -t miguelarmasabt/config-server-v1:0.0.1-SNAPSHOT ./services/infrastructure/config-server-v1
docker build -t miguelarmasabt/auth-adapter-v1:0.0.1-SNAPSHOT ./services/infrastructure/auth-adapter-v1
docker build -t miguelarmasabt/api-gateway-v1:0.0.1-SNAPSHOT ./services/infrastructure/api-gateway-v1
```

NEGOCIO
```shell script
docker build -t miguelarmasabt/product-v1:0.0.1-SNAPSHOT ./services/business/product-v1
docker build -t miguelarmasabt/menu-v1:0.0.1-SNAPSHOT ./services/business/menu-v1
docker build -f ./services/business/menu-v2/src/main/docker/Dockerfile.jvm -t miguelarmasabt/menu-v2:0.0.1-SNAPSHOT ./services/business/menu-v2
docker build -t miguelarmasabt/table-placement-v1:0.0.1-SNAPSHOT ./services/business/table-placement-v1
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

> **NOTA**: Desactive el filtro de autenticación para los servicios web que desplegará. Para ello siga las instrucciones
> en el README de api-gateway-v1

## 5.1. Construir imágenes
Las imágenes de nuestros servicios deben estar disponibles en el clúster de Kubernetes de Minikube. Para ello 
establecemos el entorno de Docker de Minikube en nuestra shell y sobre ella construimos las imágenes.

Servicios de infraestructura:
```shell script
docker build -t miguelarmasabt/registry-discovery-server-v1:0.0.1-SNAPSHOT ./services/infrastructure-services/registry-discovery-server-v1
docker build -t miguelarmasabt/config-server-v1:0.0.1-SNAPSHOT ./services/infrastructure-services/config-server-v1
docker build -t miguelarmasabt/api-gateway-v1:0.0.1-SNAPSHOT ./services/infrastructure-services/api-gateway-v1
Invoke-Expression ((minikube docker-env) -join "`n")
```

Servicios de negocio:
```shell script
docker build -t miguelarmasabt/menu-v1:0.0.1-SNAPSHOT ./services/business-services/menu-v1
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
kubectl apply -f ./devops/k8s/menu-v1/
```

Usted puede obtener la URL del servicio `api-gateway-v1` con el siguiente comando: `minikube service --url api-gateway-v1`

## 5.3. Eliminar orquestación:
```shell script
kubectl delete -f ./devops/k8s/mysql_db/
kubectl delete -f ./devops/k8s/registry-discovery-server-v1/
kubectl delete -f ./devops/k8s/config-server-v1/
kubectl delete -f ./devops/k8s/api-gateway-v1/
kubectl delete -f ./devops/k8s/menu-v1/
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

| Parámetro         | Valor (Docker Compose)     | Valor (Kubernetes)                                        |   
|-------------------|----------------------------|-----------------------------------------------------------|
| Connect by        | `HOST`                     | `HOST`                                                    |
| Host              | `localhost`                | `localhost`                                               |
| Port              | `5432`                     | Puerto de Minikube: `minikube service --url bbq-postgres` |
| Database          | `db_table_orders`          | `db_table_orders`                                         |
| Nombre de usuario | `postgres` o  `bbq_user`   | `postgres` o  `bbq_user`                                  |
| Contraseña        | `qwerty`                   | `qwerty`                                                  |
