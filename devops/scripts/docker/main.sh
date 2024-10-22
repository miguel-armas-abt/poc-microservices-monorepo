#!/bin/bash

source ./../commons.sh

print_title() {
  echo -e "\n########## ${CYAN} Ejecute una acción con Docker ${NC}##########\n"
}

script_caller() {
  $1
  print_title
}

print_title

options=(
  "Construir imágenes"
  "Generar docker-compose.yml"
  "Iniciar docker-compose"
  "Mostrar contenedores"
  "Recrear docker-compose"
  "Detener docker-compose"
  "Eliminar docker-compose"
  "Iniciar Keycloak"
  "Salir"
)

while true; do
  select option in "${options[@]}"; do
      case $REPLY in
        1) script_caller "./docker-csv-processor.sh build"; break ;;
        2) script_caller "./compose-file-processor.sh template"; break ;;
        3) script_caller "./compose-file-processor.sh up"; break ;;
        4) script_caller "./compose-file-processor.sh list"; break ;;
        5) script_caller "./compose-file-processor.sh recreate"; break ;;
        6) script_caller "./compose-file-processor.sh stop"; break ;;
        7) script_caller "./compose-file-processor.sh delete"; break ;;
        8) script_caller "./keycloak-processor.sh up"; break ;;
        9) exit; ;;
        *) echo -e "${RED}Opción inválida${NC}" >&2
      esac
  done
done