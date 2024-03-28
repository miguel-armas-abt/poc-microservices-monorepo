> 📌 Utilice una shell compatible con Unix (PowerShell o Git bash) para ejecutar scripts `.sh`

# 1. CREAR DOCKER-COMPOSE

> ⚙️ **Actualizar variables de entorno**
> <br>Las variables de entorno y scripts de inicialización de BD para cada uno de los servicios están definidas en el siguiente directorio.
> ```shell script 
> cd ./../../environment
> ```

> ⚙️ **Actualizar parámetros de Docker Compose**
> <br>Los parámetros de configuración Docker Compose para cada uno de los servicios están definidos en el siguiente archivo `csv`.
> ```shell script 
> nano ./../parameters/docker-compose-parameters.csv #Linux
> notepad ./../parameters/docker-compose-parameters.csv #Windows
> ```
>
> 💡 **Notas**:
> - Puede utilizar `#` para comentar las líneas que desea ignorar.
> - El archivo `.csv` cuenta con las siguientes columnas.
>   - `APP_NAME`: Nombre del servicio.
>   - `DOCKER_IMAGE`: Imagen de Docker.
>   - `DEPENDENCIES`: Servicios de los que depende la ejecución del servicio. (separados por punto y coma `;`). Coloque `null` si es que no aplica.
>   - `HOST_PORT`: Puerto de escucha local.
>   - `CONTAINER_PORT`: Puerto del contenedor.
>   - `VOLUMES`: Volúmenes (separados por punto y coma `;`). Coloque `null` si es que no aplica.

> ▶️ **Crear / Actualizar Docker Compose**
> ```shell script 
> ./docker-compose-builder.sh
> ```

# 2. CONSTRUIR IMÁGENES

> ⚙️ **Actualizar las imágenes que desea construir**
> <br>Los parámetros para la construcción de imágenes están en el siguiente archivo `csv`.
> ```shell script 
> nano ./../../environment/images-to-build.csv #Linux
> notepad ./../../environment/images-to-build.csv #Windows
> ```
>
> 💡 **Notas**:
> - Puede utilizar `#` para comentar las líneas que desea ignorar.
> - El archivo `.csv` cuenta con las siguientes columnas.
>   - `APP_NAME`: Nombre del servicio sin la versión.
>   - `TAG_VERSION`: Tag de la imagen.
>   - `TYPE`: Tipo de servicio (`BS` o `INF`).
>   - `DOCKERFILE_PATH`: Ruta del Dockerfile. Si el archivo está en la raíz del proyecto utilizar `Default`.

> ▶️ **Construir imágenes**
> ```shell script 
> ./images-builder.sh
> ```
