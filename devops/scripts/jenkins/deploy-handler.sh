#!/bin/bash

source ./../commons.sh
SCRAPING_COMPONENT="jenkins-settings-scraping-v1"
JENKINS_SCRAPING_FILE="./../web-scraping/$SCRAPING_COMPONENT/src/main/resources/application.yaml"

forward_server() {
  local server=$1

  ngrok_home=$(./../local/local-value-searcher.sh "ngrok_home")
  command="$ngrok_home/ngrok http $server"
  eval start "$command"

  sleep 2

  ngrok_url=$(curl -s http://127.0.0.1:4040/api/tunnels | yq '.tunnels[0].public_url')

  if [ -n "$ngrok_url" ]; then
    echo -e "${CYAN}forwarded url:${NC} $ngrok_url${NC}"
    sed -i "s|\(forwardedServer:\s*\).*|\1$ngrok_url|" "$JENKINS_SCRAPING_FILE"
  else
    echo -e "${RED}Error getting forwarded url${NC}"
  fi
}

retrieve_cloud_config() {
  certificate_authority=$(kubectl config view --raw | yq '.clusters[0].cluster."certificate-authority"')
  server=$(kubectl config view --raw | yq '.clusters[0].cluster.server')

  escaped_certificate_authority=$(printf '%s' "$certificate_authority" | sed 's|\\|\\\\|g')

  if [ -n "$certificate_authority" ]; then
    echo -e "${CYAN}certificate:${NC} $certificate_authority${NC}"
    sed -i "s|\(certificate:\s*\).*|\1$escaped_certificate_authority|" "$JENKINS_SCRAPING_FILE"
  else
    echo -e "${RED}Error getting certificate${NC}"
  fi

  echo

  if [ -n "$server" ]; then
    echo -e "${CYAN}local server:${NC} $server${NC}"
    sed -i "s|\(localServer:\s*\).*|\1$server|" "$JENKINS_SCRAPING_FILE"
  else
    echo -e "${RED}Error getting local server${NC}"
  fi

  forward_server "$server"
}

install_scraper() {
  component_path="./../web-scraping/$SCRAPING_COMPONENT"
  maven_home=$(./../local/local-value-searcher.sh "maven_home")
  maven_repository=$(./../local/local-value-searcher.sh "maven_repository")

  command="mvn clean install -Dmaven.home=\"$maven_home\" -Dmaven.repo.local=\"$maven_repository\""
  echo "$(get_timestamp) .......... $command" >> "./../../$LOG_FILE"
  cd "$component_path"
  eval $command
  cd "./../../docker/"
}

scraping_jenkins() {
  java_home=$(./../local/local-value-searcher.sh "java_home")
  export JAVA_HOME=$java_home
  java="$java_home/bin/java"

  component_path="./../web-scraping/$SCRAPING_COMPONENT"

  command="$java -jar ./target/$SCRAPING_COMPONENT-0.0.1-SNAPSHOT.jar"

  echo "$(get_timestamp) .......... $command" >> "./../../$LOG_FILE"
  cd $component_path
  eval "$command"
  cd "./../../docker/"
}

retrieve_k8s_password() {
  k8s_password=$(kubectl describe secret jenkins-token-rk2mg | grep 'token:' | awk '{print $2}')

  if [ -n "$k8s_password" ]; then
    echo -e "${CYAN}k8s password:${NC} $k8s_password${NC}"
    sed -i "s|\(k8sPassword:\s*\).*|\1$k8s_password|" "$JENKINS_SCRAPING_FILE"
  else
    echo -e "${RED}Error getting k8s password${NC}"
  fi
}

retrieve_unlock_password() {
  container_name="bbq-jenkins"
  password=$(docker logs $container_name 2>&1 | grep -o "^[0-9a-f]\{32\}$")

  if [ -n "$password" ]; then
    echo -e "${CYAN}unlock password:${NC} $password${NC}"
    sed -i "s|\(unlockPassword:\s*\).*|\1$password|" "$JENKINS_SCRAPING_FILE"
  else
    echo -e "${RED}Error getting unlock password${NC}"
  fi
}

grant_authorization_in_k8s() {
  kubectl apply -f ./resources/jenkins-auth.yml
  retrieve_k8s_password
}

handle_deploy() {
  ./jenkins-processor.sh build
  ./jenkins-processor.sh up
  ./jenkins-processor.sh wait
  retrieve_unlock_password
  grant_authorization_in_k8s
  retrieve_cloud_config
  install_scraper
  scraping_jenkins
}

handle_deploy