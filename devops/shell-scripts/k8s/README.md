> 📌 Utilice una shell compatible con Unix e instale `yq` para ejecutar los scripts `.sh`

# KUBERNETES

[← Regresar](../../../README.md) <br>

## 📋 Pre requisitos
> ⚙️ **Instalar herramientas**<br>
> `Kubectl`, `Minikube`, `Helm`

## ▶️ Iniciar Minikube
```shell script 
  docker context use default
  minikube start
```

## ▶️ Menú de opciones
> El menú cuenta con las siguientes opciones:
> - Generar manifiestos
> - Construir imágenes en Minikube
> - Instalar objetos de k8s en el clúster
> - Desinstalar objetos de k8s del clúster
> - Port forwarding
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