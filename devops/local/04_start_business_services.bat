@echo off

set LOCAL_REPO=C:\dev-workspace\bbq\bbq-monorepo
set BUSINESS_PATH=%LOCAL_REPO%\application\backend\business
set LOG_FILE=C:\dev-workspace\bbq\bbq-monorepo\devops\local\output.log
set JAVA=C:\dev-environment\java\jdk-11.0.2\bin\java
set GO=C:\dev-environment\go\go1.21.4\bin\go

set "GO_SERVICES=product-v1"
set "SPRING_BOOT_SERVICES=menu-v1 order-hub-v1 payment-v1 table-placement-v1"
set "QUARKUS=menu-v2"

echo %DATE% %TIME%: Business execution script started > "%LOG_FILE%"

for %%i in (%SPRING_BOOT_SERVICES%) do (
  start "%%i" cmd /k %JAVA% -jar "%BUSINESS_PATH%\%%i\target\%%i-0.0.1-SNAPSHOT.jar"
  echo %DATE% %TIME%: %%i executed >> "%LOG_FILE%"
)

for %%i in (%GO_SERVICES%) do (
  cd %BUSINESS_PATH%\%%i
  start "%%i" cmd /k %GO% run main.go
  echo %DATE% %TIME%: %%i executed >> "%LOG_FILE%"
)