var abi = [...]; // Contract interface
var address = "0x0..."; // Deployed ERC20 contract address

var tokenContract = new web3.eth.Contract(abi, address);

var userAddress = "0x0..."; // Address of the user
tokenContract.methods.getBalance(userAddress)
    .call().then(function (result) {
        console.log(result);
    })


var fromAddress = "0x1.."; // Address of the sender
var toAddress = "0x2.."; // Address of the receiver
var amount = 10; // Amount to transfer
tokenContract.methods
    .transder(toAddress, amount).send({
        from: fromAddress
    }).then(function (receipt) {
        console.log(receipt);
    });

tokenContract.events.Transfer({
    filter: {},
    fromBlock: 0
}).on('data', function (event) {
    // ...
}).on('changed', function (event) {
    // ...
}).on('error', function (event) {
    // ...
});