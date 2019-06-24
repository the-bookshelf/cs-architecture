#!/bin/bash
# Blockchain By Example for restful api demo

export COMPOSE_PROJECT_NAME="fscrestful"
# Generate the needed certificates, the genesis block and start the network.
function networkUp () {
  set -e
  starttime=$(date +%s)
  LANGUAGE=${1:-"golang"}
  #CC_SRC_PATH="github.com/chaincode/foodcontract/"
  CC_SRC_PATH=github.com/foodcontract
  # clean the keystore
  rm -rf ./hfc-key-store

  docker-compose -f $COMPOSE_FILE down

  docker-compose -f $COMPOSE_FILE up -d ca.example.com orderer.example.com peer0.org1.example.com couchdb

  sleep 5
  # Create the channel
  docker exec -e "CORE_PEER_LOCALMSPID=Org1MSP" -e "CORE_PEER_MSPCONFIGPATH=/etc/hyperledger/msp/users/Admin@org1.example.com/msp" peer0.org1.example.com peer channel create -o orderer.example.com:7050 -c mychannel -f /etc/hyperledger/configtx/channel.tx
  # Join peer0.org1.example.com to the channel.
  docker exec -e "CORE_PEER_LOCALMSPID=Org1MSP" -e "CORE_PEER_MSPCONFIGPATH=/etc/hyperledger/msp/users/Admin@org1.example.com/msp" peer0.org1.example.com peer channel join -b mychannel.block

  docker-compose -f ./$COMPOSE_FILE up -d cli

  docker exec -e "CORE_PEER_LOCALMSPID=Org1MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp" cli peer chaincode install -n fsccc -v 1.0 -p "$CC_SRC_PATH" -l "$LANGUAGE"
  docker exec -e "CORE_PEER_LOCALMSPID=Org1MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp" cli peer chaincode instantiate -o orderer.example.com:7050 -C mychannel -n fsccc -l "$LANGUAGE" -v 1.0 -c '{"Args":["init","order_001","John_1","100","5"]}' -P "OR	('Org1MSP.member','Org2MSP.member','Org3MSP.member')"
  sleep 10
  docker exec -e "CORE_PEER_LOCALMSPID=Org1MSP" -e "CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp" cli peer chaincode invoke -o orderer.example.com:7050 -C mychannel -n fsccc -c '{"Args":["createRawFood","order_001"]}'

  printf "\nTotal setup execution time : $(($(date +%s) - starttime)) secs ...\n\n\n"
  printf "====== Hyperledger fabric network is ready.... you can query data from app ======"
}

# Tear down running network
function networkDown () {
  docker-compose -f $COMPOSE_FILE kill && docker-compose -f $COMPOSE_FILE down
  rm -f ~/.hfc-key-store/*
  docker rmi $(docker images dev-* -q)
}

COMPOSE_FILE=docker-compose.yml
# Parse commandline args
while getopts "h?m:c:t:d:f:s:" opt; do
  case "$opt" in
    h|\?)
      printHelp
      exit 0
    ;;
    m)  MODE=$OPTARG
    ;;

  esac
done


#Create the network using docker compose
if [ "${MODE}" == "up" ]; then
  networkUp
  elif [ "${MODE}" == "down" ]; then
  networkDown
else
  printHelp
  exit 1
fi
