> 📌 Utilice una shell compatible con Unix (PowerShell o Git bash) para ejecutar scripts `.sh`

# JENKINS

[← Ir a Principal](./../../README.md)

**ToDo**: Los Jenkinsfiles deben pushear y recuperar las imágenes desde DockerHub.

> ✅ **Pre requisitos**
> - Iniciar el clúster de Minikube
> - [Construir imágenes en Minikube](./../k8s/README.md)
> - [Instalar Ngrok](https://github.com/miguel-armas-abt/technical-resources/blob/main/04_devops/ngrok/README.md)

# 1. Iniciar Jenkins
> 🔨 **Ejecutar contenedor de Jenkins**
> ```shell script 
> docker-compose -f docker-compose-cicd.yml up -d
> docker build -t miguelarmasabt/bbq-jenkins:v1 . --no-cache
> ```

> ⏸️️ **Detener orquestación**
> <br>Para eliminar la orquestación utilice `down -v` en lugar de `stop`
> ```shell script 
> docker-compose -f docker-compose-cicd.yml stop
> ```

- Abra el navegador en `http://localhost:8181`

> ✅ **Login**
> <br>Autentíquese en Jenkins con el token ubicado en los logs del contenedor
> ```shell script 
> docker logs bbq-jenkins
> ```

- Instale los plugins sugeridos: `Install suggested plugins`
- Cree una cuenta de administrador: (username=`bbq-user`, password=`qwerty`)
- Mantenga la URL por defecto: `http://localhost:8181/`

# 2. Integrar con Kubernetes
- Seleccione `Panel de control > Administrar Jenkins > Plugins > Available plugins` e instale `Kubernetes`.

> ⚠️ **Conectar Jenkins a la red de Minikube**
> <br>Desconéctelo antes de apagar Minikube, ya que podría tener problemas al encender el clúster la siguiente vez. 
> Para tal propósito utilice `disconnect`.
> ```shell script 
> docker network connect minikube bbq-jenkins
> ```

> 🔑 **Recuperar token de autenticación k8s**
> <br>Conceda privilegios a Kubernetes y reserve el token de autenticación k8s
> ```shell script 
> kubectl describe secret/jenkins-token-rk2mg
> kubectl apply -f ./deploy/k8s-authorization/
> ```

- **🔓 Creación de secreto - Token de autenticación k8s**
  - Seleccione la opción `Panel de control > Administrar Jenkins > Credentials` y presione `(global)`
  - Presione el botón `+ Add Credentials`, configure los siguientes campos y acepte.
> - **Kind**: `Secret Text`
> - **Secret**: `<Token de autenticación k8s>`
> - **ID**: `k8s-cluster-token`
> 
> ⚠️ Si durante la ejecución del pipeline obtiene un error de autenticación, elimine y cree nuevamente la credencial.

> ⚙️ **Recuperar configuración del clúster**
> ```shell script 
> kubectl config view
> ```
> - **Certificado k8s**: Reserve el valor de la propiedad `clusters.cluster.certificate-authority`. Por ejemplo, `C:\Users\User\.minikube\ca.crt` 
> - **URL pública k8s**: Ubique el valor de la propiedad `clusters.cluster.server`, por ejemplo, `https://127.0.0.1:52619` y 
> expóngalo hacia internet con ayuda de ngrok. Reserve la URL pública, por ejemplo, `https://f247-179-6-212-27.ngrok-free.app`
> ```shell script 
> ngrok http https://127.0.0.1:52619
> ```

- **🔧 Configurar conexión a Kubernetes**
  - Seleccione la opción `Panel de control > Administrar Jenkins > Clouds > New cloud`
  - Digite `bbq-kubernetes` en el campo `Cloud name`, seleccione la opción `Kubernetes` y de clic en el botón `Create`
  - Presione el botón `Kubernetes Cloud details`, configure los siguientes campos y guarde.
> - **Kubernetes URL**: `<URL pública k8s>`
> - **Kubernetes server certificate key**: `<Certificado k8s>`
> - **Disable https certificate check**: Habilitado
> - **Credentials**: `k8s-cluster-token`

# 3. Crear Jenkinsfiles
> - El script solicitará la URL pública k8s
> ```shell script
> ./shell-scripts/jenkinsfiles-builder.sh
> ```
- Suba el commit al repositorio remoto.

# 4. Crear pipeline
- 📂 Cree una estructura de carpetas conveniente para la organización de sus pipelines.
- Ingrese a su carpeta, seleccione `+ Nueva Tarea > Pipeline` y configure los siguientes campos:
> - **General > GitHub project**: `https://github.com/miguel-armas-abt/demo-microservices-bbq`
> - **Pipeline > Definition**: `Pipeline script from SCM`
> - **SCM**: `Git`
> - **Repository URL**: `https://miguel-armas-abt:<github-access-token>@github.com/miguel-armas-abt/demo-microservices-bbq`
> - **Branch Specifier**: `*/main`
> - **Script Path**: `devops/jenkins/jenkinsfiles/<app-name>/Jenkinsfile`
- ▶️ Ejecute el pipeline.

# 5. Copiar pipelines
- Ubíquese en el folder en el que desea copiar su pipeline y seleccione `+ New Item`.
- Digite el nombre de su nuevo pipeline en el campo `Enter an item name`.
- Ubique la sección `Copy from`, digite el nombre del pipeline que copiará, selecciónelo y presiones `OK`.
- Ajuste las configuraciones revisadas durante la creación de un pipeline.