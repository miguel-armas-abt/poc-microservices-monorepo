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

> 🔨 **Construir imágenes**
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

> ▶️ **Aplicar manifiestos**
> <br> Iniciamos la orquestación aplicando los siguientes manifiestos.
> ```shell script 
> kubectl apply -f ./manifests/mysql-db/
> kubectl apply -f ./manifests/postgres-db/
> kubectl apply -f ./manifests/registry-discovery-server-v1/
> kubectl apply -f ./manifests/config-server-v1/
> kubectl apply -f ./manifests/api-gateway-v1/
> kubectl apply -f ./manifests/product-v1/
> kubectl apply -f ./manifests/menu-v1/
> kubectl apply -f ./manifests/menu-v2/
> kubectl apply -f ./manifests/table-placement-v1/
> ```

> 🔃 **Port forwarding**
> <br> Haciendo un port forward podremos acceder desde nuestro entorno local a los servicios disponibles en el clúster de Minikube.
> ```shell script 
> minikube service --url <service-name>
> ```
> 💡 **Nota**: Del mismo modo, si queremos probar conexión a las bases de datos, utilizaremos el puerto provisto en el comando anterior.

> ⏸️ **Eliminar manifiestos**
> <br> Finalizamos la orquestación eliminando los manifiestos creados previamente.
>  ```shell script 
> kubectl delete -f ./manifests/mysql-db/
> kubectl delete -f ./manifests/postgres-db/
> kubectl delete -f ./manifests/registry-discovery-server-v1/
> kubectl delete -f ./manifests/config-server-v1/
> kubectl delete -f ./manifests/api-gateway-v1/
> kubectl delete -f ./manifests/product-v1/
> kubectl delete -f ./manifests/menu-v1/
> kubectl delete -f ./manifests/menu-v2/
> kubectl delete -f ./manifests/table-placement-v1/
> ```

