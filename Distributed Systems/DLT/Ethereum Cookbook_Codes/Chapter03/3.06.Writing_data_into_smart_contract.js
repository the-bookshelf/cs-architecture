// pragma solidity ^0.4.21;
// contract HelloWorld {
//     // State variable
//     string textToPrint = "hello world";
//     // State changing function
//     function changeText(string _text) public {
//         textToPrint = _text;
//     }
//     // Read-only function
//     function printSomething() public view returns (string) {
//         return textToPrint;
//     }
// }

// For v0.2x.x
var helloWorld = web3.eth.contract("<ABI>");
var helloWorldInstance = helloWorld.at("<Address>");

// For v1.x.x
var helloWorldInstance = new web3.eth.Contract("<ABI>", "<Address>");


// Example - v0.2x.x
helloWorldInstance.changeText.sendTransaction("Greetings!", {
    from: "0xce5C2D181f6DD99091351f6E6056c333A969AEC9",
    gas: 470000
}, function (error, result) {
    //...
});

// Example - v1.x.x
helloWorldInstance.methods.changeText("Greetings!").send({
    from: "0xce5C2D181f6DD99091351f6E6056c333A969AEC9"
}, function (error, transactionHash) {
    //...
});

// Events in v1.x.x
helloWorldInstance.methods.changeText("Greetings!").send({
    from: "0xce5C2D181f6DD99091351f6E6056c333A969AEC9"
}).on('transactionHash', function (hash) {
    //..
}).on('confirmation', function (confirmationNumber, receipt) {
    //..
}).on('receipt', function (receipt) {
    //..
}).on('error', console.error);

// Method syntax - v1.x.x
myContractInstance.methods
.stateChangingMethod([param1[, param2[, ...]]])
.send(options [, callback]);

// Example - v1.x.x
helloWorldInstance.methods
.changeText("Greetings!")
.send({
from: "0xce5C2D181f6DD99091351f6E6056c333A969AEC9"
}, function(error, transactionHash){ ... });

// Estimate gas
helloWorldInstance.methods.changeText("Greetings!").estimateGas({
    from: "0xce5C2D181f6DD99091351f6E6056c333A969AEC9"
}, function (error, gasAmount) {
    console.log(gasAmount);
});

// Estimate gas using Promise
helloWorldInstance.methods.changeText("Greetings!").estimateGas({
    from: "0xce5C2D181f6DD99091351f6E6056c333A969AEC9"
}).on('transactionHash', function(hash){
...}).on('confirmation', function(confirmationNumber, receipt){
...}).on('receipt', function(receipt){ console.log(receipt);
}).on('error', console.error);
