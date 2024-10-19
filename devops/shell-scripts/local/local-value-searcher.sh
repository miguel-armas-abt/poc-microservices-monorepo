#!/bin/bash

source ./../commons.sh

LOCAL_CSV="./../../../application/local.csv"

search_value() {
  local key=$1

  value=$(awk -F',' -v key="$key" '$1 == key {print $2}' "$LOCAL_CSV")
  echo "$value"
}

key=$1
search_value "$key"