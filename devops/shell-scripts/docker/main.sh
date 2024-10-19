#!/bin/bash

source ./../commons.sh

print_title() {
  echo -e "\n########## ${CYAN} Ejecute una acción con Docker ${NC}##########\n"
}

script_caller() {
  $1
  read -rsn1 -p "Presione la tecla enter para regresar al menú"
  clear
  print_title
}

print_title

options=(
  "Construir imágenes"
  "Generar docker-compose"
  "Iniciar docker-compose"
  "Mostrar contenedores"
  "Recrear docker-compose"
  "Detener docker-compose"
  "Eliminar docker-compose"
  "Salir"
)

select option in "${options[@]}"; do
    case $REPLY in
      1) script_caller "./docker-csv-processor.sh build"; ;;
      2) script_caller "./compose-file-processor.sh template"; ;;
      3) script_caller "./compose-file-processor.sh up"; ;;
      4) script_caller "./compose-file-processor.sh list"; ;;
      5) script_caller "./compose-file-processor.sh recreate"; ;;
      6) script_caller "./compose-file-processor.sh stop"; ;;
      7) script_caller "./compose-file-processor.sh delete"; ;;
      8) exit; ;;
      *) echo "Opción inválida" >&2
    esac
done