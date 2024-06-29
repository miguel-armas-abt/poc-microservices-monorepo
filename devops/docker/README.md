> 📌 Puede utilizar `#` para comentar las líneas que desea ignorar.

# PARÁMETROS DOCKER

> ### Lista de contenedores
> 📄 `containers-to-run.csv`

| Cabecera         | Descripción                                                                |   
|------------------|----------------------------------------------------------------------------|
| `APP_NAME`       | Nombre del servicio.                                                       |
| `DOCKER_IMAGE`   | Imagen con tag.                                                            |
| `DEPENDENCIES`   | Servicios requeridos separados por `;`. En su ausencia especifique `null`. |
| `HOST_PORT`      | Puerto de escucha local.                                                   |
| `CONTAINER_PORT` | Puerto de escucha del contenedor.                                          |
| `VOLUMES`        | Volúmenes requeridos separados por `;`. En su ausencia especifique `null`. |

> ### Lista de imágenes
> 📄 `images-to-build.csv`

| Cabecera          | Descripción                                                                 |   
|-------------------|-----------------------------------------------------------------------------|
| `APP_NAME`        | Nombre del servicio sin la versión.                                         |
| `TAG_VERSION`     | Tag de la imagen.                                                           |
| `TYPE`            | Tipo de servicio (business: `BS`, infraestructura: `INF`).                  |
| `DOCKERFILE_PATH` | Ruta del Dockerfile. Si está en la raíz del proyecto, especifique `Default` |