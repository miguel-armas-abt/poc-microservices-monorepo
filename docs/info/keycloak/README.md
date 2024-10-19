# Proveedor de autenticación Keycloak

[← Ir a Local](../../../devops/shell-scripts/local/README.md) <br>
[← Ir a Docker Compose](./../../../devops/docker-compose/README.md)

### ▶️ Despliegue local
Para desplegar Keycloak localmente, utilizaremos nuestro archivo de docker compose y especificaremos únicamente el servicio `keycloak-server`.  

```shell script
docker-compose -f ./../../../devops/docker-compose/docker-compose.yml up -d --force-recreate keycloak-server
```

### ⚙️ Configuración

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

### ⚙️ Actualización de la llave pública RS256
Después de haber configurado Keycloak debemos actualizar la llave pública RS256 en el componente `auth-adapter-v1` y 
desplegarlo nuevamente.

- **Local**: Actualizar el properties y reiniciar el servicio.
- **Docker Compose**: Actualizar la variable `KEYCLOAK_KEY_RS256` en el docker compose y reiniciar el servicio.

```shell script
docker-compose -f ./../../../devops/docker-compose/docker-compose.yml up -d --force-recreate auth-adapter-v1
```