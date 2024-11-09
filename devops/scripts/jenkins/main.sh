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
  "Instalar Jenkins"
  "Desinstalar Jenkins"
  "Generar Jenkinsfiles"
  "Salir"
)

while true; do
  select option in "${options[@]}"; do
      case $REPLY in
        1) script_caller "./deploy-handler.sh"; break ;;
        2) script_caller "./jenkins-processor.sh delete"; break ;;
        3) script_caller "./jenkinsfile-csv-processor.sh"; break ;;
        4) exit; ;;
        *) echo -e "${RED}Opción inválida${NC}" >&2
      esac
  done
done