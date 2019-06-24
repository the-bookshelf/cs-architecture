// Connec to an exising node
// $ geth attach http://localhost:8545
// $ geth --verbosity 5 console 2>> /tmp/eth-node.log
// $ geth --verbosity 0 console

// Global web3 object
web3

// Admin APIs
admin
admin.nodeInfo
admin.peers

// Eth APIs
eth
eth.blockNumber
eth.getBlock(301)

// Other APIs
personal
miner
txpool
web3.toWei(1, "ether")


// To execute JS Code in non-interactive way
// $ geth --exec "eth.accounts" attach http://localhost:8545
// $  geth --jspath "/home" --exec 'loadScript("sendTransaction.js")' attach http://localhost:8545