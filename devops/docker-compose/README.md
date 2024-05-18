> 📌 Utilice una shell compatible con Unix (PowerShell o Git bash) para ejecutar scripts `.sh`

# DESPLIEGUE CON DOCKER COMPOSE

> ✅ **Pre requisitos**
> - [Iniciar Docker](#configurar-docker-desktop)
> - [Compilar proyectos](./../local/README.md)

🔨 **Construir imágenes** - [Lista de imágenes a construir](./../environment/docker/README.md)
> ```shell script
> notepad ./../environment/docker/images-to-build.csv
> ```
> ```shell script 
> cd ./../environment/docker/shell-scripts
> ./images-builder.sh
> ```

⚙️ **Crear docker-compose.yml** - [Lista de contenedores a incluir en el docker-compose](./../environment/docker/README.md)
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

---

# Anexos

### Configurar Docker Desktop
> Para especificar los recursos asignados a Docker Desktop, cree un archivo `.wslconfig` en la ruta
> `C:\Users\<username>\`, agregue el siguiente contenido en dependencia de los recursos de su entorno y reinicie Docker Desktop.
> ```javascript
> [wsl2]
> memory=3072MB
> processors=5
> ```