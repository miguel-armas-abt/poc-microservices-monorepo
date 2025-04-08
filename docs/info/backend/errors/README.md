# CONTROL DE ERRORES

[← Ir a Backend](./../README.md)

---

## 1. ErrorDto
La respuesta de error tiene la siguiente estructura:

```json
{
	"type": "SYSTEM", // SYSTEM | BUSINESS | EXTERNAL | FORWARD
	"code": "Default",
	"message": "No hemos podido realizar tu operación. Estamos trabajando para solucionar el inconveniente."
}
```

| Type       | Descripción                                                     |
|------------|-----------------------------------------------------------------|
| `SYSTEM`   | Error inesperado                                                |
| `BUSINESS` | Error de negocio o inputs inválidos                             |
| `EXTERNAL` | Error proveniente de un servicio web ajeno a nuestra aplicación |
| `FORWARD`  | Error proveniente de un servicio web propio de la aplicación    |


## 2. ErrorDictionary
Es un Enum que representa el catálogo de errores de nuestra aplicación.

## 3. GenericException
Por cada caso de error, crearemos una excepción personalizada que extienda de `GenericException`. 
```java
@Getter
public class GenericException extends RuntimeException {

    protected ErrorDTO errorDetail;
    protected HttpStatus httpStatus;

    public GenericException(String message) {
        super(message);
    }
}
```


## 4. Características

### 1. Mensajes de error personalizados
- **Objetivo**: Brindar mensajes personalizados de error.
- **Implementación**: `ResponseErrorHandler`
- **Lógica**: 
  - Corresponder mensajes personalizados con los códigos de error.
  - Asignar un código de error `Default` para errores sin mensaje personalizado.
  - Establecer un flag para habilitar o deshabilitar funcionalidad.
    - `false`: Mostrar mensaje propagado.
    - `true`: Reemplazar el mensaje propagado por el mensaje personalizado.
- **Uso**: Configuración en el properties.

```yaml
configuration:
  error-messages:
    enabled: false
    messages:
      Default: No hemos podido realizar tu operación. Estamos trabajando para solucionar el inconveniente.
      ExampleErrorCode: Mensaje personalizado de ejemplo.
```

### 2. Errores personalizados del cliente REST
- **Objetivo**: Capturar el error del cliente REST y personalizar sus atributos.
- **Implementación**: `ExternalErrorHandler`
- **Lógica**: Se podrá propagar o reemplazar opcionalmente cada uno de los siguientes atributos del error del cliente REST.
  - Código de error
  - Mensaje de error
  - Código HTTP
- **Precondiciones**:
  - Extender de `ExternalErrorWrapper` (DTO que representa la respuesta de error del cliente REST).
  - Implementar `ExternalErrorStrategy`.
    - `getCodeAndMessage()`: Recupera el código y mensaje a partir de la respuesta del cliente REST.
    - `supports()`: Identifica la estrategia seleccionada.
- **Uso**: 
  - Utilizar el método `handleError()` de `ExternalErrorHandler` cuando falle el consumo del cliente REST.
  - Configuración en el properties.

```yaml
configuration:
  rest-clients:
    product-v1:
      errors:
        ExampleExternalErrorCode:
          code: CustomExternalErrorCode
          message: Mensaje personalizado de ejemplo.
          httpCode: 400

```


