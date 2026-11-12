@echo off
REM =============================================================================
REM  edEXT - script de compilacion (Requerimiento 7.8)
REM
REM  Genera en la carpeta dist\:
REM    servidor.jar  Servidor Central + Estacion de Trabajo  (java -jar servidor.jar)
REM    web.war       Servidor Web, para desplegar con el Manager de Tomcat
REM    config.properties.ejemplo
REM
REM  Uso:  build.bat              compila y corre los tests
REM        build.bat --sin-tests  compila sin correr los tests
REM
REM  Necesita Java 21 y Maven ("mvn" en el PATH, o la ruta en la variable MVN).
REM  La configuracion se lee de %USERPROFILE%\.edEXT\config.properties.
REM =============================================================================
setlocal
cd /d "%~dp0"

if "%MVN%"=="" set "MVN=mvn"
where "%MVN%" >nul 2>nul
if errorlevel 1 if not exist "%MVN%" (
    echo No se encontro Maven ^("%MVN%"^). Instalalo o indica la ruta: set MVN=C:\ruta\a\mvn.cmd
    exit /b 1
)

set "OPCIONES="
if "%~1"=="--sin-tests" set "OPCIONES=-DskipTests"

echo ==^> Servidor Central
call "%MVN%" -q -f Proyecto_Proframacion_grupo02\pom.xml clean package %OPCIONES%
if errorlevel 1 exit /b 1

echo ==^> Servidor Web
call "%MVN%" -q -f PDA_WebServer\pom.xml clean package %OPCIONES%
if errorlevel 1 exit /b 1

echo ==^> Copiando a dist\
if exist dist rmdir /s /q dist
mkdir dist
copy /y Proyecto_Proframacion_grupo02\target\proyecto_pda_grupo02-1.0-SNAPSHOT-jar-with-dependencies.jar dist\servidor.jar >nul
copy /y PDA_WebServer\target\PDA_WebServer-1.0-SNAPSHOT.war dist\web.war >nul
copy /y Proyecto_Proframacion_grupo02\src\main\resources\config-default.properties dist\config.properties.ejemplo >nul

echo.
echo Listo:
dir /b dist
echo.
echo Servidor Central:  java -jar dist\servidor.jar
echo Servidor Web:      desplegar dist\web.war con el Manager de Tomcat (queda en /web)
echo Configuracion:     %USERPROFILE%\.edEXT\config.properties
endlocal
