# GRAFANA

[← Regresar Monitoreo](./../README.md)

# Fuentes de datos
En la sección `Data sources` podemos administrar nuestras fuentes de datos (Prometheus y Loki)

![Fuentes de datos](./images/add-data-source.jpg)

En la sección `Explore` podemos explorar los datos asociados a cada fuente de información

![Fuentes de datos](./images/explore.jpg)

# Logs con LOKI
Podemos filtrar los logs por las etiquetas o labels expuestas a Loki.

![Filtro de logs](./images/loki-logs.jpg)

Para observar el detalle de cada log, podemos dar clic en el botón `Inspect value`

![Detalle del log](./images/loki-logs-detail.jpg)

# Métricas con PROMETHEUS
Por hacer.

# Dashboards
- Grafana tiene una variedad de plantillas de dashboards en su página oficial https://grafana.com/grafana/dashboards/
- Podemos buscar el dashboard que más se adecúe a nuestras necesidades e importarlo mediante su ID.
  - `Spring Boot Statistics` (Requiere Prometheus): `6756` 
  - `Spring Boot Observability` (Requiere Loki y Prometheus): `17175`
- Previo a la importación, validar que nuestra integración cumpla con los requisitos del dashboard.

![Importación del dashboard](./images/dashboard-import.jpg)

Tras la importación del dashboard, podremos visualizarlo y personalizarlo según nuestras preferencias.

![Importación del dashboard](./images/dashboard-spring-boot-stastics.jpg)