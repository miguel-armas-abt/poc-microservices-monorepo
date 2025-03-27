#!/bin/bash

source ./../commons.sh

need_generate_file=$1

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
  "Configurar y orquestar"
  "Generar docker-compose.yml"
  "Iniciar orquestación (up)"
  "Detener orquestación (stop)"
  "Eliminar orquestación (delete)"
  "Salir"
)

while true; do
  select option in "${options[@]}"; do
      case $REPLY in
        1) script_caller "./docker-csv-processor.sh"; break ;;
        2) script_caller "./deploy-handler.sh $need_generate_file"; break ;;
        3) script_caller "./compose-file-generator.sh"; break ;;
        4) script_caller "./compose-file-processor.sh up"; break ;;
        5) script_caller "./compose-file-processor.sh stop"; break ;;
        6) script_caller "./compose-file-processor.sh delete"; break ;;
        7) exit; ;;
        *) echo -e "${RED}Opción inválida${NC}" >&2
      esac
  done
done