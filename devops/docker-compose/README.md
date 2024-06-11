> 📌 Utilice una shell compatible con Unix (PowerShell o Git bash) para ejecutar scripts `.sh`

# DESPLIEGUE CON DOCKER COMPOSE

[← Regresar Principal](./../../README.md)

> ✅ **Pre requisitos**
> - Iniciar Docker
> - [Compilar proyectos](./../local/README.md)

🔨 **Construir imágenes**
> ```shell script
> notepad ./../environment/docker/images-to-build.csv
> ```
> ```shell script 
> cd ./../environment/docker/shell-scripts
> ./images-builder.sh
> ```

⚙️ **Crear docker-compose.yml**
> ```shell script
> notepad ./../environment/docker/containers-to-run.csv
> ```
> ```shell script
> cd ./shell-scripts
> ./docker-compose-builder.sh
> ```

▶️ **Iniciar orquestación**
> Para forzar la recreación de los servicios utilice el flag `--force-recreate`
> ```shell script 
> docker-compose -f ./docker-compose.yml up -d
> ```

⏸️️ **Eliminar orquestación**
> Para detener la orquestación utilice `stop` en lugar de `down -v`
> ```shell script 
> docker-compose -f ./docker-compose.yml down -v
> ```

▶️ [Configurar Keycloak](./../../docs/info/keycloak/README.md)