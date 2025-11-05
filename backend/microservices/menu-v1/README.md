
[← Regresar](../README.md) <br>

---

## 📋 Core library
[🌐 Documentación](https://github.com/miguel-armas-abt/backend-core-library) <br>
[🏷️ Versión](./src/main/java/com/demo/service/commons/core/package-info.java) <br>

---

## ▶️ Despliegue local en modo dev

```shell
clean compile quarkus:dev
```

---

## ▶️ Empaquetar y ejecutar jar

⚙️ Empaquetar en un tipo de **jar** específico (`fast-jar`, `uber-jar`, `mutable-jar`)
```shell
mvn clean package -Dquarkus.package.type=uber-jar
```

⚙️ Ejecutar empaquetado
```shell
java -jar target/menu-v1-1.0-SNAPSHOT-runner.jar
```

---

## ▶️ Despliegue local con imagen nativa

⚙️ Generar imagen nativa
```sh
mvn clean package -Pnative
```

⚙️ Ejecutar imagen nativa <br>
Para pruebas locales, desactiva desde el `pom.xml` la siguiente propiedad: `quarkus.native.container-build`
```sh
./target/menu-v1-1.0-SNAPSHOT-runner.exe
```