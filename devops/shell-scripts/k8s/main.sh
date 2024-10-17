#!/bin/bash

source ./../commons.sh

print_title() {
  echo -e "\n########## ${GREEN} Ejecute una acción con HELM ${NC}##########\n"
}

script_caller() {
  $1
  read -rsn1 -p "Presione la tecla enter para regresar al menú"
  clear
  print_title
}

print_title

options=(
  "Generar manifiestos"
  "Instalar componentes en el clúster"
  "Desinstalar componentes del clúster"
  "Salir"
)

select option in "${options[@]}"; do
    case $REPLY in
      1) script_caller "./csv-processor.sh template"; ;;
      2) script_caller "./csv-processor.sh install"; ;;
      3) script_caller "./csv-processor.sh uninstall"; ;;
      4) exit; ;;
      *) echo "Opción inválida" >&2
    esac
done