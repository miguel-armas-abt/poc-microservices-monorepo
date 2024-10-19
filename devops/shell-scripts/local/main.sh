#!/bin/bash

source ./../commons.sh

print_title() {
  echo -e "\n########## ${CYAN} Ejecute una acción ${NC}##########\n"
}

script_caller() {
  $1
  read -rsn1 -p "Presione la tecla enter para regresar al menú"
  clear
  print_title
}

print_title

options=(
  "Compilar los proyectos"
  "Crear BD en MySQL"
  "Salir"
)

select option in "${options[@]}"; do
    case $REPLY in
      1) script_caller "./compile-csv-processor.sh compile"; ;;
      2) script_caller "./database-initializer.sh"; ;;
      3) exit; ;;
      *) echo "Opción inválida" >&2
    esac
done