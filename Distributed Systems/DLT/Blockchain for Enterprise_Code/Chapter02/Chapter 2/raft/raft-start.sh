#Starting node 1
PRIVATE_CONFIG=constellation1.conf ./geth --datadir qdata/node1 --port 23000 --raftport 21000 --raft --ipcpath "./geth.ipc"

#Starting node 2
PRIVATE_CONFIG=constellation2.conf ./geth --datadir qdata/node2 --port 23001 --raftport 21001 --raft --ipcpath "./geth.ipc"

#Starting node 3
PRIVATE_CONFIG=constellation3.conf ./geth --datadir qdata/node3 --port 23002 --raftport 21002 --raft --ipcpath "./geth.ipc"

#Starting node 4
PRIVATE_CONFIG=constellation4.conf ./geth --datadir qdata/node4 --port 23003 --raftport 21003 --raft --ipcpath "./geth.ipc"