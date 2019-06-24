// Web3js 0.2x.x
var batch = web3.createBatch();

var balance = web3.eth.getBalance.request(
    web3.eth.accounts[1],
    'latest',
    callback
);
var contract = web3.eth.Contract(abi)
    .at(address)
    .balance
    .request(web3.eth.accounts[0], callback2);

batch.add(balance);
batch.add(contract);

batch.execute();

// new web3.BatchRequest()
// new web3.eth.BatchRequest()
// new web3.shh.BatchRequest()
// new web3.bzz.BatchRequest()

// Web3js v1.x.x
var balance = web3.eth.getBalance.request(
    web3.eth.accounts[1],
    'latest',
    callback
);
var contract = new web3.eth.Contract(abi, address)
    .methods.balance("<address>")
    .call.request({
        from: "<address>"
    }, callback2)
var batch = new web3.BatchRequest();
batch.add(balance);
batch.add(contract);
batch.execute();