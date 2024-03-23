# Despliegue con docker-compose

> 📋 **Pre requisitos**
> - Instalar e iniciar Docker.
> - **Opcional**. Para aumentar los recursos asignados a Docker Desktop, cree un archivo `.wslconfig` en la ruta
    > `C:\Users\<username>\`, agregue el siguiente contenido en dependencia de su entorno y reinicie Docker Desktop.
> ```javascript
> [wsl2]
> memory=3072MB
> processors=5
> ```

> 🔨 **Construir imágenes**
> ```shell script 
> docker build -t miguelarmasabt/config-server:v1.0.1 ./../../application/backend/infrastructure/config-server-v1
> docker build -t miguelarmasabt/auth-adapter:v1.0.1 ./../../application/backend/infrastructure/auth-adapter-v1
> docker build -t miguelarmasabt/api-gateway:v1.0.1 ./../../application/backend/infrastructure/api-gateway-v1
> docker build -t miguelarmasabt/product:v1.0.1 ./../../application/backend/business/product-v1
> docker build -t miguelarmasabt/menu:v1.0.1 ./../../application/backend/business/menu-v1
> docker build -f ./../../application/backend/business/menu-v2/src/main/docker/Dockerfile.jvm -t miguelarmasabt/menu:v2.0.1 ./../../application/backend/business/menu-v2
> docker build -t miguelarmasabt/table-placement:v1.0.1 ./../../application/backend/business/table-placement-v1
> ```

> 🔧 **Crear docker-compose.yml**
> <br>Utilice una shell compatible con Unix (PowerShell o Git bash)
> ```shell script
> cd ./shell-scripts
> ./docker-compose-builder.sh
> ```

> ▶️ **Iniciar orquestación**
> <br>Para forzar la recreación de los servicios utilice el flag `--force-recreate`
> ```shell script 
> docker-compose -f ./docker-compose.yml up -d
> ```

> ⏸️️ **Detener orquestación**
> <br>Para eliminar la orquestación utilice `down -v` en lugar de `stop`
> ```shell script 
> docker-compose -f ./docker-compose.yml stop
> ```
