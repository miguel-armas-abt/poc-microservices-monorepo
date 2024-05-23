# OBSERVABILIDAD Y MONITOREO

> ✅ **Pre requisitos** ([Revisar anexos](#anexos))
> - Instalar las siguientes tecnologías:
>   <br>`Prometheus`, `Grafana`, `Zipkin`, `Loki`

# Herramientas de monitoreo
| Herramienta                          | Utilidad                                                                                             |
|--------------------------------------|------------------------------------------------------------------------------------------------------|
| [Prometheus](./prometheus/README.md) | Brinda métricas de rendimiento y estado de aplicaciones, como uso de CPU, RAM, tasas de error, etc.  |
| [Zipkin](./zipkin/README.md)         | Brinda visibilidad sobre la propagación de una solicitud a través de múltiples servicios.            |
| [Loki](./loki/README.md)             | Permite almacenar y consultar Logs.                                                                  |
| [Grafana](./grafana/README.md)       | Brinda dashboards personalizados basados en las métricas de Prometheus, Loki u otra fuente de datos. |

# Anexos
<br>Guarde los binarios de cada herramienta.

```javascript
  C:
  └───dev-environment
      ├───grafana\grafana-v10.4.2
      ├───prometheus\prometheus-2.51.1
      └───zipkin
```

### Instalar Prometheus
> Descargar para `Windows`
> <br>**Ruta**: `C:\dev-environment\prometheus\prometheus-2.51.1`
> <br>https://prometheus.io/download/
> <br>
> - Configurar el archivo `prometheus.yml` con los servicios que desea monitorear
> - Ejecutar el archivo `prometheus.exe`
> - Puerto de escucha: `9090`

### Instalar Grafana
> Descargar para `Windows`
> <br>**Ruta**: `C:\dev-environment\grafana\grafana-v10.4.2`
> <br>https://grafana.com/grafana/download?platform=windows
> <br>
> - Ejecutar el archivo `\bin\grafana-server.exe`
> - Puerto de escucha: `3000`

### Instalar Zipkin
> Descargar para `Java` (latest release)
> <br>**Ruta**: `C:\dev-environment\zipkin`
> <br>https://zipkin.io/pages/quickstart
> <br>
> - Ejecutar el archivo .jar `java -jar zipkin-server-3.3.0-exec.jar`
> - Puerto de escucha: `9411`