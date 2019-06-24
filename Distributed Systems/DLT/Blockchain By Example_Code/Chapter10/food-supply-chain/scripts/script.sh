#!/bin/bash

echo
echo " ____    _____      _      ____    _____ "
echo "/ ___|  |_   _|    / \    |  _ \  |_   _|"
echo "\___ \    | |     / _ \   | |_) |   | |  "
echo " ___) |   | |    / ___ \  |  _ <    | |  "
echo "|____/    |_|   /_/   \_\ |_| \_\   |_|  "
echo
echo "Build Food Supply Chain Network (FSCN) end-to-end test"
echo
CHANNEL_NAME="fscchannel"
DELAY="$2"
LANGUAGE="golang"
: ${DELAY:="3"}
: ${LANGUAGE:="golang"}
: ${TIMEOUT:="20"}
LANGUAGE=`echo "$LANGUAGE" | tr [:upper:] [:lower:]`
COUNTER=1
MAX_RETRY=5
ORDERER_CA=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/fsc.com/orderers/orderer.fsc.com/msp/tlscacerts/tlsca.fsc.com-cert.pem

CC_SRC_PATH="github.com/chaincode/foodcontract/"

echo "Channel name : "$CHANNEL_NAME

# import utils
. scripts/utils.sh

createChannel() {
	setGlobals 0 1

	if [ -z "$CORE_PEER_TLS_ENABLED" -o "$CORE_PEER_TLS_ENABLED" = "false" ]; then
		peer channel create -o orderer.fsc.com:7050 -c $CHANNEL_NAME -f ./channel-artifacts/channel.tx >&log.txt
		res=$?
	else
		peer channel create -o orderer.fsc.com:7050 -c $CHANNEL_NAME -f ./channel-artifacts/channel.tx --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA >&log.txt
		res=$?
	fi
	cat log.txt
	verifyResult $res "Channel creation failed"
	echo "===================== Channel \"$CHANNEL_NAME\" is created successfully ===================== "
	echo
}

joinChannel () {
	for org in 1 2 3; do
	    for peer in 0 1; do
		joinChannelWithRetry $peer $org
		echo "===================== peer${peer}.org${org} joined on the channel \"$CHANNEL_NAME\" ===================== "
		sleep $DELAY
		echo
	    done
	done
}

## Create channel
echo "Creating channel..."
createChannel

## Join all the peers to the channel
echo "Having all peers join the channel..."
joinChannel


## Install chaincode on peer0.org1 and peer0.org2
echo "Installing chaincode on consumer peer: peer0.org1..."
installChaincode 0 1
echo "Installing chaincode on retailer peer: peer1.org1..."
installChaincode 1 1
echo "Installing chaincode on logistics peer: peer0.org2..."
installChaincode 0 2
echo "Installing chaincode on wholesaler peer: peer1.org2..."
installChaincode 1 2
echo "Installing chaincode on manufacture processor peer: peer0.org3..."
installChaincode 0 3
echo "Installing chaincode on raw food producer peer: peer1.org3..."
installChaincode 1 3


# Instantiate chaincode on peer1.org1
echo "Instantiating chaincode on peer1.org1..."
instantiateChaincode 1 1

# Query chaincode on peer0.org1
echo "Querying chaincode on peer0.org1..."
chaincodeQuery 0 1

# Invoke chaincode on peer1.org3
echo "Sending invoke createRawFood transaction on peer1.org3..."
chaincodeInvokeCreateRawFood 1 3

#Query on chaincode on RawFood/peer1
echo "Querying chaincode on RawFood/PEER5 peer1.org3..."
chaincodeQuery 1 3 

# Invoke chaincode on peer0.org3
echo "Sending invoke Manufacture Processing transaction on peer0.org3..."
chaincodeInvokeManufactureProcessing 0 3

#Query on chaincode on ManufactureProcessing/PEER4
echo "Querying chaincode on ManufactureProcessing/PEER4 peer0.org3..."
chaincodeQuery 0 3 

# Invoke chaincode on peer1.org2
echo "Sending invoke Wholesaler Distribution transaction on peer1.org2..."
chaincodeInvokeWholesalerDistribute 1 2

#Query on chaincode on WholesalerDistribute/PEER3
echo "Querying chaincode on WholesalerDistribute/PEER3 peer1.org2..."
chaincodeQuery 1 2 

# Invoke chaincode on peer0.org2
echo "Sending invoke Initiate Shipment transaction on peer0.org2..."
chaincodeInvokeInitiateShipment 0 2

#Query on chaincode on InitiateShipment/PEER2
echo "Querying chaincode on InitiateShipment/PEER2 peer0.org2..."
chaincodeQuery 0 2 

# Invoke chaincode on peer1.org1
echo "Sending invoke Deliver To Retailer transaction on peer1.org1..."
chaincodeInvokeDeliverToRetailer 1 1

#Query on chaincode on DeliverToRetailer/PEER1
echo "Querying chaincode on DeliverToRetailer/peer1 peer1.org1..."
chaincodeQuery 1 1 
# Invoke chaincode on peer0.org1
echo "Sending invoke Complete Order transaction on peer0.org1..."
chaincodeInvokeCompleteOrder 0 1

#Query on chaincode on CompleteOrder/PEER0
echo "Querying chaincode on CompleteOrder/PEER0 peer0.org1..."
chaincodeQuery 0 1 
echo
echo "========= All GOOD, FSCN execution completed =========== "
echo

echo
echo " _____   _   _   ____   "
echo "| ____| | \ | | |  _ \  "
echo "|  _|   |  \| | | | | | "
echo "| |___  | |\  | | |_| | "
echo "|_____| |_| \_| |____/  "
echo

exit 0
