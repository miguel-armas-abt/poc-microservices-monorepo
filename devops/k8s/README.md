> 📌 Utilice una shell compatible con Unix (PowerShell o Git bash) para ejecutar scripts `.sh`

# DESPLIEGUE CON K8S

> 📋 **Pre requisitos** ([Revisar anexo](#anexo))
> - Instalar Kubectl, Minikube e iniciar el clúster
> - Compilar proyectos

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

> 🔧 **Crear manifiestos**
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
> ```shell script 
> kubectl port-forward svc/<service-name> <local-port>:<pod-port> -n <namespace>
> ```

> ⏸️ **Eliminar recursos k8s**
> <br> Finalizamos la orquestación eliminando los recursos creados previamente.
> ```shell script 
> cd ./shell-scripts/k8s-operations
> ./apply-manifests.sh delete
> ```

---

# Anexo

> ### Minikube
> - Para especificar los recursos asignados a Minukube, puede indicar `--memory=2816 --cpus=4` al iniciar el clúster de Minikube.
> - Utilice el contexto default de Docker.
> ```shell script
> docker context use default
> minikube start
> ```

> ### Compilar proyectos
> ```shell script 
> cd ./../local/shell-scripts
> ./01_compile_projects.sh
> ```

> ### Acceder al Docker de Minikube
> ```shell script 
> eval $(minikube docker-env --shell bash) #Unix
> Invoke-Expression ((minikube docker-env) -join "`n") #Windows
> ```


