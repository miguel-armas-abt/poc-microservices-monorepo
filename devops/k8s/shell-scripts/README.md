> 📌 Utilice una shell compatible con Unix (PowerShell o Git bash) para ejecutar scripts `.sh`

# CREAR MANIFIESTOS K8S

El proyecto cuenta con un script que automatiza la generación de manifiestos de k8s.

> ⚙️ **Actualizar las variables de entorno**
> <br>Las variables de entorno y scripts de inicialización de BD para cada uno de los servicios están definidas en el siguiente directorio.
> ```shell script 
> cd ./../../environment
> ```

> ⚙️ **Actualizar parámetros de k8s**
> <br>Los parámetros de configuración k8s para cada uno de los servicios están definidos en los siguientes archivos `csv`.
> <br><br> Utilice `nano` para Unix
> ```shell script 
> #Windows
> notepad ./../parameters/k8s-app-parameters.csv
> notepad ./../parameters/databases/k8s-db-parameters.csv
> ```
>
> 💡 **Notas**:
> - Puede utilizar `#` para comentar las líneas que desea ignorar.
> - El archivo `k8s-app-parameters.csv` cuenta con las siguientes columnas.
>   - `APP_NAME`: Nombre del servicio.
>   - `PORT`: Puerto del contenedor.
>   - `NODE_PORT`: Puerto del nodo.
>   - `IMAGE`: Imagen de Docker.
>   - `CONTROLLER_TYPE`: Tipo de controlador (`STS`: Statefulset, `DEP`: Deployment).
> - El archivo `k8s-db-parameters.csv` cuenta con las siguientes columnas.
>   - `APP_NAME`: Nombre del servicio.
>   - `CONTAINER_NAME`: Nombre del contenedor.
>   - `IMAGE`: Imagen de Docker.
>   - `PORT`: Puerto del contenedor.
>   - `SUB_PATH_INIT_DB`: Nombre del fichero de inicialización de BD.
>   - `CLUSTER_IP`: IP del Clúster para aceptar las peticiones.
>   - `NODE_PORT`: Puerto del nodo.
>   - `CONTROLLER_TYPE`: Tipo de controlador (`STS`: Statefulset, `DEP`: Deployment).


> ▶️ **Crear Manifiestos de k8s**
> ```shell script 
> ./k8s-manifests-builder.sh
> ```
