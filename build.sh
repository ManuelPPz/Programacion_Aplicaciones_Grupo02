#!/usr/bin/env bash
# =============================================================================
#  edEXT - script de compilación (Requerimiento 7.8)
#
#  Genera en la carpeta dist/:
#    servidor.jar  Servidor Central + Estación de Trabajo  (java -jar servidor.jar)
#    web.war       Servidor Web, para desplegar con el Manager de Tomcat
#    config.properties.ejemplo
#
#  Uso:  ./build.sh             compila y corre los tests
#        ./build.sh --sin-tests compila sin correr los tests
#
#  Necesita Java 21 y Maven ("mvn" en el PATH, o la ruta en la variable MVN).
#  La configuración NO va dentro de los paquetes: se lee de
#  ~/.edEXT/config.properties (el Servidor Central lo crea si no existe).
# =============================================================================
set -euo pipefail
cd "$(dirname "$0")"

MVN="${MVN:-mvn}"
if ! command -v "$MVN" >/dev/null 2>&1; then
    echo "No se encontró Maven ('$MVN'). Instalalo o indicá la ruta: MVN=/ruta/a/mvn ./build.sh" >&2
    exit 1
fi

OPCIONES=()
if [[ "${1:-}" == "--sin-tests" ]]; then
    OPCIONES+=(-DskipTests)
fi

echo "==> Servidor Central"
"$MVN" -q -f Proyecto_Proframacion_grupo02/pom.xml clean package "${OPCIONES[@]+"${OPCIONES[@]}"}"

echo "==> Servidor Web"
"$MVN" -q -f PDA_WebServer/pom.xml clean package "${OPCIONES[@]+"${OPCIONES[@]}"}"

echo "==> Copiando a dist/"
rm -rf dist
mkdir -p dist
cp Proyecto_Proframacion_grupo02/target/proyecto_pda_grupo02-1.0-SNAPSHOT-jar-with-dependencies.jar dist/servidor.jar
cp PDA_WebServer/target/PDA_WebServer-1.0-SNAPSHOT.war dist/web.war
cp Proyecto_Proframacion_grupo02/src/main/resources/config-default.properties dist/config.properties.ejemplo

echo
echo "Listo:"
ls -lh dist
echo
echo "Servidor Central:  java -jar dist/servidor.jar"
echo "Servidor Web:      desplegar dist/web.war con el Manager de Tomcat (queda en /web)"
echo "Configuración:     ~/.edEXT/config.properties"
