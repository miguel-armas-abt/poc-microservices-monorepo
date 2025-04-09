# Control de logs

[← Ir a Backend](./../README.md)

## 1. `LoggingType`
Enum que mapea los tipos de logs:

```java
@Getter
@RequiredArgsConstructor
public enum LoggingType {

  ERROR("error", "Error"),
  REST_SERVER_REQ("rest.server.req", "REST server request"),
  REST_SERVER_RES("rest.server.res", "REST server response"),
  REST_CLIENT_REQ("rest.client.req", "REST client request"),
  REST_CLIENT_RES("rest.client.res", "REST client response");

  private final String code;
  private final String message;
}
```
Con el propósito de mostrar un stack trace más o menos poblado, es posible habilitar/deshabilitar cada tipo de log.
Para ello, ajuste la propiedad `configuration.logging.logging-type`:

```yaml
configuration:
  logging:
    logging-type:
        error: true
        rest.server.req: false
        rest.server.res: false
        rest.client.req: true
        rest.client.res: true
```

---

## 2. Ofuscación de valores sensibles
Para ofuscar valores sensibles, ajuste la propiedad `configuration.logging.obfuscation`:

```yaml
configuration:
  logging:
      obfuscation:
        body-fields: ["example.request.body.field", "example.response.body.array[*].field"]
        headers: ["Authorization", "Subscription-Key"]
```