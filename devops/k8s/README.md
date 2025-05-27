> 📌 Utilice una shell compatible con Unix e instale `yq` para ejecutar los scripts `.sh`

# KUBERNETES

[← Regresar](../../README.md) <br>

## 📋 Prerrequisitos
> ⚙️ **Instalar herramientas**<br>
> `Kubectl`, `Minikube`, `Helm`

## ▶️ Iniciar Minikube
```shell script 
  docker context use default
  minikube start
```

## ▶️ Menú de opciones
> 1. Generar manifiestos
> 2. Construir imágenes en Minikube
> 3. Instalar objetos k8s
> 4. Desinstalar objetos k8s
> ```shell script 
> ./main.sh
> ```

## ▶️ Habilitar Ingress Controller en Minikube
```shell script 
  minikube addons enable ingress
  kubectl get pods -n ingress-nginx
  kubectl apply -f ingress.yaml
  minikube tunnel #ingress accesible en 127.0.0.1
```
📋 **Nota**: Para que `com.api.dev` resuelva a tu Minikube, añade esta línea en tu `/etc/hosts`.
```
127.0.0.1 poc.api.dev
```

## 💡 Consideraciones
> ⚙️ Edite el secreto de `KEYCLOAK_KEY_RS256` de `auth-adapter-v1`:
> ```shell
> kubectl edit secret auth-adapter-v1 -n restaurant
> ```

> ⚙️ **Acceder a Minikube**<br>
> ```shell
>   minikube ssh
> ```
> Podemos acceder a Minikube para validar que las imágenes hayan sido construidas correctamente dentro del clúster.

> 🔃 **Por forwarding**<br>
> Desde el SGBD utilice la opción `allowPublicKeyRetrieval=true` para establecer las conexiones SQL.