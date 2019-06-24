geth version
geth
 
# Start a geth node
geth --networkid 3 --datadir "./ropsten-db" --keystore "./ropsten-keys" --syncmode "fast" --rpc --rpcport "8546" --rpcapi "web3,eth,miner,admin" --rpccorsdomain "*" --port 30301 console

# Connect to an existing node
geth attach http://localhost:8546

# > admin.peers
# > eth.syncing
