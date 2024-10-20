#!/bin/bash

source ./../commons.sh

print_title() {
  echo -e "\n########## ${CYAN} Ejecute una acción con K8S/HELM ${NC}##########\n"
}

script_caller() {
  $1
  print_title
}

print_title

options=(
  "Generar manifiestos"
  "Construir imágenes en Minikube"
  "Instalar componentes en el clúster"
  "Desinstalar componentes del clúster"
  "Port forwarding"
  "Salir"
)

while true; do
  select option in "${options[@]}"; do
      case $REPLY in
        1) script_caller "./helm-csv-processor.sh template"; break ;;
        2) script_caller "./minikube-processor.sh build-in-minikube"; break ;;
        3) script_caller "./helm-csv-processor.sh install"; break ;;
        4) script_caller "./helm-csv-processor.sh uninstall"; break ;;
        5) script_caller "./k8s-csv-processor.sh port-forward"; break ;;
        6) exit; ;;
        *) echo "Opción inválida" >&2
      esac
  done
done