> 📌 Utilice una shell compatible con Unix (PowerShell o Git bash) para ejecutar scripts `.sh`

# CONSTRUIR IMÁGENES

> ⚙️ **Actualizar las imágenes que desea construir**
> <br>Los parámetros para la construcción de imágenes están en el siguiente archivo `csv`.
> <br><br> Utilice `nano` para Unix
> ```shell script 
> notepad ./images-to-build.csv #Windows
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
> ./shell-scripts/images-builder.sh
> ```
