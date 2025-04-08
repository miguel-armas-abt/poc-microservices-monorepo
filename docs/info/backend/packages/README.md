# ARQUITECTURA DE PAQUETES

[← Ir a Backend](./../README.md)

---

- La implementación real variará según las necesidades de cada proyecto.
- Puede haber más de un bounded context.

```sh
    com.demo.poc
    │───`commons`
    │   ├───core
    │   └───custom
    │       ├───exceptions                  # *Exception                  
    │       └───properties                  # ApplicationProperties
    │
    └───`entrypoint`
        └───`<bounded-context>`
            │
            ├───constants                   # *Constants
            │
            ├───dto
            │   ├───params                  # *Param
            │   ├───request                 # *RequestDto
            │   └───response                # *ResponseDto
            │
            ├───enums                       # *Type | *Category | *Enum
            │
            ├───event
            │   └───`<data-model>`          # *Producer | *Consumer
            │       └───message             # *InboundMessage | *OutboundMessage 
            │
            ├───mapper                      # *RequestMapper | *ResponseMapper
            │
            ├───repository
            │   └───`<data-model>`          # *Repository
            │       └───entity | wrapper    # *Entity | *Document | *RequestWrapper | *ResponseWrapper
            │
            ├───rest                        # *RestService
            │
            └───service                     # *Service
```

## Paquete `core`

```sh
    com.demo.poc
    │───`commons`
    .   └───core
    .       ├───config              # Configuración e inyección de beans
    .       ├───errors              # Administración de errores
    .       ├───interceptor         # Intercepción de eventos (errores, peticiones REST, etc)
    .       ├───logging             # Población de logs
    .       ├───obfuscation         # Ofuscamiento de headers y JSON bodies
    .       ├───properties          # Estructura base de las properties
    .       ├───restclient          # Configuración del cliente REST (RestTemplate, Retrofit, WebClient, etc)
    .       ├───restserver          # Utilidades para la respuesta REST de nuestro backend
    .       ├───serialization       # Utilidades de serialización/deserialización
    .       ├───tracing             # Utilidades de trazabilidad (traceParent, traceId, parentId)
    .       └───validations         # Validación de inputs
```