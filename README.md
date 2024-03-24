# DEMO MICROSERVICIOS BBQ

- [1. Caso de estudio](#1-caso-de-estudio)
- [2. Diseño del software](#2-disenio-del-software)
- [3. Gestión del repositorio](#3-gestion-del-repositorio)
- [4. Despliegue](#4-despliegue)

# 1. Caso de estudio
BBQ Restaurant es una cadena global de restaurantes que planea implementar de arquitectura de microservicios para mejorar la escalabilidad y la eficiencia operativa en su creciente red de restaurantes.

Los expertos en el dominio "restaurante" utilizan los siguientes procesos para prestar servicios a sus clientes.
- Atención en el comedor
- Reserva en línea
- Delivery

> 📝 Atención en el comedor
>
![Proceso de atención en el restaurante](./docs/diagrams/restaurant-process.jpg)

> 📝 Reserva en línea

![Proceso de reserva](./docs/diagrams/reservation-process.jpg)


> 📝 Delivery

![Proceso de delivery](./docs/diagrams/delivery-process.jpg)

# 2. Disenio del software
> 📌 **Glosario**
> - **Dominio**: Área de conocimiento (conceptos, reglas, requisitos) que el software está destinado a abordar. Por lo general, cada `servicio web` aborda un `dominio` específico.

## 2.1. Arquitectura de software
![Arquitectura de software](./docs/diagrams/software-architecture.jpg)

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
> - **Subdominio**: Área de conocimiento más específica dentro del dominio principal. Por lo general, los `subdominios` de cada servicio web son representados por los `modelos de datos` de sus fuentes de información.
> - **Modelo de datos**: Modelo que captura la estructura y el significado de los datos en un subdominio específico.
> - **Contexto**: Funcionalidad en el sistema que puede abarcar uno o más subdominios. Los contextos ayudan a delimitar las responsabilidades entre los componentes del sistema.
>
> 🔍 **Ejemplo**
> - **Dominio**: `Colocación de pedidos en mesa`
> - **Subdominios**: `Mesas` y `pedidos`
> - **Contextos**:
>   - `Colocación de pedidos`: Se encarga de tomar los pedidos de los clientes y asignarlos a una mesa específica.
>   - `Gestión de estado de mesa`: Controla el estado de ocupación de las mesas, indicando si están disponibles, ocupadas o reservadas.

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
>   - Facilita a los desarrolladores seguir los principios de Inversión de dependencias y clean architecture. "Un componente de una capa inferior no debe llamar a uno de una capa superior".
>
> ⚠️ **Desventajas**:
>   - En arquitecturas de microservicios, donde los servicios web tienden a ser menos complejos, esta estructura de paquetes podría introducir una complejidad adicional.

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
    │   └───local // to deploy in local
    └───`docs`
        ├───diagrams // draw.io file diagrams
        └───postman // postman collection and environment
```

# 4. Despliegue
Revise las instrucciones de despliegue para cada uno de los siguientes entornos.
- Local: `devops/local/README.md`
- Docker Compose: `devops/docker-compose/README.md`
- Kubernetes: `devops/k8s/README.md`
- Jenkins: `devops/jenkins/README.md`

