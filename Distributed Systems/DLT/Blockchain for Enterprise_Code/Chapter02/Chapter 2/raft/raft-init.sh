#Configuring Node 1
mkdir -p qdata/node1/{keystore,geth} #'keystore' stores acccounts and geth stores all other ethereum related data
cp static-nodes.json qdata/node1
cp accounts/keystore/key1 qdata/node1/keystore
cp enode_id_1 qdata/node1/geth/nodekey
./geth --datadir qdata/node1 init genesis.json #bootstrap the blockchain

#Configuring Node 2
mkdir -p qdata/node2/geth
cp static-nodes.json qdata/node2
cp enode_id_2 qdata/node2/geth/nodekey
./geth --datadir qdata/node2 init genesis.json

#Configuring Node 3
mkdir -p qdata/node3/geth
cp static-nodes.json qdata/node3
cp enode_id_3 qdata/node3/geth/nodekey
./geth --datadir qdata/node3 init genesis.json

#Configuring Node 4
mkdir -p qdata/node4/geth
cp enode_id_4 qdata/node4/geth/nodekey
./geth --datadir qdata/node4 init genesis.json