#!/bin/bash

source ./../commons.sh

print_title() {
  echo -e "\n########## ${GREEN} Ejecute una acción con K8S/HELM ${NC}##########\n"
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
  "Construir imágenes en Minikube"
  "Instalar componentes en el clúster"
  "Desinstalar componentes del clúster"
  "Salir"
)

select option in "${options[@]}"; do
    case $REPLY in
      1) script_caller "./helm-csv-processor.sh template"; ;;
      2) script_caller "./k8s-processor.sh build-in-minikube"; ;;
      3) script_caller "./helm-csv-processor.sh install"; ;;
      4) script_caller "./helm-csv-processor.sh uninstall"; ;;
      5) exit; ;;
      *) echo "Opción inválida" >&2
    esac
done