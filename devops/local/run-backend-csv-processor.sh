#!/bin/bash
set -e

source ./../commons.sh
source ./../variables.env

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
    if [[ $component_name != "#"* ]] && [[ $component_type == "$BUSINESS_TYPE" || $component_type == "$PLATFORM_TYPE" ]]; then
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
    if [[ $component_name != "#"* ]] && [[ $component_type == "$BUSINESS_TYPE" || $component_type == "$PLATFORM_TYPE" ]]; then
      if (( index == component_index )); then
        selected_component_name=$component_name
        selected_component_type=$component_type
        break
      fi
      index=$((index + 1))
    fi

  done < <(sed 's/\r//g' "$COMPONENTS_CSV")

  if [[ -n $selected_component_name && -n $selected_component_type ]]; then
    ./run-backend.sh "$selected_component_name" "$selected_component_type"
  else
    echo -e "${RED}invalid option${NC}"
  fi
}

select_component
