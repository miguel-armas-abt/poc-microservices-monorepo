# Despliegue con k8s

> 📋 **Pre requisitos**
> - Instalar Minikube y Kubectl.
> - Cambiar el contexto de la CLI de Docker (default)
> ```shell script
> docker context use default
> ```
> - Encender el clúster de Minikube. Puede especificar la cantidad de recursos con `--memory=2816 --cpus=4`
> ```shell script
> minikube start
> ```
> - 🔨 Compilar los proyectos
> ```shell script 
> ./01_install_services.bat
> cd ./../local/
> ```

> 🔨 **Construir imágenes en Minikube**
> <br>Las imágenes deben estar disponibles en el clúster de Minikube. Para ello estableceremos el Docker de Minikube en
> nuestra línea de comandos y sobre ella construiremos las imágenes en el clúster de Minikube.
> ```shell script 
> docker build -t miguelarmasabt/product:v1.0.1 ./../../application/backend/business/product-v1
> docker build -t miguelarmasabt/menu:v1.0.1 ./../../application/backend/business/menu-v1
> docker build -f ./../../application/backend/business/menu-v2/src/main/docker/Dockerfile.jvm -t miguelarmasabt/menu:v2.0.1 ./../../application/backend/business/menu-v2
> docker build -t miguelarmasabt/table-placement:v1.0.1 ./../../application/backend/business/table-placement-v1
> docker build -t miguelarmasabt/registry-discovery-server:v1.0.1 ./../../application/backend/infrastructure/registry-discovery-server-v1
> docker build -t miguelarmasabt/config-server:v1.0.1 ./../../application/backend/infrastructure/config-server-v1
> docker build -t miguelarmasabt/auth-adapter:v1.0.1 ./../../application/backend/infrastructure/auth-adapter-v1
> docker build -t miguelarmasabt/api-gateway:v1.0.1 ./../../application/backend/infrastructure/api-gateway-v1
> Invoke-Expression ((minikube docker-env) -join "`n")
> ```
> A continuación, abrimos la shell de Minikube y revisamos que las imágenes hayan sido creadas.
> ```shell script
> docker images
> minikube ssh
> ```

> 🔧 **Crear manifiestos**
> <br>Utilice una shell compatible con Unix (PowerShell o Git bash)
> ```shell script
> ./k8s-manifests-builder.sh
> ```

> ▶️ **Crear namespaces**
> ```shell script 
> kubectl create namespace restaurant
> ```

> ▶️ **Crear recursos k8s**
> <br> Iniciamos la orquestación aplicando los siguientes manifiestos.
> ```shell script 
> kubectl apply -f ./manifests/mongo-db/ -n restaurant
> kubectl apply -f ./manifests/mysql-db/ -n restaurant
> kubectl apply -f ./manifests/postgres-db/ -n restaurant
> kubectl apply -f ./manifests/registry-discovery-server-v1/ -n restaurant
> kubectl apply -f ./manifests/config-server-v1/ -n restaurant
> kubectl apply -f ./manifests/api-gateway-v1/ -n restaurant
> kubectl apply -f ./manifests/product-v1/ -n restaurant
> kubectl apply -f ./manifests/menu-v1/ -n restaurant
> kubectl apply -f ./manifests/menu-v2/ -n restaurant
> kubectl apply -f ./manifests/table-placement-v1/ -n restaurant
> ```

> 🔃 **Port forwarding**
> <br> Haciendo un port forward podremos acceder desde nuestro entorno local a los services disponibles en el clúster de Kubernetes.
>
> ```shell script 
> kubectl port-forward svc/<service-name> <local-port>:<pod-port> -n <namespace>
> ```

> ⏸️ **Eliminar recursos k8s**
> <br> Finalizamos la orquestación eliminando los recursos creados previamente.
>  ```shell script 
> kubectl delete -f ./manifests/mongo-db/ -n restaurant
> kubectl delete -f ./manifests/mysql-db/ -n restaurant
> kubectl delete -f ./manifests/postgres-db/ -n restaurant
> kubectl delete -f ./manifests/registry-discovery-server-v1/ -n restaurant
> kubectl delete -f ./manifests/config-server-v1/ -n restaurant
> kubectl delete -f ./manifests/api-gateway-v1/ -n restaurant
> kubectl delete -f ./manifests/product-v1/ -n restaurant
> kubectl delete -f ./manifests/menu-v1/ -n restaurant
> kubectl delete -f ./manifests/menu-v2/ -n restaurant
> kubectl delete -f ./manifests/table-placement-v1/ -n restaurant
> ```

