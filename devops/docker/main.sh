#!/bin/bash

source ./../commons.sh

print_title() {
  echo -e "\n########## ${CYAN} Ejecute una acción ${NC}##########\n"
}

script_caller() {
  $1
  print_title
}

print_title

options=(
  "Construir imágenes"
  "Generar docker-compose.yml"
  "Iniciar orquestación (up)"
  "Detener orquestación (stop)"
  "Eliminar orquestación (delete)"
  "Configurar Keycloak"
  "Salir"
)

while true; do
  select option in "${options[@]}"; do
      case $REPLY in
        1) script_caller "./build-images-csv-processor.sh"; break ;;
        2) script_caller "./docker-compose-file-generator.sh"; break ;;
        3) script_caller "./docker-commands.sh up-compose"; break ;;
        4) script_caller "./docker-commands.sh stop-compose"; break ;;
        5) script_caller "./docker-commands.sh delete-compose"; break ;;
        6) script_caller "./keycloak-config.sh"; break ;;
        7) exit; ;;
        *) echo -e "${RED}Opción inválida${NC}" >&2
      esac
  done
done