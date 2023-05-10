# Jenkins
Esta guía le ayudará a configurar Jenkins e integrarlo con otras herramientas de DevOps.

- [1. Crear imagen y ejecutar contenedor](#1-crear-imagen-y-ejecutar-contenedor)
- [2. Crear pipeline de estilo libre](#2-crear-pipeline-de-estilo-libre)
- [3. Integrar con GitHub Webhooks](#3-integrar-con-github-webhooks)
- [4. Integrar con Slack](#4-integrar-con-slack)
- [5. Integrar con SonarQube](#5-integrar-con-sonarqube)
- [6. Integrar con DockerHub](#6-integrar-con-dockerhub)
- [7. Integrar con Kubernetes](#7-integrar-con-kubernetes)

# 1. Crear imagen y ejecutar contenedor
```shell script
docker-compose -f ./devops/jenkins/docker-compose-cicd.yml up -d --force-recreate
docker build -t miguelarmasabt/bbq-jenkins:v1 --no-cache ./devops/jenkins
```
Revise los logs del contenedor y copie el token en la pantalla de login `localhost:8080`.
```shell script
docker logs bbq-jenkins
```
A continuación, instale los plugins sugeridos `Install suggested plugins`, cree una cuenta de administrador
(username=`bbq-user`, password=`qwerty`) y mantenga la URL por defecto `http://localhost:8080/`.

# 2. Crear pipeline de estilo libre


# 3. Integrar con GitHub Webhooks
## 3.1. Reservar URI del reverse proxy
Para conectar Jenkins con GitHub Webhook, nuestra instancia de Jenkins debe ser accesible desde internet mediante un
reverse proxy. Para ello utilice una versión estable de `ngrok` y ejecute los siguientes comandos para exponer el puerto
`8080` hacia internet.

```shell script
ngrok http 8080
ngrok config add-authtoken <ngrok-authtoken>
cd <ngrok-directory>
```
Finalmente reserve el valor del campo `Forwarding`. Por ejemplo, `https://95ee-179-6-212-27.ngrok-free.app` .

## 3.2. Configurar repositorio
Para notificar a nuestra instancia de Jenkis sobre los eventos que se producen en el repositorio, ubíquise en GitHub y
diríajse a `Repository > Settigs > Webhooks`. A continuación configure los siguientes campos.

> **Payload URL**: `<URI del reverse proxy>/github-webhook/`
>
> **Content type**: `application/json`
>
> **Which events would you like to trigger this webhook?**: `Just push event`

## 3.3. Conectar Jenkins con GitHub Server
Ubíquese en Jenkins y diríjase a `Panel de Control > Administrar Jenkins > System > GitHub`. A continuación, seleccione
la opción `Add GitHub Server` y configure los siguientes campos.

> **Name**: `miguel-armas-abt-server`
>
> **API URL**: `https://api.github.com`

Genere un token clásico de GitHub para el siguiente campo de tipo `Secret text`. Para ello, ubíquise en GitHub,
diríjase a `Account > Settings > Developer settings > Personal access tokens` y cree un nuevo token con acceso a `repo`.

> **Credentials**: `<github-classic-token>`

Finalmente, para que nuestro pipeline se ejecute automáticamente cuando escuchen algún evento en el repositorio,
ubíquese en el pipeline y diríjase a la sección `Disparadores de ejecución` y seleccione la opción `GitHub hook trigger for GITScm polling`.

# 4. Integrar con Slack
Cree un workspace en `Slack`, instale el plugin `Jenkins CI` y reserve los datos de configuración para integrar su
workspace con Jenkins.

Ubíquese en Jenkins e instale el plugin `Slack Notification`. A continuación, diríjase a
`Panel de control > Administrar Jenkins > System > Slack` y configure los siguientes campos.

> **Workspace** (dato provisto por Slack): `<Subdominio de equipo>`
>
> **Credential**: (dato provisto por Slack) `<ID de credencial de token de integración>`

Finalmente, para que nuestro pipeline envíe notificaciones a Slack, ubíquese en el pipeline y diríjase a la sección
`Acciones para ejecutar después`, seleccione la opción `Slack Notifications` y marque todas las casillas.

# 5. Integrar con SonarQube
Ingrese a la web de SonarQube `localhost:9000` e ingrese con las credenciales por default (username=`admin` y password=`admin`).
Dentro de SonarQube diríjase a `Administration > Security > Users`, ubique la columna `Tokens`, de clic en el botón
`Update tokens` y genere un token sin tiempo de expiración.

Ubíquese en Jenkins e instale el plugin `SonarQube Scanner`. A continuación, diríjase a
`Panel de control > Administrar Jenkins > System > SonarQube servers` y configure los siguientes campos.

> **Environment variables**: Activado
>
> **Name** (nombre del contenedor): `bbq-sonarqube`
>
> **URL del servidor**: `http://bbq-sonarqube:9000`
>
> **Server authentication token**: `<sonarqube-token>`

Ubíquese en Jenkins y diríjase a `Panel de control > Administrar Jenkins > System > Tools > instalaciones de SonarQube Scanner`,
añada un scanner y configure los siguientes campos.

> **Name**: `bbq-sonarqube-scanner`
>
> **Versión**: Escoger la última versión estable compatible con SonarQube

Finalmente, para que nuestro pipeline realice el análisis estático de SonarQube, ubíquese en el pipeline y diríjase a la
sección `Build Steps`. A continuación, presione el botón `Añadir un nuevo paso`, seleccione la opción `Execute SonarQube Scanner`
y configure los siguientes campos.

> **Task to run**: `Scan`
>
> **JDK**: `Inherit from job`
>
> **Aditional Arguments**: `-X`
>
> **Analysis properties**: Agregue el siguiente contenido y reemplace el servicio web correspondiente
```javascript
sonar.projectKey=business-menu-option-v1
sonar.sources=services/business-services/business-menu-option-v1/src/main/java
sonar.java.binaries=services/business-services/business-menu-option-v1/target/classes
```

# 6. Integrar con DockerHub
Ubíquese en Jenkins e instale el plugin `CloudBees Docker Build and Publish`.

Para que nuestro pipeline se conecte con un repositorio de Docker Hub, ubíquese en el pipeline y diríjase a la sección
`Build Steps`. A continuación, presione el botón `Añadir un nuevo paso`, seleccione la opción `Docker Build and Publish`
y configure los siguientes campos.

> **Repositoy Name**: `miguelarmasabt/<repository>`
>
> **Tag**: `0.0.1-SNAPSHOT`

El siguiente campo requiere que expongamos el Daemon Docker del host. Para ello, ubíquese en Docker Desktop y diríjase a
`Settings > General`, active la opción `Expose daemon on tcp://localhost:2375 without TLS` y reinicie Docker Desktop.

Puede validar que el Daemon Docker fue expuesto exitosamente accediendo a `http://localhost:2375/images/json`.
> **Docker Host URI**: `tcp://host.docker.internal:2375`

Finalmente presione el botón `Avanzado...` y especifique el siguiente campo con el servicio web correspondiente.
> **Build Context**: `services/business-services/business-menu-option-v1/`

# 7. Integrar con Kubernetes
Ubíquese en Jenkins e instale el plugin `Kubernetes`.

Conecte Jenkins a la red de Minikube. Puede utilizar `disconnect` para desconectarse cuando lo requiera.
```shell script
docker network connect minikube bbq-jenkins
```

Configure los privilegios de Jenkins para acceder al clúster de Kubernetes y recupere el token de autenticación.
```shell script
kubectl describe secret/jenkins-token-rk2mg
kubectl apply -f ./devops/jenkins/k8s-jenkins/
```

A continuación, ubíquese en Jenkins y agregue el token como una credencial de tipo `Secret Text` con ID `cluster-k8s-secret`.
Seguidamente, revise la la configuración del clúster de Kubernetes y recupere los siguientes valores.
```shell script
kubectl config view
```

> **clusters.cluster.certificate-authority** (Certificado del clúster): `C:\Users\Miguel\.minikube\ca.crt`
>
> **clusters.cluster.server** (URL de la API del clúster): `https://127.0.0.1:51870`

Exponga la URL de la API del clúster hacia internet con ayuda de `ngrok` y reserve la URL pública.
```shell script
ngrok http https://127.0.0.1:51870
```

Ubíquese en Jenkins y diríjase a `Panel de control > Administrar Jenkins > Configure Clouds`, presione el botón
`Kubernetes Cloud details...`, configure los siguientes campos y pruebe la conexión.
>**Nota**: Si obtiene un error de autenticación, elimine y vuelva a crear la credencial

> **Kubernetes URL** (URL pública): `https://f247-179-6-212-27.ngrok-free.app`
>
> **Kubernetes server certificate key**: Copiar el contenido del certificado `C:\Users\Miguel\.minikube\ca.crt`
>
> **Disable https certificate check**: Activado
>
> **Credentials**: `cluster-k8s-secret`

Para que nuestro pipeline se conecte al clúster de Kubernetes, configure un nuevo job de tipo `Pipeline` con los siguientes campos.
> **General > GitHub project**: `https://github.com/miguel-armas-abt/demo-microservices-bbq`
>
> **Pipeline > Definition**: `Pipeline script from SCM`
>
> **SCM**: `Git`
>
> **Repository URL**: `https://miguel-armas-abt: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@github.com/miguel-armas-abt/demo-microservices-bbq`
>
> **Script Path**: `devops/k8s/business-menu-option-v1/Jenkinsfile`

A continuación, modifique el Jenkinsfile del servicio web correspondiente con la URL pública de la API del cluster.

Finalmente puede encadenar este pipeline con otros pipelines. Para ello diríjase a `Acciones para ejecutar después`,
seleccione `Ejecutar otros proyectos` e indique el/los jobs que desea ejecutar a continuación.
