#!/bin/bash
set -e

get_compile_command() {
  command="$GO build -o runner ./main.go"
  echo "$command"
}

get_compile_command