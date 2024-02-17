@echo off

call ./00_local_path_variables.bat @REM recover local path variables

set "INFRASTRUCTURE_SERVICES=registry-discovery-server-v1 config-server-v1 api-gateway-v1"

echo %DATE% %TIME%: Infrastructure execution script started > "%LOG_FILE%"
for %%i in (%INFRASTRUCTURE_SERVICES%) do (
  start "%%i" cmd /k %JAVA_PATH% -jar "%INFRASTRUCTURE_PATH%\%%i\target\%%i-0.0.1-SNAPSHOT.jar"
  echo %DATE% %TIME%: %%i executed >> "%LOG_FILE%"
)