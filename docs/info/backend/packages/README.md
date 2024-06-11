# Arquitectura de paquetes

[← Regresar Backend](./../README.md)

La arquitectura de paquetes presentada es solo una plantilla. La implementación real variará según las necesidades
específicas de cada proyecto.

```javascript
    com.demo.bbq
    │───`rest`
    ├───`application`
    │   ├───constant                //*Constant
    │   ├───dto
    │   │   └───`<context>`
    │   │       ├───request         //*RequestDTO
    │   │       └───response        //*ResponseDTO
    │   ├───enums                   //*Category | *Type
    │   ├───events
    │   │   ├───consumer
    │   │   │   └───`<context>`     //*Consumer
    │   │   │       └───message     //*Message
    │   │   └───producer
    │   │       └───`<context>`     //*Producer
    │   │           └───message     //*Message
    │   ├───mapper                  //*Mapper
    │   └───service                 
    │       └───`<context>`         //*Service & *ServiceImpl
    ├───`repository`
    │   └───`<data-model>`          //*Repository
    │       └───entity | document | request | response  //*Entity | *Document | *RequestWrapper | *ResponseWrapper
    └───`config`
        ├───properties   
        ├───errors   
        └───restclient   
```