# Adaptador de autenticación
Este componente conecta con un proveedor de autenticación SS0 y Oauth2 (Keycloak), y tiene como propósito validar los
tokens de acceso.

# Despliegue (Docker Compose)
## 1. keycloak-server
### 1.1. Despliegue aislado (Opcional)
**[AMBIENTE LOCAL]** Si usted desea desplegar únicamente el servicio de keycloak para tenerlo disponible en su ambiente local, ejecute el 
siguiente comando.

```shell script
docker-compose -f ./../../../../devops/docker-compose/docker-compose.yml up -d --force-recreate keycloak-server
```

### 1.2. Configuración
Acceda a la web `http://localhost:8091`, seleccione la opción 
`Administration Console` e inicie sesión con las credenciales (username=admin, password=admin).

Configure las siguientes propiedades.
> Cree un nuevo realm y asígnele el nombre `bbq-management`
> 
> **Realm Settings**: Seleccion el tab `Keys` y ubique la llave pública `RS256`. A continuación, reemplace la variable 
> `KEYCLOACK_KEY_RS256` en el `docker-compose.yml`.
> 
> **Realm Settings**: Seleccion el tab `Tokens`, ubique la propiedad `Access Token Lifespan` y cambie el tiempo de
> expiración a 30'.
> 
> **User**: Cree un nuevo user con username=`admin`, presione el botón `save`. A continuación, seleccione el tab 
> `Credentials` y asigne password=`admin` y temporary=`off`
>
> **Roles**: Cree un nuevo rol (rolename=`partners`)
> 
> **User**: Seleccion el tab `Role Mappings` y agregue el rol `partners`
> 
> **Clients**: Cree un nuevo cliente (clientid=`front-bbq-app`, client-protocol=`openid-connect`)
> 
> **Clients**: Ubique la propiedad y actualícela `Valid Redirect URIs`=`*`

## 2. auth-adapter-v1
Si previamente orquestó todos los servicios del docker compose, entonces debe reiniciar el servicio `auth-adapter-v1`
para que tengas las credenciales actualizadas.

```shell script
docker-compose -f ./../../../devops/docker-compose/docker-compose.yml up -d --force-recreate auth-adapter-v1
```
