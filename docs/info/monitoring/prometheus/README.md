# PROMETHEUS

[← Regresar](./../README.md)

# Endpoints de Actuator
Accediendo a la opción `Targets` podemos visualizar los servicios que tienen un enpoint activo de Actuator.

![Endpoints de Actuator](./images/available-actuator-endpoints.png)

# Consultas PromQL
Desde el home de Prometheus podemos hacer consultas con PromQL. Por ejemplo:
- `jvm_memory_used_bytes{area="heap"}`: Cantidad de memoria usada por la JVM en la región heap
- `system_cpu_usage`: Porcentaje de uso del CPU
- `http_server_requests_seconds_count`: Total de solicitudes HTTP recibidas por el servidor
- `http_server_requests_seconds_count{uri="/bbq/business/menu/v1/menu-options/{productCode}"}`: Total de solicitudes HTTP para una URI específica

![PromQL](./images/graph-promql.png)