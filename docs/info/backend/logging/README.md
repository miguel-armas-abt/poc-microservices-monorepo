# Gestión de logs

[← Regresar Backend](./../README.md)

## Características

### 1. Ofuscación en los logs
- **Objetivo**: Ofucar los campos sensibles en los logs.
- **Implementación**: `BodyObfuscatorUtil`, `HeaderObfuscatorUtil` 
- **Lógica**: Por cada log registrado se ofuscarán los campos del body y headers.
- **Uso**: Configuración en el properties.

```yaml
configuration:
  obfuscation:
    body-fields: ["example.request.body.field", "example.response.body.array[*].field"]
    headers: ["Authorization", "Subscription-Key"]
```