#
# Blockchain By Example - Food supply chain
# Utility script for run peer command
#

# verify the result of the end-to-end test
verifyResult () {
	if [ $1 -ne 0 ] ; then
		echo "!!!!!!!!!!!!!!! "$2" !!!!!!!!!!!!!!!!"
    echo "========= ERROR !!! FAILED to execute End-2-End Scenario ==========="
		echo
   		exit 1
	fi
}

# Set OrdererOrg.Admin globals
setOrdererGlobals() {
        CORE_PEER_LOCALMSPID="OrdererMSP"
        CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/fsc.com/orderers/orderer.fsc.com/msp/tlscacerts/tlsca.fsc.com-cert.pem
        CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/fsc.com/users/Admin@fsc.com/msp
}
# PEER0 for consumer, PEER1 for retailer
# PEER2 for logistics, PEER3 for wholesaler
# PEER4 for manufacture processor, PEER5 for raw food producer
setGlobals () {
	ORG=$2
	if [ $ORG -eq 1 ] ; then
		CORE_PEER_LOCALMSPID="Org1MSP"
		CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.fsc.com/peers/peer0.org1.fsc.com/tls/ca.crt
		CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.fsc.com/users/Admin@org1.fsc.com/msp
		#consumer and retailer on shopping site 
		if [ $1 -eq 0 ]; then
			#consumer
			CORE_PEER_ADDRESS=peer0.org1.fsc.com:7051
			PEER=PEER0
		else
			#retailer
			CORE_PEER_ADDRESS=peer1.org1.fsc.com:7051
			PEER=PEER1
		fi
	elif [ $ORG -eq 2 ] ; then
		CORE_PEER_LOCALMSPID="Org2MSP"
		CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.fsc.com/peers/peer0.org2.fsc.com/tls/ca.crt
		CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.fsc.com/users/Admin@org2.fsc.com/msp
		#logistics and wholesaler
		if [ $1 -eq 0 ]; then
			#logistics
			CORE_PEER_ADDRESS=peer0.org2.fsc.com:7051
			PEER=PEER2
		else
			#wholesaler
			CORE_PEER_ADDRESS=peer1.org2.fsc.com:7051
			PEER=PEER3
		fi
	elif [ $ORG -eq 3 ] ; then
		CORE_PEER_LOCALMSPID="Org3MSP"
		CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org3.fsc.com/peers/peer0.org3.fsc.com/tls/ca.crt
		CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org3.fsc.com/users/Admin@org3.fsc.com/msp
		#manufacture processor and raw food producer
		if [ $1 -eq 0 ]; then
			#manufacture processor
			CORE_PEER_ADDRESS=peer0.org3.fsc.com:7051
			PEER=PEER4
		else
			#raw food producer
			CORE_PEER_ADDRESS=peer1.org3.fsc.com:7051
			PEER=PEER5
		fi		
	else
		echo "================== ERROR !!! ORG OR PEER Unknown =================="
	fi

	env |grep CORE
}

## Sometimes Join takes time hence RETRY at least for 5 times
joinChannelWithRetry () {
	PEER=$1
	ORG=$2
	setGlobals $PEER $ORG
	peer channel join -b $CHANNEL_NAME.block  >&log.txt
	res=$?
	cat log.txt
	if [ $res -ne 0 -a $COUNTER -lt $MAX_RETRY ]; then
		COUNTER=` expr $COUNTER + 1`
		echo "peer${PEER}.org${ORG} failed to join the channel, Retry after $DELAY seconds"
		sleep $DELAY
		joinChannelWithRetry $PEER $ORG
	else
		COUNTER=1
	fi
	verifyResult $res "After $MAX_RETRY attempts, peer${PEER}.org${ORG} has failed to Join the Channel"
}

createChannel() {
	setGlobals 0 1

    if [ -z "$CORE_PEER_TLS_ENABLED" -o "$CORE_PEER_TLS_ENABLED" = "false" ]; then
		peer channel create -o orderer.fsc.com:7050 -c $CHANNEL_NAME -f ./channel-artifacts/channel.tx >&log.txt
	else
		peer channel create -o orderer.fsc.com:7050 -c $CHANNEL_NAME -f ./channel-artifacts/channel.tx --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA >&log.txt
	fi
	res=$?
	cat log.txt
	verifyResult $res "Channel creation failed"
	echo "Channel \"$CHANNEL_NAME\" is created successfully."
	echo
	echo
}
installChaincode () {
	PEER=$1
	ORG=$2
	setGlobals $PEER $ORG
	peer chaincode install -n fsccc -v 1.0 -l ${LANGUAGE} -p github.com/chaincode/foodcontract >&log.txt
	res=$?
	cat log.txt
	verifyResult $res "Chaincode installation on peer${PEER}.org${ORG} has Failed"
	echo "===================== Chaincode is installed on remote peer${PEER}.org${ORG} ===================== "
	echo
}

instantiateChaincode () {
	PEER=$1
	ORG=$2
	setGlobals $PEER $ORG

	# while 'peer chaincode' command can get the orderer endpoint from the peer (if join was successful),
	# lets supply it directly as we know it using the "-o" option
	if [ -z "$CORE_PEER_TLS_ENABLED" -o "$CORE_PEER_TLS_ENABLED" = "false" ]; then
		peer chaincode instantiate -o orderer.fsc.com:7050 -C $CHANNEL_NAME -n fsccc -l ${LANGUAGE} -v 1.0 -c '{"Args":["init","order_001","John_1","100","5"]}' -P "OR	('Org1MSP.member','Org2MSP.member','Org3MSP.member')" >&log.txt
		res=$?
	else
		peer chaincode instantiate -o orderer.fsc.com:7050 --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA -C $CHANNEL_NAME -n fsccc -l ${LANGUAGE} -v 1.0 -c '{"Args":["init","order_001","John_1","100","5"]}' -P "OR	('Org1MSP.member','Org2MSP.member','Org3MSP.member')" >&log.txt
		res=$?
	fi
	cat log.txt
	verifyResult $res "Chaincode instantiation on peer${PEER}.org${ORG} on channel '$CHANNEL_NAME' failed"
	echo "===================== Chaincode Instantiation on peer${PEER}.org${ORG} on channel '$CHANNEL_NAME' is successful ===================== "
	echo
}


chaincodeQuery () {
  PEER=$1
  ORG=$2
  setGlobals $PEER $ORG
  echo "========== Querying on peer${PEER}.org${ORG} on channel '$CHANNEL_NAME'... ======= "
  local rc=1
  local starttime=$(date +%s)

  # continue to poll
  # we either get a successful response, or reach TIMEOUT
  while test "$(($(date +%s)-starttime))" -lt "$TIMEOUT" -a $rc -ne 0
  do
     sleep $DELAY
     echo "Attempting to Query peer${PEER}.org${ORG} ...$(($(date +%s)-starttime)) secs"
     peer chaincode query -C $CHANNEL_NAME -n fsccc -c '{"Args":["query","order_001"]}' >&log.txt
  done
  echo
  cat log.txt
}

#create rawfood by invoke chaincode
chaincodeInvokeCreateRawFood() {
	PEER=$1
	ORG=$2
	setGlobals $PEER $ORG
	if [ -z "$CORE_PEER_TLS_ENABLED" -o "$CORE_PEER_TLS_ENABLED" = "false" ]; then
		peer chaincode invoke -o orderer.fsc.com:7050 -C $CHANNEL_NAME -n fsccc -c '{"Args":["createRawFood","order_001"]}' >&log.txt
	else
		peer chaincode invoke -o orderer.fsc.com:7050  --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA -C $CHANNEL_NAME -n fsccc -c '{"Args":["createRawFood","order_001"]}' >&log.txt
	fi
	res=$?
	cat log.txt
	verifyResult $res "Invoke:CreateRawFood execution on PEER$PEER failed "
	echo "Invoke:CreateRawFood transaction on PEER $PEER on channel '$CHANNEL_NAME' is successful. "
	echo
}

#Manufacture Processing by invoke chaincode
chaincodeInvokeManufactureProcessing() {
	PEER=$1
	ORG=$2
	setGlobals $PEER $ORG
	if [ -z "$CORE_PEER_TLS_ENABLED" -o "$CORE_PEER_TLS_ENABLED" = "false" ]; then
		peer chaincode invoke -o orderer.fsc.com:7050 -C $CHANNEL_NAME -n fsccc -c '{"Args":["manufactureProcessing","order_001"]}' >&log.txt
	else
		peer chaincode invoke -o orderer.fsc.com:7050  --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA -C $CHANNEL_NAME -n fsccc -c '{"Args":["manufactureProcessing","order_001"]}' >&log.txt
	fi
	res=$?
	cat log.txt
	verifyResult $res "Invoke:ManufactureProcessing execution on PEER$PEER failed "
	echo "Invoke:ManufactureProcessing transaction on PEER $PEER on channel '$CHANNEL_NAME' is successful. "
	echo
}

#Wholesaler Distribute by invoke chaincode
chaincodeInvokeWholesalerDistribute () {
	PEER=$1
	ORG=$2
	setGlobals $PEER $ORG
	if [ -z "$CORE_PEER_TLS_ENABLED" -o "$CORE_PEER_TLS_ENABLED" = "false" ]; then
		peer chaincode invoke -o orderer.fsc.com:7050 -C $CHANNEL_NAME -n fsccc -c '{"Args":["wholesalerDistribute","order_001"]}' >&log.txt
	else
		peer chaincode invoke -o orderer.fsc.com:7050  --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA -C $CHANNEL_NAME -n fsccc -c '{"Args":["wholesalerDistribute","order_001"]}' >&log.txt
	fi
	res=$?
	cat log.txt
	verifyResult $res "Invoke:WholesalerDistribute execution on PEER$PEER failed "
	echo "Invoke:WholesalerDistribute transaction on PEER $PEER on channel '$CHANNEL_NAME' is successful. "
	echo
}

#Wholesaler Distribute by invoke chaincode
chaincodeInvokeInitiateShipment () {
	PEER=$1
	ORG=$2
	setGlobals $PEER $ORG
	if [ -z "$CORE_PEER_TLS_ENABLED" -o "$CORE_PEER_TLS_ENABLED" = "false" ]; then
		peer chaincode invoke -o orderer.fsc.com:7050 -C $CHANNEL_NAME -n fsccc -c '{"Args":["initiateShipment","order_001"]}' >&log.txt
	else
		peer chaincode invoke -o orderer.fsc.com:7050  --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA -C $CHANNEL_NAME -n fsccc -c '{"Args":["initiateShipment","order_001"]}' >&log.txt
	fi
	res=$?
	cat log.txt
	verifyResult $res "Invoke:InitiateShipment execution on PEER$PEER failed "
	echo "Invoke:InitiateShipment transaction on PEER $PEER on channel '$CHANNEL_NAME' is successful. "
	echo
}

#deliverToRetailer by invoke chaincode
chaincodeInvokeDeliverToRetailer () {
	PEER=$1
	ORG=$2
	setGlobals $PEER $ORG
	if [ -z "$CORE_PEER_TLS_ENABLED" -o "$CORE_PEER_TLS_ENABLED" = "false" ]; then
		peer chaincode invoke -o orderer.fsc.com:7050 -C $CHANNEL_NAME -n fsccc -c '{"Args":["deliverToRetail","order_001"]}' >&log.txt
	else
		peer chaincode invoke -o orderer.fsc.com:7050  --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA -C $CHANNEL_NAME -n fsccc -c '{"Args":["deliverToRetail","order_001"]}' >&log.txt
	fi
	res=$?
	cat log.txt
	verifyResult $res "Invoke:DeliverToRetail execution on PEER$PEER failed "
	echo "Invoke:DeliverToRetail transaction on PEER $PEER on channel '$CHANNEL_NAME' is successful. "
	echo
}

#completeOrder by invoke chaincode
chaincodeInvokeCompleteOrder () {
	PEER=$1
	ORG=$2
	setGlobals $PEER $ORG
	if [ -z "$CORE_PEER_TLS_ENABLED" -o "$CORE_PEER_TLS_ENABLED" = "false" ]; then
		peer chaincode invoke -o orderer.fsc.com:7050 -C $CHANNEL_NAME -n fsccc -c '{"Args":["completeOrder","order_001"]}' >&log.txt
	else
		peer chaincode invoke -o orderer.fsc.com:7050  --tls $CORE_PEER_TLS_ENABLED --cafile $ORDERER_CA -C $CHANNEL_NAME -n fsccc -c '{"Args":["completeOrder","order_001"]}' >&log.txt
	fi
	res=$?
	cat log.txt
	verifyResult $res "Invoke:CompleteOrder execution on PEER$PEER failed "
	echo "Invoke:CompleteOrder transaction on PEER $PEER on channel '$CHANNEL_NAME' is successful. "
	echo
}