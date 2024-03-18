@echo off

call ./00_local_path_variables.bat @REM recover local path variables

set MAVEN_COMMAND=call mvn clean install -Dmaven.home="%MVN_HOME_PATH%" -Dmaven.repo.local="%MVN_REPOSITORY_PATH%"

set "INFRASTRUCTURE_SERVICES=spring-boot-parent-v1 bbq-support-v1 registry-discovery-server-v1 config-server-v1 api-gateway-v1 auth-adapter-v1"
set "BUSINESS_SERVICES=menu-v1 table-placement-v1 order-hub-v1 invoice-v1 payment-v1 menu-v2"

echo %DATE% %TIME%: Installation script started > "%LOG_FILE%"
for %%i in (%INFRASTRUCTURE_SERVICES%) do (
  cd "%INFRASTRUCTURE_PATH%\%%i"
  %MAVEN_COMMAND%
  echo %DATE% %TIME%: %%i executed >> "%LOG_FILE%"
)

for %%i in (%BUSINESS_SERVICES%) do (
  cd "%BUSINESS_PATH%\%%i"
  %MAVEN_COMMAND%
  echo %DATE% %TIME%: %%i executed >> "%LOG_FILE%"
)