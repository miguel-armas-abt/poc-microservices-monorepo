# API Gateway

## Proveedor de autenticación
Este componente se conecta con el adaptador de auntenticación para validar las peticiones entrantes al sistema. 

Usted puede deshabilitar la validación de los tokens de acceso, para ello desactive el filtro `AuthenticatorFiltering`
comentando todas las ocurrencias de la propiedad `spring.cloud.gateway.routes.<id>.filters.name.AuthenticatorFiltering`
en el archivo de propiedades.

## Docker Compose
En el paso anterior se actualizó el archivo de propiedades, así que es necesario recompilar las fuentes, actualizar la
imagen de Docker y recrear el contenedor.
```shell script
docker-compose -f ./../../../devops/docker-compose/docker-compose.yml up -d --force-recreate api-gateway-v1
docker build -t miguelarmasabt/api-gateway-v1:0.0.1-SNAPSHOT .
mvn clean install
```