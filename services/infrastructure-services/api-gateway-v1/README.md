# API Gateway
Este componente se conecta con el adaptador de auntenticación para validar las peticiones entrantes al sistema. 

## 1. Deshabilitar autenticación
Para que sus servicios de negocio ignoren la autenticación, desactive el filtro `AuthenticationFilter` correspondiente a 
su servicio web. Modifique el archivo de propiedades.

### 1.1. Docker Compose
Para deshabilitar la autenticación mientras el docker compose está en ejecución es necesario recompilar las fuentes, 
actualizar la imagen de Docker y recrear el contenedor.
```shell script
docker-compose -f ./../../../devops/docker-compose/docker-compose.yml up -d --force-recreate api-gateway-v1
docker build -t miguelarmasabt/api-gateway-v1:0.0.1-SNAPSHOT .
mvn clean install
```
