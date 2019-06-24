// geth --rpc --rpcapi="db,eth,net,web3,personal"
// admin.startRPC("<ip_address>", <rpc_port>, "*", "db,eth,net,web3,personal")

// v0.2x.x
web3.eth.accounts;
// v1.x.x
web3.eth.getAccounts().then(console.log);

// For v1.x.x
web3.eth.accounts.create();
web3.eth.accounts.create("<random entropy>");

// {
//     address: '0x824e470cCac64CC5fa4Abe953e64FA360EA11366',
//     privateKey: '0x782174a3e404424af...499baffd30e6105f',
//     signTransaction: [Function: signTransaction],
//     sign: [Function: sign],
//     encrypt: [Function: encrypt]
// }

// For v1.x.x
web3.eth.accounts.privateKeyToAccount("<privateKey>");

// For v0.2x.x
web3.personal.unlockAccount("<address>", "<password>", "<duration>");
// For v1.x.x
web3.eth.personal.unlockAccount("<address>", "<password>", "<duration>")
    .then(console.log);

// web3.eth.sendTransaction(transaction_object [, callback])

web3.eth.sendTransaction({
    from: "0xce5C2D181f6DD99091351f6E6056c333A969AEC9",
    to: "",
    gas: 21000,
    gasPrice: 20000000000, // 20 Gwei
    value: 200000
}, function (error, transactionHash) {
    if (!error)
        console.log(transactionHash);
})

web3.eth.sendTransaction({
    from: "0xce5C2D181f6DD99091351f6E6056c333A969AEC9",
    to: 0x71495cd51c5356B1f0769dB5027DC0588010dC14,
    value: '10000000000000000'
}).on('transactionHash', function (hash) {
    console.log(hash);
}).on('receipt', function (receipt) {
    console.log(receipt);
}).on('confirmation', function (confirmationNumber, receipt) {
    console.log(confirmationNumber);
}).on('error', console.error);
