# Despliegue con docker-compose

> 📋 **Pre requisitos**
> - Instalar e iniciar Docker ([Revisar anexo](#anexo))

> 🔨 **Construir imágenes**
> ```shell script 
> cd ./shell-scripts
> ./images-builder.sh
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

> ⏸️️ **Eliminar orquestación**
> <br>Para detener la orquestación utilice `stop` en lugar de `down -v`
> ```shell script 
> docker-compose -f ./docker-compose.yml down -v
> ```

---

# Anexo

> ### Docker Desktop
> Para aumentar los recursos asignados a Docker Desktop, cree un archivo `.wslconfig` en la ruta
> `C:\Users\<username>\`, agregue el siguiente contenido en dependencia de los recursos de su entorno y reinicie Docker Desktop.
> ```javascript
> [wsl2]
> memory=3072MB
> processors=5
> ```
