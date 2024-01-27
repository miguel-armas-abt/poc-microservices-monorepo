@echo off

set LOCAL_REPO=C:\dev-workspace\bbq\bbq-monorepo
set INFRASTRUCTURE_PATH=%LOCAL_REPO%\application\backend\infrastructure
set LOG_FILE=C:\dev-workspace\bbq\bbq-monorepo\devops\local\output.log
set JAVA=C:\dev-environment\java\jdk-11.0.2\bin\java

set "INFRASTRUCTURE_SERVICES=registry-discovery-server-v1 config-server-v1 api-gateway-v1"

echo %DATE% %TIME%: Infrastructure execution script started > "%LOG_FILE%"
for %%i in (%INFRASTRUCTURE_SERVICES%) do (
  start "%%i" cmd /k %JAVA% -jar "%INFRASTRUCTURE_PATH%\%%i\target\%%i-0.0.1-SNAPSHOT.jar"
  echo %DATE% %TIME%: %%i executed >> "%LOG_FILE%"
)