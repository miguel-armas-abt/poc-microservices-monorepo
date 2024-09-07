# Manejo de logs

[← Ir a Backend](./../README.md)

## Características

### 1. Ofuscación
- **Objetivo**: Ofucar los campos sensibles en los logs.
- **Lógica**: Por cada log registrado se ofuscarán los campos del body y headers.
- **Uso**: Configuración en el properties.

```yaml
configuration:
  obfuscation:
    body-fields: ["example.request.body.field", "example.response.body.array[*].field"]
    headers: ["Authorization", "Subscription-Key"]
```