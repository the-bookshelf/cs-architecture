// For v0.2x.x
var web3 = new Web3(web3.currentProvider);
// For v1.x.x
var web3 = new Web3(Web3.givenProvider);

// For v0.2x.x
if (typeof web3 !== 'undefined') {
    web3 = new Web3(web3.currentProvider);
} else {
    web3 = new Web3(
        new Web3.providers.HttpProvider("http://localhost:8545")
    );
}
// For v1.x.x
var web3 = new Web3(Web3.givenProvider || "ws://localhost:8546");

// For v0.2x.x
web3.eth.getBalance("<address>", function (error, result) {
    if (!error) {
        console.log(result);
} });
// For v1.x.x
web3.eth.getBalance("<address>").then(console.log);