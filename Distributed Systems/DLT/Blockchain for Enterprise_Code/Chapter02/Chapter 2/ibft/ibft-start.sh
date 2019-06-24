./geth --datadir qdata/node1 --mine --port 23000 --ipcpath "./geth.ipc" --istanbul.requesttimeout 5000 --istanbul.blockperiod 1 --istanbul.blockpausetime 20 console
./geth --datadir qdata/node2 --mine --port 23001 --ipcpath "./geth.ipc" --istanbul.requesttimeout 5000 --istanbul.blockperiod 1 --istanbul.blockpausetime 20 console
./geth --datadir qdata/node3 --mine --port 23002 --ipcpath "./geth.ipc" --istanbul.requesttimeout 5000 --istanbul.blockperiod 1 --istanbul.blockpausetime 20 console
./geth --datadir qdata/node4 --mine --port 23003 --ipcpath "./geth.ipc" --istanbul.requesttimeout 5000 --istanbul.blockperiod 1 --istanbul.blockpausetime 20 console
./geth --datadir qdata/node5 --port 23004 --ipcpath "./geth.ipc" console
./geth --datadir qdata/node6 --port 23005 --ipcpath "./geth.ipc" console
./geth --datadir qdata/node7 --mine --port 23006 --ipcpath "./geth.ipc" --istanbul.requesttimeout 5000 --istanbul.blockperiod 1 --istanbul.blockpausetime 20 console