# ref:
# https://medium.com/@lilnya79/building-jenkins-image-with-docker-cli-preinstalled-for-seamless-devops-integration-ec185f76755b
FROM jenkins/jenkins:latest

USER root

RUN apt-get update && \
    apt-get install -y sudo vim curl wget

RUN echo 'root:abc123' | chpasswd

RUN echo "jenkins ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers

RUN apt-get update && \
    apt-get install -y ca-certificates curl gnupg2 && \
    install -m 0755 -d /etc/apt/keyrings && \
    curl -fsSL https://download.docker.com/linux/debian/gpg \
        | gpg --dearmor -o /etc/apt/keyrings/docker.gpg && \
    chmod a+r /etc/apt/keyrings/docker.gpg && \
    echo \
      "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] \
      https://download.docker.com/linux/debian $(. /etc/os-release; echo "$VERSION_CODENAME") stable" \
      > /etc/apt/sources.list.d/docker.list && \
    apt-get update && \
    apt-get install -y docker-ce-cli

RUN groupadd docker

RUN usermod -aG docker jenkins

USER jenkins