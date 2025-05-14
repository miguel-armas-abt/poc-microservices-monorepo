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
> 5. Port forwarding
> ```shell script 
> ./main.sh
> ```

## 💡 Consideraciones
> ⚙️ **Acceder a Minikube**<br>
> ```shell
>   minikube ssh
> ```
> Podemos acceder a Minikube para validar que las imágenes hayan sido construidas correctamente dentro del clúster.

> 🔃 **Por forwarding**<br>
> Desde el SGBD utilice la opción `allowPublicKeyRetrieval=true` para establecer las conexiones SQL.