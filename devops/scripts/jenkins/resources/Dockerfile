FROM jenkins/jenkins:2.484
USER root
#Define variables
ENV MAVEN_VERSION=3.9.6

#Update Base OS and install additional tools
RUN apt-get update && apt-get install -y wget
RUN wget --no-verbose https://dlcdn.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz -P /tmp/
RUN tar xzf /tmp/apache-maven-${MAVEN_VERSION}-bin.tar.gz -C /opt/
RUN ln -s  /opt/apache-maven-${MAVEN_VERSION} /opt/maven
RUN ln -s /opt/maven/bin/mvn /usr/local/bin 
RUN rm /tmp/apache-maven-${MAVEN_VERSION}-bin.tar.gz

#Install Docker CLI
ENV DOCKERVERSION=18.03.1-ce
RUN curl -fsSLO https://download.docker.com/linux/static/stable/x86_64/docker-${DOCKERVERSION}.tgz && tar xzvf docker-${DOCKERVERSION}.tgz --strip 1 -C /usr/local/bin docker/docker && rm docker-${DOCKERVERSION}.tgz

#Install kubectl
RUN curl -LO https://storage.googleapis.com/kubernetes-release/release/v1.20.0/bin/linux/amd64/kubectl && chmod +x ./kubectl && mv ./kubectl /usr/local/bin/kubectl

#Set up permissions
RUN chown jenkins:jenkins /opt/maven;
ENV MAVEN_HOME=/opt/mvn
USER jenkins