#!/bin/bash
set -e

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
  "Compilar proyectos backend"
  "Inicializar bases de datos en MySQL"
  "Ejecutar servidor"
  "Ejecutar backend"
  "Salir"
)

while true; do
  select option in "${options[@]}"; do
      case $REPLY in
        1) script_caller "./compile-backend.sh"; break ;;
        2) script_caller "./database-initializer.sh"; break ;;
        3) script_caller "./start-server.sh"; break ;;
        4) script_caller "./run-backend-csv-processor.sh"; break ;;
        5) exit; ;;
        *) echo -e "${RED}Opción inválida${NC}" >&2
      esac
  done
done