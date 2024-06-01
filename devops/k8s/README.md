> 📌 Utilice una shell compatible con Unix (PowerShell o Git bash) para ejecutar scripts `.sh`

# DESPLIEGUE CON K8S

[← Regresar](./../../README.md)

> ✅ **Pre requisitos**
> - [Compilar proyectos](./../local/README.md)
> - Instalar Kubectl y Minikube
> - Iniciar el clúster de Minikube

🔨 **Construir imágenes en Minikube**
> Las imágenes deben estar disponibles en el clúster de Minikube. Para ello estableceremos el Docker de Minikube en
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

▶️ **Crear namespaces**
> ```shell script 
> kubectl create namespace restaurant
> ```

▶️ **Crear manifiestos**
> ```shell script 
> notepad ./parameters/k8s-app-manifests.csv
> ```
> ```shell script 
> notepad ./parameters/k8s-db-manifests.csv
> ```
> 
> ```shell script
> cd ./shell-scripts/manifests-builder
> ./k8s-manifests-builder.sh
> ```

▶️ **Aplicar manifiestos k8s**
> ```shell script 
> cd ./shell-scripts/k8s-operations
> ./apply-manifests.sh apply
> ```

🔃 **Port forwarding**
> - Haciendo un port forward podremos acceder desde nuestro entorno local a los services disponibles en el clúster de Kubernetes.
> - Desde su SGBD utilice la opción `allowPublicKeyRetrieval=true` para establecer las conexiones SQL.
> 
> ```shell script 
> cd ./shell-scripts/k8s-operations
> ./port-forward.sh
> ```

⏸️ **Eliminar recursos k8s**
> Finalizamos la orquestación eliminando los recursos creados previamente.
> ```shell script 
> cd ./shell-scripts/k8s-operations
> ./apply-manifests.sh delete
> ```