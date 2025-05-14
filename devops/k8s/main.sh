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
  "Generar manifiestos"
  "Construir imágenes en Minikube"
  "Instalar objetos k8s"
  "Desinstalar objetos k8s"
  "Port forwarding"
  "Salir"
)

while true; do
  select option in "${options[@]}"; do
      case $REPLY in
        1) script_caller "./helm-csv-processor.sh template"; break ;;
        2) script_caller "./build-image-minikube-service.sh"; break ;;
        3) script_caller "./helm-csv-processor.sh install"; break ;;
        4) script_caller "./helm-csv-processor.sh uninstall"; break ;;
        5) script_caller "./k8s-port-forward-csv-processor.sh"; break ;;
        6) exit; ;;
        *) echo "Opción inválida" >&2
      esac
  done
done