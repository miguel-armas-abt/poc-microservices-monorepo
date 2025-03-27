#!/bin/bash

source ./../commons.sh

COMPONENTS_CSV="./../../../application/components.csv"

iterate_csv_records() {
  firstline=true
  while IFS=',' read -r component_name component_type || [ -n "$component_name" ]; do
    # Ignore headers
    if $firstline; then
        firstline=false
        continue
    fi

    # Ignore comments
    if [[ $component_name != "#"* ]]; then
      ./compile-component.sh "$component_name" "$component_type"
    fi

  done < <(sed 's/\r//g' "$COMPONENTS_CSV")
}

iterate_csv_records