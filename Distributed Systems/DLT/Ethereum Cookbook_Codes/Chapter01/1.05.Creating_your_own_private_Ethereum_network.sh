# $ mkdir datadir
# To create a new account
geth account new --datadir datadir

# To initialize the genesis file
geth --datadir ./datadir init ./genesis.json

# To start the network
geth --datadir ./datadir --networkid 1100 console 2>> network.log
# > eth.accounts

# Starting a second network
geth --datadir ./datadir2 init ./genesis.json
geth --datadir ./datadir2 --networkid 1100 --port 30302 console 2>> network.log

# To get the enode address, run the following command in the JS console
# > admin.nodeInfo.enode
# Connect to a peer using its enode address using JS console
# > admin.addPeer("enode://315d8f023dfa1ae1b59dc11462f3e13697fc8fe4886034e01530ebe36b2f8cc154a8dd9c21f5b42564668f22ae7173943b9dd9a0fbfc1430cca8c47196872914@127.0.0.1:30303")
# > admin.peers

# $ puppeth