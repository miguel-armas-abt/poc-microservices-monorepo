#!/bin/bash
set -e

get_compile_command() {
  command="mvn clean install -Dmaven.home=\"$MAVEN_HOME\" -Dmaven.repo.local=\"$MAVEN_REPOSITORY\""
  echo "$command"
}

case "$1" in
  compile_command) get_compile_command ;;
  *) echo -e "Invalid option" >&2
esac