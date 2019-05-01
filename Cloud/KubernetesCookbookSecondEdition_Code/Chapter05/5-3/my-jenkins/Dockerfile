FROM jenkins/jenkins:lts

EXPOSE 8080 50000

# install Docker CE for Debian : https://docs.docker.com/engine/installation/linux/docker-ce/debian/
USER root
RUN apt-get update
RUN apt-get install -y sudo apt-transport-https ca-certificates curl gnupg2 software-properties-common
RUN curl -fsSL https://download.docker.com/linux/$(. /etc/os-release; echo "$ID")/gpg | apt-key add -
RUN add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/$(. /etc/os-release; echo "$ID") $(lsb_release -cs) stable"
RUN apt-get update && apt-get install -y docker-ce


# install kubectl binary
RUN curl -LO https://storage.googleapis.com/kubernetes-release/release/v1.8.6/bin/linux/amd64/kubectl
RUN chmod +x ./kubectl
RUN mv ./kubectl /usr/local/bin/kubectl


# setup Jenkins plubins : https://github.com/jenkinsci/docker#script-usage
RUN /usr/local/bin/install-plugins.sh docker
