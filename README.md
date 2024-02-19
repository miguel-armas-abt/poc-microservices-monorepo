# DEMO MICROSERVICIOS BBQ

- [1. Caso de estudio](#1-caso-de-estudio)
- [2. Diseño del software](#2-disenio-del-software)
- [3. Gestión del repositorio](#3-gestion-del-repositorio)
- [4. Despliegue](#4-despliegue)

# 1. Caso de estudio
> 📑 **Resumen**
> - BBQ Restaurant, es un negocio de barbacoa tradicional que comenzó como una tienda familiar y que evolucionó hasta convertirse en una cadena global de restaurantes.
> - Su sistema de software actual se ha vuelto difícil de mantener debido a su naturaleza monolítica, lo que requiere períodos de inactividad para mantenimiento.
> - BBQ Restaurant planea una transición hacia una arquitectura de microservicios para mejorar la escalabilidad y la eficiencia operativa en su creciente red de restaurantes.

> 👥 **Expertos en el dominio**
> <br> Tras conversar con los expertos en el dominio "restaurante" se obtuvo que BBQ Restaurant utiliza los siguientes procesos para prestar servicios a sus clientes.
> - Atención en el comedor
> - Reserva en línea
> - Delivery

> ☑️ Atención en el comedor

| Mesero                                                | Chef de cocina                                                |
|-------------------------------------------------------|---------------------------------------------------------------|
| El mesero toma el pedido en el comedor                | El chef de cocina prepara un pedido                           |
| El mesero notifica al chef de cocina del nuevo pedido | El chef de cocina notifica al mesero que el pedido está listo |
| El mesero entrega el pedido en el comedor             | -                                                             |
| El mesero cobra el pago de un pedido en el comedor    | -                                                             |

> ☑️ Reserva en línea

| Cliente en línea                     | Anfitrión                                                         |
|--------------------------------------|-------------------------------------------------------------------|
| El cliente en línea hace una reserva | El anfitrión verifica la reserva y asienta al cliente con reserva |

El proceso continúa con la atención en el comedor.

> ☑️ Delivery

| Cliente en línea                                          | Chef de cocina                                                               | Conductor de delivery                           |
|-----------------------------------------------------------|------------------------------------------------------------------------------|-------------------------------------------------|
| El cliente en línea agrega elementos del menú a un pedido | El chef de cocina es notificado de un pedido para delivery                   | El conductor recoge un pedido en el restaurante |
| El cliente en línea realiza el pago de un pedido          | El chef de cocina prepara un pedido                                          | El conductor entrega un pedido al cliente       |
| -                                                         | El chef de cocina notifica al conductor de delivery que el pedido está listo | -                                               |

# 2. Disenio del software
> 📌 **Glosario**
> - `Dominio`. Área de conocimiento (conceptos, reglas, requisitos) que el software está destinado a abordar.

> 💡 **Notas**: 
<br>Por lo general, en una arquitectura de microservicios:
> - Cada `servicio web` aborda un `dominio` específico.

## 2.1. Arquitectura de software
![Texto alternativo](./docs/diagrams/software-architecture.jpg)

| Servicio web                   | Descripción                                                                                                                   | Puerto | Stack                                                         |   
|--------------------------------|-------------------------------------------------------------------------------------------------------------------------------|--------|---------------------------------------------------------------|
| `product-v1`                   | Permite gestionar los productos que ofrece el restaurante BBQ (CRUD).                                                         | 8017   | **GO**: `GORM`                                                |
| `menu-v1`                      | Permite gestionar las opciones de menú que ofrece el restaurante BBQ (CRUD), siendo las opciones de menú un tipo de producto. | 8012   | **Spring Boot**: `JPA, Retrofit`                              |
| `menu-v2`                      | Cumple el mismo propósito que menu-v1 (CRUD).                                                                                 | 8016   | **Quarkus**: `Panache Entity, RestClient, Multiny, GraphQL`   |
| `table-placement-v1`           | Permite realizar la colocación de la mesa, es decir que permite agregar pedidos en cada mesa y consultarlos.                  | 8013   | **Spring Boot**: `MongoDB Reactive, Webflux, RouterFunctions` |
| `invoice-v1`                   | Permite generar una factura de proforma y enviarla a pagar.                                                                   | 8014   | **Spring Boot**: `JPA, RxJava2, Retrofit, Kafka`              |
| `payment-v1`                   | Recibe las facturas y las procesa.                                                                                            | 8015   | **Spring Boot**: `JPA, Kafka`                                 |
| `order-hub-v1`                 | `Backend for Frontend` Construye la experiencia de generación de pedidos.                                                     | 8018   | **Spring Boot**: `Retrofit, Redis, Circuit Breaker`           |
| `registry-discovery-server-v1` | Servicio de registro y descubrimiento.                                                                                        | 8761   | **Spring Cloud**                                              |
| `config-server-v1`             | Servicio de configuraciones.                                                                                                  | 8888   | **Spring Cloud**                                              |
| `api-gateway-v1`               | API Gateway.                                                                                                                  | 8010   | **Spring Cloud**                                              |
| `auth-adapter-v1`              | Adaptador de autenticación.                                                                                                   | 8011   | **Spring Boot**: Retrofit                                     |

## 2.2. Arquitectura de paquetes
> 📌 **Glosario**
> - `Subdominio`. Área de conocimiento más específica dentro del dominio principal.
> - `Contexto`. Funcionalidad en el sistema que puede abarcar uno o más subdominios. Los contextos ayudan a delimitar las interacciones y responsabilidades entre los componentes del sistema.
> - `Modelo de datos`. Modelo que captura la estructura y el significado de los datos en un subdominio específico.

> 🔍 **Ejemplo**
> - **Dominio**: `Colocación de pedidos en mesa`
> - **Subdominios**: `Mesas` y `pedidos`
> - **Contextos**:
>   - `Colocación de pedidos`: Se encarga de tomar los pedidos de los clientes y asignarlos a una mesa específica.
>   - `Gestión de estado de mesa`: Controla el estado de ocupación de las mesas, indicando si están disponibles, ocupadas o reservadas.

> 💡 **Notas**:
<br>Por lo general, en una arquitectura de microservicios:
> - Los `subdominios` de cada servicio web son representados por los `modelos de datos` de sus fuentes de información.
> - Las `funcionalidades` de cada servicio web representan sus `contextos`.

```javascript
    application-name
    │───`infrastructure` // Receives the requests and handles the implementation details
    │   ├───rest
    │   │   ├───_ContextName_RestService.java // RestController or RouterFunction implementation
    │   │   └───_ContextName_Handler.java // Converts ServerRequest and ServerResponse to DTO
    │   ├───graphql
    │   │   └───_ContextName_GraphQLService.java
    │   ├───event
    │   │   ├───_EventName_Consumer.java
    │   │   └───_EventName_Producer.java
    │   └───exception.handler
    │       └───ApiExceptionHandler.java // Intercepts exceptions to show in HTTP response
    ├───`application` // Contributes with the domain logic and application logic
    │   ├───service
    │   │   ├───_ContextName_Service.java
    │   │   └───impl
    │   │       └───_ContextName_ServiceImpl.java
    │   ├───mapper
    │   │   └───_ContextName_Mapper.java
    │   ├───dto
    │   │   └───_context-name_
    │   │       ├───request
    │   │       │   └───_ContextName_Request.java
    │   │       └───response
    │   │           └───_ContextName_Response.java
    │   ├───enums
    │   │   └───_EnumName_Enum.java
    │   ├───exception
    │   │   └───_ApplicationName_Exception.java // Application specific exceptions
    │   └───aspect
    │       └───_cross-cutting-concern_
    │           └───_CrossCuttingConcern_Aspect.java // Cross-cutting concern aspect
    └───`domain` // Handles the domain data
        └───repository
            └───data-model-name
                ├───_DataModelName_Repository.java
                └───(entity | document | request | response)
                      └───_DataModelName_(Entity | Document | RequestWrapper | ResponseWrapper).java
```
> ✅ **Ventajas**: 
>   - Define una clara separación de responsabilidades. 
>   - Facilita a los desarrolladores seguir los fundamentos de Inversión de dependencias y clean architecture. "Un componente de una capa inferior no debe llamar a uno de una capa superior".

> ⚠️ **Desventajas**:
>   - En arquitecturas de microservicios, donde los servicios web tienden a ser más pequeños y menos complejos, esta estructura de paquetes podría introducir una complejidad innecesaria.

# 3. Gestion del repositorio

## 3.1. Ramas

> 💾 **Código fuente**
> - `main`: Contiene el código fuente del monorepo en su versión estable.
> - `feature/<feature-name>`: Contiene el código fuente del monorepo en su versión de desarrollo

> ⚙️ **Archivos de configuración**
> - `config-server`: Contiene los archivos de configuración en su versión estable.
> - `config-server/<feature-name>`: Contiene los archivos de configuración en su versión de desarrollo.

## 3.2. Estructura de carpetas

```javascript
    bbq-monorepo
    │───`application`
    │   ├───backend
    │   │   ├───business // web services containing business logic 
    │   │   │   ├───product-v1
    │   │   │   ├───menu-v1
    │   │   │   └─── ...
    │   │   └───infrastructure // infrastructure web services
    │   │       ├───api-gateway-v1
    │   │       ├───config-server-v1
    │   │       └─── ...
    │   └───frontend
    │       └───bbq-restaurant-web
    │───`devops`
    │   ├───docker-compose // to deploy in docker-compose
    │   ├───jenkins // to deploy in Jenkins
    │   ├───k8s // to deploy in k8s
    │   └───local // bash files to automate local deployment
    └───`docs`
        ├───diagrams // draw.io file diagrams
        └───postman // postman collection and environment
```

# 4. Despliegue

## 4.1. Despliegue local
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

> 📂 **Cambiar ruta a devops local**
> ```shell script 
> cd ./devops/local/
> ```

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

> 💾 **Insertar data**
> ```shell script 
> ./06_insert_data.bat
> ```


## 4.2. Despliegue con docker-compose

> 📋 **Pre condiciones**
> - Tener Docker iniciado.
> - **Opcional**. Para aumentar los recursos asignados a Docker Desktop, cree un archivo `.wslconfig` en la ruta
> `C:\Users\<username>\`, agregue el siguiente contenido en dependencia de su entorno y reinicie Docker Desktop.
> ```javascript
> [wsl2]
> memory=3072MB
> processors=5
> ```

> 🔨 **Construir imágenes**
> ```shell script 
> docker build -t miguelarmasabt/registry-discovery-server:v1.0.1 ./application/backend/infrastructure/registry-discovery-server-v1
> docker build -t miguelarmasabt/config-server:v1.0.1 ./application/backend/infrastructure/config-server-v1
> docker build -t miguelarmasabt/auth-adapter:v1.0.1 ./application/backend/infrastructure/auth-adapter-v1
> docker build -t miguelarmasabt/api-gateway:v1.0.1 ./application/backend/infrastructure/api-gateway-v1
> docker build -t miguelarmasabt/product:v1.0.1 ./application/backend/business/product-v1
> docker build -t miguelarmasabt/menu:v1.0.1 ./application/backend/business/menu-v1
> docker build -f ./application/backend/business/menu-v2/src/main/docker/Dockerfile.jvm -t miguelarmasabt/menu:v2.0.1 ./application/backend/business/menu-v2
> docker build -t miguelarmasabt/table-placement:v1.0.1 ./application/backend/business/table-placement-v1
> ```

> ▶️ **Iniciar orquestación**
> <br>Para forzar la recreación de los servicios utilice el flag `--force-recreate`
> ```shell script 
> docker-compose -f ./devops/docker-compose/docker-compose.yml up -d
> ```

> ⏸️️ **Detener orquestación**
> <br>Para eliminar la orquestación utilice `down -v` en lugar de `stop`
> ```shell script 
> docker-compose -f ./devops/docker-compose/docker-compose.yml stop
> ```

## 4.3. Despliegue con k8s

> 📋 **Pre condiciones**
> - Tener Minikube y Kubectl instalados.
> - Cambiar el contexto de la CLI de Docker (default)
> ```shell script
> docker context use default
> ```
> - Encender el clúster de Minikube. Puede especificar la cantidad de recursos con `--memory=2816 --cpus=4`
> ```shell script
> minikube start
> ```

> 🔨 **Construir imágenes**
> <br>Las imágenes deben estar disponibles en el clúster de Minikube y para ello estableceremos el Docker de Minikube en 
> nuestra línea de comandos y sobre ella construiremos las imágenes en el clúster de Minikube.
> ```shell script 
> docker build -t miguelarmasabt/product:v1.0.1 ./application/backend/business/product-v1
> docker build -t miguelarmasabt/menu:v1.0.1 ./application/backend/business/menu-v1
> docker build -f ./application/backend/business/menu-v2/src/main/docker/Dockerfile.jvm -t miguelarmasabt/menu:v2.0.1 ./application/backend/business/menu-v2
> docker build -t miguelarmasabt/table-placement:v1.0.1 ./application/backend/business/table-placement-v1
> docker build -t miguelarmasabt/registry-discovery-server:v1.0.1 ./application/backend/infrastructure/registry-discovery-server-v1
> docker build -t miguelarmasabt/config-server:v1.0.1 ./application/backend/infrastructure/config-server-v1
> docker build -t miguelarmasabt/auth-adapter:v1.0.1 ./application/backend/infrastructure/auth-adapter-v1
> docker build -t miguelarmasabt/api-gateway:v1.0.1 ./application/backend/infrastructure/api-gateway-v1
> Invoke-Expression ((minikube docker-env) -join "`n")
> ```
> A continuación, abrimos la shell de Minikube y revisamos que las imágenes hayan sido creadas.
> ```shell script
> docker images
> minikube ssh
> ```

> ▶️ **Aplicar manifiestos**
> <br> Iniciamos la orquestación aplicando los siguientes manifiestos.
> ```shell script 
> kubectl apply -f ./devops/k8s/mysql_db/
> kubectl apply -f ./devops/k8s/registry-discovery-server-v1/
> kubectl apply -f ./devops/k8s/config-server-v1/
> kubectl apply -f ./devops/k8s/api-gateway-v1/
> kubectl apply -f ./devops/k8s/product-v1/
> kubectl apply -f ./devops/k8s/menu-v1/
> kubectl apply -f ./devops/k8s/menu-v2/
> kubectl apply -f ./devops/k8s/postgres_db/
> kubectl apply -f ./devops/k8s/table-placement-v1/
> ```

> 🔃 **Port forwarding**
> <br> Haciendo un port forward podremos acceder desde nuestro entorno local a los servicios disponibles en el clúster de Minikube.
> ```shell script 
> minikube service --url <service-name>
> ```
> 💡 **Nota**: Del mismo modo, si queremos probar conexión a las bases de datos, utilizaremos el puerto provisto en el comando anterior.

> ⏸️ **Eliminar manifiestos**
> <br> Finalizamos la orquestación eliminando los manifiestos creados previamente.
> ```shell script 
> kubectl delete -f ./devops/k8s/mysql_db/
> kubectl delete -f ./devops/k8s/registry-discovery-server-v1/
> kubectl delete -f ./devops/k8s/config-server-v1/
> kubectl delete -f ./devops/k8s/api-gateway-v1/
> kubectl delete -f ./devops/k8s/product-v1/
> kubectl delete -f ./devops/k8s/menu-v1/
> kubectl delete -f ./devops/k8s/menu-v2/
> kubectl delete -f ./devops/k8s/postgres_db/
> kubectl delete -f ./devops/k8s/table-placement-v1/
> ```