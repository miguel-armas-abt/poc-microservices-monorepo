# CONTROL DE ERRORES

[← Ir a Backend](./../README.md)

---

[1. ErrorDto](#1-errordto) <br>
[2. GenericException](#2-genericexception) <br>
[3. ErrorDictionary](#3-errordictionary) <br>
[4. ProjectType](#4-projecttype) <br>

---

## 1. `ErrorDto`
La respuesta de error tiene la siguiente estructura:

```json
{
	"type": "BUSINESS",
	"code": "10.00.01",
	"message": "Invalid field."
}
```

| ErrorType  | Descripción                                                     |
|------------|-----------------------------------------------------------------|
| `SYSTEM`   | Error del sistema                                               |
| `BUSINESS` | Error de negocio o por inputs inválidos                         |
| `EXTERNAL` | Error proveniente de un servicio web ajeno a nuestra aplicación |
| `FORWARD`  | Error proveniente de un servicio web propio de la aplicación    |

---

## 2. `GenericException`
- Las nuevas excepciones personalizadas se deben ubicar en el paquete `commons.custom.exceptions`.
- Cada excepción personalizada extenderá de `GenericException` y será registrada en `ErrorDictionary`.

---

## 3. `ErrorDictionary`
Enum que actúa como catálogo de errores de nuestro servicio web.

```java
@Getter
@RequiredArgsConstructor
public enum ErrorDictionary {

  INVALID_FIELD("10.00.01", "Invalid field", BUSINESS, BAD_REQUEST, InvalidFieldException.class),;
    
  private final String code;
  private final String message;
  private final ErrorType type;
  private final HttpStatus httpStatus;
  private final Class<? extends GenericException> exceptionClass;
}
```

---

## 4. `ProjectType`
Enum que establece el tipo de proyecto. Se configura a través de la propiedad `configuration.project-type`.
```java
public enum ProjectType {
  BFF,  // Backend For Frontend
  MS    // Generic Microservice 
}
```

> ### ⚙️ BFF
> Se espera que el frontend no reciba códigos/mensajes de error que comprometan la seguridad del sistema. 
> Debido a ello, si el tipo de proyecto es `BFF`, entonces la respuesta de error será por defecto:
> ```json
> {
>   "type": "SYSTEM",
>   "code": "Default",
>   "message": "No hemos podido realizar tu operación. Estamos trabajando para solucionar el inconveniente."
> }
> ```
> 
> Sin embargo, es posible habilitar aquellos códigos de error que sean necesarios para la experiencia.
> Para ello, agregue aquellos códigos de error y sus correspondientes mensajes en la propiedad `configuration.error-messages`:
> 
> ```yaml
> configuration:
>   error-messages:
>     Default: No hemos podido realizar tu operación. Estamos trabajando para solucionar el inconveniente.
>     "[10.01.03]": ${configuration.error-messages.Default}.
>     "[10.01.04]": Por tu seguridad, tu cuenta ha sido bloqueada.
> ```

> ### ⚙️ MS
> Las respuestas de error tendrán el código y mensaje definidos en la excepción que ha sido lanzada.

---

## 5. Errores personalizados del cliente REST
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


