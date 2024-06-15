# Manejo de errores

[← Regresar Backend](./../README.md)

```json
{
	"type": "SYSTEM",
	"code": "Default",
	"message": "No hemos podido realizar tu operación. Estamos trabajando para solucionar el inconveniente."
}
```

## Excepciones y códigos de error HTTP

| Excepción                  | Código HTTP                      | ErrorType              | Propósito                              |
|----------------------------|----------------------------------|------------------------|----------------------------------------|
| `SystemException`          | `500`                            | `SYSTEM`               | Errores de configuración o sistema     |
| `BusinessException`        | `400`                            | `BUSINESS`             | Errores de negocio o datos incorrectos |
| `ExternalServiceException` | `409` o reutiliza el código HTTP | `EXTERNAL` o `FORWARD` | Error en la petición al cliente REST   |

> 📌 Las excepciones `SystemException` y `BusinessException` requieren obligatoriamente un código de error.

## Características

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


