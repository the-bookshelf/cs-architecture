#Configuring Node 1
mkdir -p qdata/node1/{keystore,geth}
cp accounts/keystore/key1 qdata/node1/keystore
cp static-nodes.json qdata/node1
cp enode_id_1 qdata/node1/geth/nodekey
./geth --datadir qdata/node1 init genesis.json 

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
cp static-nodes.json qdata/node4
cp enode_id_4 qdata/node4/geth/nodekey
./geth --datadir qdata/node4 init genesis.json 

#Configuring Node 5
mkdir -p qdata/node5/geth
cp static-nodes.json qdata/node5
./geth --datadir qdata/node5 init genesis.json 

#Configuring Node 6
mkdir -p qdata/node6/geth
cp static-nodes.json qdata/node6
./geth --datadir qdata/node6 init genesis.json 

#Configuring Node 7
mkdir -p qdata/node7/geth
cp static-nodes.json qdata/node7
cp enode_id_5 qdata/node7/geth/nodekey
./geth --datadir qdata/node7 init genesis.json