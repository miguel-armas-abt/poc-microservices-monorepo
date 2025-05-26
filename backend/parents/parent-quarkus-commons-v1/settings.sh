#!/bin/bash
set -e

get_compile_command() {
  command="mvn clean install -Dmaven.home=\"$MAVEN_HOME\" -Dmaven.repo.local=\"$MAVEN_REPOSITORY\""
  echo "$command"
}

get_compile_command