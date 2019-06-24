FROM ubuntu:16.04

RUN apt-get update
RUN apt-get install -y --no-install-recommends vim less net-tools inetutils-ping wget curl git telnet nmap socat dnsutils netcat tree htop unzip sudo software-properties-common jq psmisc iproute python ssh rsync gettext-base

# Install QNM
RUN mkdir -p workspace && cd workspace && wget https://raw.githubusercontent.com/ConsenSys/QuorumNetworkManager/v0.7.5-beta/setup.sh && chmod +x setup.sh && ./setup.sh
ENV LANGUAGE=en_US.UTF-8 LC_ALL=en_US.UTF-8 LANG=en_US.UTF-8
RUN apt-get install -y locales && locale-gen en_US.UTF-8

WORKDIR /workspace/QuorumNetworkManager
ENTRYPOINT ["/bin/bash", "-i", "-c"]