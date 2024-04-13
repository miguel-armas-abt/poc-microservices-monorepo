> 📌 Utilice una shell compatible con Unix (PowerShell o Git bash) para ejecutar scripts `.sh`

# DESPLIEGUE CON K8S

> 📋 **Pre requisitos**
> - [Compilar proyectos](./../local/README.md)
> - Instalar Kubectl y Minikube
> - [Iniciar el clúster de Minikube - Anexos](#iniciar-el-cluster-de-minikube)

> 🔨 **Construir imágenes en Minikube**
> <br>Las imágenes deben estar disponibles en el clúster de Minikube. Para ello estableceremos el Docker de Minikube en
> nuestra línea de comandos y sobre ella construiremos las imágenes en el clúster de Minikube.
>
> ```shell script 
> cd ./../environment/docker/shell-scripts
> eval $(minikube docker-env --shell bash)
> ./images-builder.sh
> ```
> 
> Ingresar a la shell de Minikube:
> ```shell script
> minikube ssh
> ```
>
> A continuación, verificar que las imágenes hayan sido creadas
> ```shell script
> docker images
> ```

> 📄 **Editar archivo** - [Lista de manifiestos de tipo APP](./parameters/README.md)
> ```shell script 
> notepad ./parameters/k8s-app-manifests.csv
> ```
> 📄 **Editar archivo** - [Lista de manifiestos de tipo DB](./parameters/README.md)
> ```shell script 
> notepad ./parameters/k8s-db-manifests.csv
> ```
> ▶️ **Crear manifiestos**
> ```shell script
> cd ./shell-scripts/manifests-builder
> ./k8s-manifests-builder.sh
> ```

> ▶️ **Crear namespaces**
> ```shell script 
> kubectl create namespace restaurant
> ```

> ▶️ **Aplicar recursos k8s**
> <br> Iniciamos la orquestación aplicando los manifiestos creados previamente.
> ```shell script 
> cd ./shell-scripts/k8s-operations
> ./apply-manifests.sh apply
> ```

> 🔃 **Port forwarding**
> <br> Haciendo un port forward podremos acceder desde nuestro entorno local a los services disponibles en el clúster de Kubernetes.
>
> Utilice la opción `allowPublicKeyRetrieval=true` para establecer las conexiones SQL.
> 
> ```shell script 
> cd ./shell-scripts/k8s-operations
> ./port-forward.sh
> ```

> ⏸️ **Eliminar recursos k8s**
> <br> Finalizamos la orquestación eliminando los recursos creados previamente.
> ```shell script 
> cd ./shell-scripts/k8s-operations
> ./apply-manifests.sh delete
> ```

---

# Anexos

> ### Iniciar el cluster de Minikube
> - Para especificar los recursos asignados a Minukube, puede indicar `--memory=2816 --cpus=4`.
> - Utilice el contexto default de Docker.
> ```shell script
> docker context use default
> minikube start
> ```

> ### Acceder al Docker de Minikube
> Windows:
> ```shell script 
> Invoke-Expression ((minikube docker-env) -join "`n")
> ```
> Unix:
> ```shell script 
> eval $(minikube docker-env --shell bash)
> ```


