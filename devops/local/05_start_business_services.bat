@echo off

call ./00_local_path_variables.bat @REM recover local path variables

set "GO_SERVICES=product-v1"
set "SPRING_BOOT_SERVICES=menu-v1 order-hub-v1 payment-v1"
set "QUARKUS_SERVICES=menu-v2"

echo %DATE% %TIME%: Business execution script started > "%LOG_FILE%"

@REM for %%i in (%SPRING_BOOT_SERVICES%) do (
@REM  start "%%i" cmd /k %JAVA_PATH% -jar "%BUSINESS_PATH%\%%i\target\%%i-0.0.1-SNAPSHOT.jar"
@REM  echo %DATE% %TIME%: %%i executed >> "%LOG_FILE%"
@REM )

for %%i in (%GO_SERVICES%) do (
  cd %BUSINESS_PATH%\%%i
  start "%%i" cmd /k %GO_PATH% run main.go
  echo %DATE% %TIME%: %%i executed >> "%LOG_FILE%"
)