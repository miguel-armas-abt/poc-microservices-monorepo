# JENKINS

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

> 📋️ **Login**
> <br>Auténtiquese en Jenkins con el token ubicado en los logs del contenedor
> ```shell script 
> docker logs bbq-jenkins
> ```

- Instale los plugins sugeridos: `Install suggested plugins`
- Cree una cuenta de administrador: (username=`bbq-user`, password=`qwerty`)
- Mantenga la URL por defecto: `http://localhost:8181/`

# 2. Crear pipelines
- Seleccione `+ Nueva Tarea > Folder` y cree las carpetas `business` e `infrastructure`. Aquí guardaremos nuestros pipelines.
- Ingrese a la carpeta `infrastructure`, seleccione `+ Nueva Tarea > Crear un proyecto de estilo libre`
- Ubique la sección `Configurar el origen del código fuente`, seleccione `Git` y configure los siguientes campos:
> - **Repository URL**: `https://miguel-armas-abt:<github-access-token>@github.com/miguel-armas-abt/demo-microservices-bbq`
> - **Branch to build**: `main`
- Ubique la sección `Build Steps`, presione el botón `Add build step`, seleccione la opción `Ejecutar línea de comandos (shell)` y agregue el siguiente script:
```shell script 
echo -e "Actual directory:"; ls
git branch
git checkout main
```
- Ubique la sección `Build Steps`, presione el botón `Add build step`, seleccione `Ejecutar tareas 'maven' de nivel superior` y configure los siguientes campos:
> - **Goals**: `clean install`
> - **POM**: `./application/backend/infrastructure/bbq-parent-v1/pom.xml`

# 3. Copiar pipelines
- Ubíquese en el folder en el que desea copiar su pipeline y seleccione `+ New Item`.
- Digite el nombre de su nuevo pipeline en el campo `Enter an item name`.
- Ubique la sección `Copy from`, digite el nombre del pipeline que copiará, selecciónelo y presiones `OK`.
- Ajustes las configuraciones revisadas durante la creación de un pipeline.

# 4. Integrar con DockerHub
- Seleccione `Panel de control > Administrar Jenkins > Plugins > Available plugins` e instale `CloudBees Docker Build and Publish`.
- Para que nuestro pipeline se conecte con un repositorio de Docker Hub, ubíquese en el pipeline y diríjase a la sección `Build Steps`.
- Presione el botón `Añadir un nuevo paso`, seleccione la opción `Docker Build and Publish` y configure los siguientes campos:
> - **Repositoy Name**: `miguelarmasabt/<repository>`
> - **Tag**: `0.0.1-SNAPSHOT`

> **Docker Desktop**
> - El siguiente campo requiere que expongamos el Daemon Docker del host. Para ello, ubíquese en Docker Desktop y diríjase a `Settings > General`.
> - Active la opción `Expose daemon on tcp://localhost:2375 without TLS` y reinicie Docker Desktop.
> - Valide que el Daemon Docker fue expuesto exitosamente accediendo a `http://localhost:2375/images/json`.

> **Docker Host URI**: `tcp://host.docker.internal:2375`

- Presione el botón `Avanzado...` y especifique el siguiente campo considerando el nombre del proyecto que está configurando:
> **Build Context**: `./application/backend/infrastructure/registry-discovery-server-v1/`

# 5. Integrar con Kubernetes
- Seleccione `Panel de control > Administrar Jenkins > Plugins > Available plugins` e instale `Kubernetes`.

> ▶️ Encienda el clúster de Minikube
> ```shell script
> minikube start
> ```

> 🔀 **Conectar Jenkins a la red de Minikube**
> <br>Utilice `disconnect` para desconectarse cuando lo requiera.
> ```shell script 
> docker network connect minikube bbq-jenkins
> ```

> 🔀 **Conceder privilegios de Kubernetes**
> <br>Conceda privilegios a Kubernetes y recupere el token de autenticación
> ```shell script 
> kubectl describe secret/jenkins-token-rk2mg
> kubectl apply -f ./k8s-jenkins/
> ```

- Seleccione la opción `Panel de control > Administrar Jenkins > Credentials` y presione `(global)`
- Presione el botón `+ Add Credentials`, configure los siguientes campos y acepte.
> - **Kind**: `Secret Text`
> - **Secret**: `<Token de autenticación k8s>`
> - **ID**: `k8s-cluster-token`

> 📋️ **Recuperar configuración del clúster**
> - **Certificado del clúster** `clusters.cluster.certificate-authority`:
>   - Ejemplo: `C:\Users\User\.minikube\ca.crt`
> - **API del clúster** `clusters.cluster.server`:
>   - Ejemplo: `https://127.0.0.1:52619`
> ```shell script 
> kubectl config view
> ```

> 🔀 **Port forwarding a la API del clúster**
> <br>Exponga la URL de la API del clúster hacia internet con ayuda de `ngrok` y reserve la URL pública.
> <br>Ejemplo de la URL pública recuperada: `https://f247-179-6-212-27.ngrok-free.app`
> ```shell script 
> ngrok http https://127.0.0.1:52619
> ```

- Seleccione la opción `Panel de control > Administrar Jenkins > Clouds > New cloud`
- Digite `bbq-kubernetes` en el campo `Cloud name`, seleccione la opción `Kubernetes` y de clic en el botón `Create`
- Presione el botón `Kubernetes Cloud details` y configure los siguientes campos:
> - **Kubernetes URL**: `<URL API del clúster>`
> - **Kubernetes server certificate key**: `<Certificado del cluster>`
> - **Disable https certificate check**: Habilitado
> - **Credentials**: `k8s-cluster-token`
- Guarde

> ⚠️ **Advertencia**: Si obtiene un error de autenticación, elimine y vuelva a crear la credencial
- Ingrese a la carpeta `infrastructure`, seleccione `+ Nueva Tarea > Pipeline` y configure los siguientes campos:
> - **General > GitHub project**: `https://github.com/miguel-armas-abt/demo-microservices-bbq`
> - **Pipeline > Definition**: `Pipeline script from SCM`
> - **SCM**: `Git`
> - **Repository URL**: `https://miguel-armas-abt:<github-access-token>@github.com/miguel-armas-abt/demo-microservices-bbq`
> - **Branch Specifier**: `*/main`
> - **Script Path**: `devops/jenkins/apps/registry-discovery-server-v1/Jenkinsfile`
- Ejecute el pipeline

