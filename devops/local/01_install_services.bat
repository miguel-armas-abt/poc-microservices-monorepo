@echo off

set LOG_FILE=C:\dev-workspace\bbq\bbq-monorepo\devops\local\output.log
set MVN_HOME_PATH=C:\dev-environment\maven\apache-maven-3.9.1
set MVN_REPOSITORY_PATH=C:\Users\User\.m2\repository

set LOCAL_REPO=C:\dev-workspace\bbq\bbq-monorepo
set INFRASTRUCTURE_PATH=%LOCAL_REPO%\application\backend\infrastructure
set BUSINESS_PATH=%LOCAL_REPO%\application\backend\business

set MAVEN_COMMAND=call mvn clean install -Dmaven.home="%MVN_HOME_PATH%" -Dmaven.repo.local="%MVN_REPOSITORY_PATH%"

set "INFRASTRUCTURE_SERVICES=bbq-parent-v1 bbq-support-v1 registry-discovery-server-v1 config-server-v1 api-gateway-v1 auth-adapter-v1"
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