# DevOps con Jenkins
Esta guía le ayudará a configurar sus pipelines de Jenkins e integrarlo con otras herramientas de DevOps.

- [1. Ejecutar contenedor de Jenkins](#1-ejecutar-contenedor-de-jenkins)
- [2. Crear pipeline de estilo libre](#2-crear-pipeline-de-estilo-libre)
- [3. Integrar con GitHub Webhooks](#3-integrar-con-github-webhooks)
- [4. Integrar con Slack](#4-integrar-con-slack)
- [5. Integrar con SonarQube](#5-integrar-con-sonarqube)
- [6. Integrar con DockerHub](#6-integrar-con-dockerhub)
- [7. Integrar con Kubernetes](#7-integrar-con-kubernetes)
- [8. Encadenar pipelines](#8-encadenar-pipelines)

Al finalizar esta guía usted podrá tener un pipeline que automatice los siguientes procesos.
> 1. (Opcional) Ejecutar el pipeline automáticamente tras efectuar un push sobre el repositorio
> 2. Compilar el código fuente de la rama `feature/<feature-name>`
> 3. Realizar un análisis estático de SonarQube
> 4. Si la compilación y el análisis estático fueron exitosos, entonces mergear la rama `feature/<feature-name>` con la rama `main`
> 5. Pushear en DockerHub la imagen del servicio web 
> 6. Desplegar el servicio web en Kubernetes, basado en la imagen disponible en DockerHub
> 7. Notificar a Slack sobre los eventos ocurridos en el pipeline

Estos pipelines automatizarán la gestión de los pull requests y el despliegue de los servicios web, promoviendo el 
siguiente flujo de trabajo: 
> 1. El desarrollador creará una rama `feature/<feature-name>` a partir de la rama `main`, sobre la cual desarrollará una nueva característica.
> 2. Al termino del desarrollo, el desarrollador creará un pull request (PR) y asignará a un revisor.
> 3. El revisor verificará el PR manualmente y dará el feedback correspondiente al desarrollador (revisión de pares).
> 4. De haber alguna observación durante la revisión de pares, el desarrollador la corregirá y a continuación, ejecutará el pipeline.
> 5. De haber alguna deuda técnica durante el análisis estático, el desarrollador la corregirá y a continuación, ejecutará el pipeline.

### Antes de empezar

> Algunos de los siguientes pasos requerirán un token clásico de GitHub. Para ello ubíquese en GitHub, diríjase a 
> `Account > Settings > Developer settings > Personal access tokens` y cree un nuevo token con acceso a `repo`. En 
> adelante lo referenciaremos como `<github-access-token>`

# 1. Ejecutar contenedor de Jenkins
#### Iniciar orquestación
```shell script
docker-compose -f docker-compose.yml up -d
docker build -t miguelarmasabt/bbq-jenkins:v1 . --no-cache
```

#### Detener orquestación
Utilice `down -v` en lugar de `stop` para eliminar la orquestación
```shell script
docker-compose -f docker-compose.yml stop
```

Revise los logs del contenedor y copie el token en la pantalla de login `localhost:8181`.
```shell script
docker logs bbq-jenkins
```
A continuación, instale los plugins sugeridos `Install suggested plugins`, cree una cuenta de administrador
(username=`bbq-user`, password=`qwerty`) y mantenga la URL por defecto `http://localhost:8181/`.

# 2. Crear pipeline de estilo libre
Seleccione `Crear un proyecto de estilo libre`, ubique la sección `Configurar el origen del código fuente`, seleccione 
Git y configure los siguientes campos.
> **Repository URL**: `https://miguel-armas-abt:<github-access-token>@github.com/miguel-armas-abt/demo-microservices-bbq`
>
> **Branch to build**: `feature/<feature-name>`

Ubique el botón `Additional Behaviours`, seleccione la opción `Custom user name/e-mail address` y agrege un username y 
email ficticios. A continuación, ubique la sección `Build Steps`, presione el botón `Add build step`, seleccione la 
opción `Ejecutar tareas 'maven' de nivel superior` y configure los siguientes campos.
> **Goals**: `clean install`
> 
> **POM**: `application/backend/business/menu-v1/pom.xml`

Ubique la sección `Build Steps`, presione el botón `Add build step`, seleccione la opción `Ejecutar línea de comandos (shell)`
y agregue los siguientes comandos:
```shell script
git branch
git checkout main
git merge origin/feature/<feature-name>
```

Ubique la sección `Acciones para ejecutar después`, presione el botón `Añadir una acción`, seleccione la opción `Git Publisher`
, configure los siguientes campos y guarde los cambios.
> **Push Only If Build Succeeds**: Activado
> 
> **Branches > Branch to push**: main
> 
> **Branches > Target remote name**: origin

> Finalmente, usted puede activar en GitHub la opción para eliminar la rama absorvida tras el merge.

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
>
> **Credentials**: `<github-access-token>`

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
sonar.projectKey=menu-v1
sonar.sources=application/backend/business/menu-v1/src/main/java
sonar.java.binaries=application/backend/business/menu-v1/target/classes
```

# 6. Integrar con DockerHub
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
Seguidamente, revise la configuración del clúster de Kubernetes y recupere los siguientes valores.
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
> **Repository URL**: `https://miguel-armas-abt:<github-access-token>@github.com/miguel-armas-abt/demo-microservices-bbq`
>
> **Script Path**: `devops/k8s/menu-v1/Jenkinsfile`

Finalmente, modifique el Jenkinsfile del servicio web con la URL pública de la API del cluster.

# 8. Encadenar pipelines
Para encadenar pipelines diríjase a la opción `Acciones para ejecutar después` en su pipeline, seleccione 
`Ejecutar otros proyectos` e indique el/los jobs que desea ejecutar a continuación. La idea es que el pipeline de
Kuberntes vaya a continuación del pipeline de estilo libre.
