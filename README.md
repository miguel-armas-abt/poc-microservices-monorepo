# Monorepo: Reactive BBQ Restaurant

- [1. Componentes de infraestructura](#1-componentes-de-infraestructura)
- [1.1. Admin Server](#1.1-admin-server)
- [1.2. Registry Discover Server](#12-registry-discovery-server)
- [1.3. Config Server](#13-config-server)
- [1.4. Api Gateway](#14-api-gateway)
- [1.5. Auth Adapter](#15-auth-adapter)
- [2. Componentes de soporte](#2-componentes-de-soporte)
- [2.1. BBQ Parent](#21-bbq-parent)
- [2.2. BBQ Support](#22-bbq-support)
- [3. Componentes de negocio](#3-componentes-de-negocio)
- [3.1. Business API Alternative Menu Option](#31-business-api-alternative-menu-option)
- [3.2. Business API Dining Room Order](#32-business-api-dining-room-order)
- [3.3. Business API Invoice](#33-business-api-invoice)
- [3.4. Business API Payment](#34-business-api-payment)


## 1. Componentes de infraestructura

### 1.1 Admin Server
> El servidor de administración permite conocer las características de interés de nuestros servicios. Por ejemplo,
> el número de instancias, la cantidad de memoria que están consumiendo, etc.
> 
> Puede acceder a la interfaz a través de: `http://localhost:8762/`

### 1.2. Registry Discovery Server
> El servidor de descubrimiento facilita la comunicación entre servicios mediante la invocación por nombres en lugar de
> URLs con su IPs y puertos (`http://127.0.0.1:<service-port>/products` => `http://product-service/products`). Es 
> importante en un escenario de escalamiento, ya que a medida que el ecosistema de servicios crezca, el problema de 
> gestión de IPs y puertos estáticos crecerá con él. Con un servidor de descubrimiento se espera un escalado dinámico 
> con instancias efímeras en dependencia de la carga de trabajo.
> 
> Puede acceder a la interfaz a través de: `http://localhost:8761/`

#### Consideraciones
> - Utilizar una versión de Spring Cloud compatible con la versión de Spring Boot
>   - `https://spring.io/projects/spring-cloud`
> - Podríamos tener errores de red al utilizar Docker Desktop
>   - `https://stackoverflow.com/questions/57319678/spring-boot-cloud-eurka-windows-10-eurkea-returns-host-docker-internal-for-clien`

### 1.3. Config Server
> El servidor de configuraciones centraliza las propiedades de los servicios en un repositorio de Git. Además permite
> separar las propiedades según el ambiente de ejecución (dev, qa, prod), ya que es posible que algunas propiedades 
> cambien entre un entorno y otro. Por ejemplo, contraseñas, URLs, IPs, constantes, etc. Para ello, basta indicar una 
> variable de entorno que indique el perfil de dev, qa o prod al ejecutar nuestro servicio.

#### Consideraciones
> - Los ficheros de propidades deben estar en la misma ruta que .git
> - Se debe considerar un prefijo y un sufijo en la nomenclatura de los archivos de propiedades. Por ejemplo:
>   - business-menu-option-dev
>   - experience-kitchen-order-prod
> - Se puede validar la conexión con los archivos de propiedades. Por ejemplo:
>   - `http://localhost:8888/business/menu-option-dev`
>   - `http://localhost:8888/experience/kitchen-order-prod`
> - Los clientes del config-server deben utilizar un bootstrap.yaml, el cual es primer el fichero de configuración que 
> SpringBoot llama al arrancar la aplicación

### 1.4. Api Gateway
> El API Gateway es una implementación de proxy inverso. Participa en un sistema distribudo como un componente 
> intermediario que simplifica la comunicación entre los distintos clientes y servicios del sistema enrutando las 
> peticiones desde un único punto de entrada. Algunas de sus responsabilidades son aplicar políticas de seguridad, 
> enrutamiento, monitorización del tráfico, escalabilidad, etc.

### 1.5. Auth Adapter
> Componente adaptador que conecta con un proveedor de autenticación y autorización (Keycloack que implementa Oauth2). 
> Su propósito es servir de habilitador para que el API Gateway aplique políticas de autenticación.
>
> De este modo, cuando la aplicación cliente quiera acceder a cualquier recurso de nuestra aplicación, primero debe
> solicitar un token (request access token) al provedoor de autenticación y enviarlo en la petición hacia el API Gateway
> a través de la cabecera Authorization. A continuación, el API Gateway utilizará el servicio adaptador para que se 
> conecte con el proveedor de auntenticación y este último valide el token de acceso. Finalmente, una vez que se haya
> verificado que el token es válido, el API Gateway redirige la petición al recurso que se esté solicitando.

## 2. Componentes de soporte
### 2.1. BBQ Parent
> Estructura de módulos que centraliza las propiedades, librerías y plugins para adoptar un estándar en todos los
> proyectos de BBQ. Los grupos de proyectos en Maven nos permiten tener una estructura de proyectos más coherente y
> ordenada, que pueden ser heredadas dentro de los proyectos hijos que conforman un grupo de proyectos, evitando así la
> redundancia.
>
> Tutorial de parent modules: `https://www.youtube.com/watch?v=XqC1zeFdxMs&ab_channel=CodeJava`

### 2.2. BBQ Support
> Proyecto no ejecutable que centraliza las funciones y constantes requeridas por los proyectos de BBQ.

## 3. Componentes de negocio
### Consideraciones
#### Swagger UI
> Para ver la documentación de los servicios en Swagger UI debe quitar la configuración relacionada al API Gateway. Sin 
> embargo, esto no es una práctica aconsejable en entornos productivos.
>
> Puede acceder a la interfaz a través de: `http://localhost:<service-port>/swagger/swagger-ui/index.html`

#### Base de datos en memoria H2
> Puede acceder a la interfaz a través de: `http://localhost:<service-port>/h2-console/`

### 3.1. Business API Alternative Menu Option
> Gesiona las opciones de menú que ofrece el restaurante.

| Endpoint                                            | Método | Descripción                                                                                       |
|-----------------------------------------------------|--------|---------------------------------------------------------------------------------------------------|
| `/bbq/business/v1/menu-options?category={category}` | GET    | Recupera todas las opciones de menú. Se filtra por categoría si se envía el query param category. |
| `/bbq/business/v1/menu-options/{id}`                | GET    | Recupera una opción de menú por id.                                                               |
| `/bbq/business/v1/menu-options`                     | POST   | Almacena una nueva opción de menú.                                                                |
| `/bbq/business/v1/menu-options/{id}`                | PUT    | Actualiza un registro de opción de menú.                                                          |
| `/bbq/business/v1/menu-options/{id}`                | DELETE | Elimina un registro de opción de menú.                                                            |

### 3.2. Business API Dining Room Order
> Gesiona las los pedidos que se realizan en el comedor.

| Endpoint                                                        | Método | Descripción                                    |
|-----------------------------------------------------------------|--------|------------------------------------------------|
| `/bbq/business/v1/dining-room-orders?tableNumber={tableNumber}` | GET    | Recupera los pedidos de una mesa.              |
| `/bbq/business/v1/dining-room-orders/{id}`                      | PATCH  | Agrega opciones de menú al pedido de una mesa. |

### 3.3. Business API Invoice
> Gesiona las facturas asociadas a los pedidos realizados en el comedor.

| Endpoint                                              | Método | Descripción                                         |
|-------------------------------------------------------|--------|-----------------------------------------------------|
| `/bbq/business/v1/invoices?tableNumber={tableNumber}` | GET    | Recupera la factura asociada al pedido de una mesa. |
| `/bbq/business/v1/invoices/send-to-pay`               | POST   | Envía a pagar la factura.                           |

### 3.4. Business API Payment
> Lista los pagos asociadas a los pedidos realizados en el comedor.

| Endpoint                      | Método | Descripción              |
|-------------------------------|--------|--------------------------|
| `/bbq/business/v1/payments`   | GET    | Recupera todos los pagos | 

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