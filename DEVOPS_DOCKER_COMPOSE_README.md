# DevOps con Docker Compose
Esta guía le ayudará a orquestar los servicios utilizando Docker Compose.

- [1. Pre requisitos](#1-pre-requisitos)
- [2. Comandos de supervivencia](#2-comandos-de-supervivencia)
- [3. Conexión con MYSQL](#3-conexin-con-mysql)
- [4. Conexión con POSTGRESQL](#4-conexin-con-postgresql)

## 1. Pre requisitos
### Servicios con Spring Boot
Los servicios implementados con Spring Boot hacen uso del plugin `dockerfile-maven-plugin` para la construcción
automática de las imágenes durante la fase `package` de Maven.

Ejecute el siguiente comando sobre cada proyecto de Spring Boot para construir sus respectivas imágenes :
```shell script
mvnw clean package
```

### Servicios con Quarkus
El framework de Quarkus provee una serie de archivos Dockerfile de acuerdo al tipo de compilación que se requiera, 
ubicados en el directorio `src/main/docker/`. Para este caso se ha configurado el uso del archivo `Dockerfile.jvm`.

Ejecute el siguiente comando sobre cada proyecto de Quarkus para construir sus respectivas imágenes :
- Reemplazar `<service-name>` por el nombre del servicio
```shell script
docker build -f src/main/docker/Dockerfile.jvm -t bbq-images/<service-name>:0.0.1-SNAPSHOT .
```

## 2. Comandos de supervivencia

### Iniciar los servicios definidos en la orquestación:
```shell script
docker-compose -f ./devops/docker-compose/docker-compose.yml up -d --force-recreate
```

### Eliminar los servicios definidos en la orquestación:
```shell script
docker-compose -f ./devops/docker-compose/docker-compose.yml down
```

## 3. Conexión con MYSQL
Para conectarnos a la base de datos MySQL a través de DBeaver agregar los siguientes parámetros.

| Parámetro         | Valor                                          | 
|-------------------|------------------------------------------------|
| Server Host       | `localhost`                                    | 
| Database          | `db_menu_options?allowPublicKeyRetrieval=true` | 
| Nombre de usuario | `root` o  `bbq_user`                           | 
| Contrasña         | `qwerty`                                       |

## 4. Conexión con POSTGRESQL
Para conectarnos a la base de datos PostregSQL a través de DBeaver agregar los siguientes parámetros.
- Activar la opción `Show all database` de la pestaña `PostgreSQL`.

| Parámetro         | Valor                    | 
|-------------------|--------------------------|
| Connect by        | `HOST`                   | 
| Host              | `localhost`              | 
| Database          | `db_dining_room_orders`  | 
| Port              | `5432`                   |
| Nombre de usuario | `postgres` o  `bbq_user` | 
| Contrasña         | `qwerty`                 |

