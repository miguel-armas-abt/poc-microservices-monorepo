#!/bin/bash
set -e

source ./../commons.sh
source ./../variables.env

build_images_in_minikube() {
  local original_dir
  original_dir="$(pwd)"

  eval $(minikube docker-env --shell bash)
  cd ./../docker/
  eval "./build-images-csv-processor.sh"
  cd "$original_dir"
}

build_images_in_minikube