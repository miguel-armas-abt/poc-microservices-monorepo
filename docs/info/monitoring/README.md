# OBSERVABILIDAD Y MONITOREO

> ✅ **Pre requisitos** ([Revisar anexos](#anexos))
> - Instalar las siguientes tecnologías:
>   <br>`Prometheus`, `Grafana`, `Zipkin`, `Loki`

# Herramientas de monitoreo
| Herramienta                          | Utilidad                                                                                             |
|--------------------------------------|------------------------------------------------------------------------------------------------------|
| [Prometheus](./prometheus/README.md) | Brinda métricas de rendimiento y estado de aplicaciones, como uso de CPU, RAM, tasas de error, etc.  |
| [Zipkin](./zipkin/README.md)         | Brinda visibilidad sobre la propagación de una solicitud a través de múltiples servicios.            |
| Loki                                 | Permite almacenar y consultar Logs.                                                                  |
| [Grafana](./grafana/README.md)       | Brinda dashboards personalizados basados en las métricas de Prometheus, Loki u otra fuente de datos. |

# Anexos
<br>Guarde los binarios de cada herramienta.

```javascript
  C:
  └───dev-environment
      ├───grafana\grafana-v10.4.2
      ├───loki
      │     ├───loki
      │     └───promptail
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
> - Ejecutar el archivo .jar: `java -jar zipkin-server-3.3.0-exec.jar`
> - Puerto de escucha: `9411`

### Instalar Loki
> - Descargar `loki-windows-amd64.exe.zip` y `promtail-windows-amd64.exe.zip` desde la release de su preferencia.
> <br>https://github.com/grafana/loki/releases/
> <br>
> ![Loki Release](./images/loki-release.png)
> - Descargar el archivo de configuración para cada uno de los ejecutables. Para ello, reemplace la versión en los siguientes enlaces.
>   - loki: https://raw.githubusercontent.com/grafana/loki/v2.9.8/cmd/loki/loki-local-config.yaml
>   - promptail: https://raw.githubusercontent.com/grafana/loki/v2.9.8/clients/cmd/promtail/promtail-local-config.yaml
> 
> **Rutas**: 
> ```javascript
>   loki
>   ├───loki
>   │   ├───loki-windows-amd64.exe
>   │   └───loki-local-config.yaml
>   └───promptail
>       ├───promtail-windows-amd64.exe
>       └───promtail-local-config.yaml
> ```
> 
> - Ejecutar Loki: `.\loki-windows-amd64.exe --config.file=loki-local-config.yaml`
> - Ejecutar Promptail: `.\promtail-windows-amd64.exe --config.file=promtail-local-config.yaml`
>
> **Informativo** - Instrucciones oficiales: https://grafana.com/docs/loki/latest/setup/install/local/