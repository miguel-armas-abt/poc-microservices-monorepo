> 📌 Utilice una shell compatible con Unix (PowerShell o Git bash) para ejecutar scripts `.sh`

# DESPLIEGUE CON DOCKER COMPOSE

> 📋 **Pre requisitos** ([Revisar anexo](#anexo))
> - Instalar e iniciar Docker
> - Compilar proyectos

> 🔨 **Construir imágenes**
> <br>📄 Edite `./../environment/docker/images-to-build.csv` con los proyectos que desea compilar e incluir en el `docker-compose.yml`.
> ```shell script 
> cd ./../environment/docker/shell-scripts
> ./images-builder.sh
> ```

> 🔧 **Crear docker-compose.yml**
> ```shell script
> cd ./shell-scripts
> ./docker-compose-builder.sh
> ```

> ▶️ **Iniciar orquestación**
> <br>Para forzar la recreación de los servicios utilice el flag `--force-recreate`
> ```shell script 
> docker-compose -f ./docker-compose.yml up -d
> ```

> ⏸️️ **Eliminar orquestación**
> <br>Para detener la orquestación utilice `stop` en lugar de `down -v`
> ```shell script 
> docker-compose -f ./docker-compose.yml down -v
> ```

---

# Anexo

> ### Docker Desktop
> Para especificar los recursos asignados a Docker Desktop, cree un archivo `.wslconfig` en la ruta
> `C:\Users\<username>\`, agregue el siguiente contenido en dependencia de los recursos de su entorno y reinicie Docker Desktop.
> ```javascript
> [wsl2]
> memory=3072MB
> processors=5
> ```

> ### Compilar proyectos
> ```shell script 
> cd ./../local/shell-scripts
> ./01_compile_projects.sh
> ```