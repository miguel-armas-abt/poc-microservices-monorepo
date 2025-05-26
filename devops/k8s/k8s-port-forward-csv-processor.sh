#!/bin/bash
set -e

source ./../commons.sh
source ./../variables.env

port_forward() {
  local component_name=$1
  local component_type=$2

  values_file="$BACKEND_PATH/$component_type/$component_name/values.yaml"

  container_port=$(yq '.ports.containerPort' "$values_file")
  host_port=$(yq '.docker.hostPort' "$values_file")
  namespace=$(awk '/namespace:/ {print $2}' "$values_file" | sed 's/"//g')

  command="kubectl port-forward svc/$component_name $host_port:$container_port -n $namespace"
  print_log "$command"
  start bash -c "echo -ne \"\\033]0;$component_name\\007\";$command" #set title in the emergent windows
}

list_components() {
  echo -e "\n########## ${CYAN} Servicios disponibles ${NC}##########\n"
  local index=1
  firstline=true
  while IFS=',' read -r component_name component_type || [ -n "$component_name" ]; do
    # Ignore headers
    if $firstline; then
      firstline=false
      continue
    fi

    # Ignore comments
    if [[ $component_name != "#"* ]] && [[ $component_type == "$MICROSERVICES_TYPE" ]]; then
      echo "$index) $component_name"
      index=$((index + 1))
    fi

  done < <(sed 's/\r//g' "$COMPONENTS_CSV")
}

select_component() {
  local component_index
  local selected_component_name
  local selected_component_type

  list_components

  read -rp "Ingrese el número de servicio: " component_index

  local index=1
  firstline=true
  while IFS=',' read -r component_name component_type || [ -n "$component_name" ]; do
    # Ignore headers
    if $firstline; then
      firstline=false
      continue
    fi

    # Ignore comments
    if [[ $component_name != "#"* ]] && [[ $component_type == "$MICROSERVICES_TYPE" ]]; then
      if (( index == component_index )); then
        selected_component_name=$component_name
        selected_component_type=$component_type
        break
      fi
      index=$((index + 1))
    fi

  done < <(sed 's/\r//g' "$COMPONENTS_CSV")

  if [[ -n $selected_component_name && -n $selected_component_type ]]; then
    port_forward "$selected_component_name" "$selected_component_type"
  else
    echo -e "${RED}Opción inválida.${NC}"
  fi
}

select_component