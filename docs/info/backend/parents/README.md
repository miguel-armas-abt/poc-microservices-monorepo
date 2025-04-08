# PARENT MODULES

[← Ir a Backend](./../README.md)

> 📌 En Maven, un `parent module` es un proyecto base que centraliza configuraciones comunes que otros proyectos pueden reutilizar.


- Se encuentran en la ruta `application/backend/commons` y tienen el prefijo `parent`.
- Deben ser compilados antes de las aplicaciones que dependan de ellos.
- Actualmente, el proyecto cuenta con los siguientes parent modules:
  - [📦 parent-quarkus-commons-v1](./../../../../application/backend/commons/parent-quarkus-commons-v1/pom.xml)
  - [📦 parent-springboot-commons-v1](./../../../../application/backend/commons/parent-springboot-commons-v1/pom.xml)
  - [📦 parent-springcloud-v1](./../../../../application/backend/commons/parent-springcloud-v1/pom.xml)

---