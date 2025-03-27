# Arquitectura de paquetes

[← Ir a Backend](./../README.md)

La arquitectura de paquetes presentada es solo una plantilla. La implementación real variará según las necesidades
específicas de cada proyecto.

```javascript
    com.demo.poc
    │───`commons`
    │   ├───config
    │   └───properties                      //ApplicationProperties
    └───`entrypoint`
        └───`<context>`
            ├───dto
            │   ├───request                 //*RequestDTO
            │   └───response                //*ResponseDTO
            ├───enums                       //*Type | *Category
            ├───event
            │   └───`<context>`             //*Producer | *Consumer
            │       └───message             //*Message
            ├───mapper                      //*Mapper
            ├───repository
            │   └───`<data-model>`          //*Repository
            │       └───entity | wrapper    //*Entity | *RequestWrapper | *ResponseWrapper
            └───rest                        //*RestService
```