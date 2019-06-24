// SAMPLE CONTRACT - HelloWorld.sol
// pragma solidity ^0.4.23;
// contract HelloWorld {
//     string public greeting = 'Hello World';
//     function changeGreeting(string _newGreeting) public {
//         greeting = _newGreeting;
//     }
// }

// $ solc --optimize --bin --abi HelloWorld.sol
// $ geth console 2>> ./geth.log


// Unlock Account
web3.personal.unlockAccount(web3.eth.accounts[0], '<password>');

// Create an object with ABI
var helloWorldContract = web3.eth.cointract('<ABI>');

// Deploy the contract
var helloworld = helloworldContract.new({
    from: web3.eth.accounts[0],
    data: '<Bytecode>',
    gas: '4700000'
}, function (e, contract) {
    if (typeof contract.address !== 'undefined') {
        console.log('Address: ' + contract.address);
    }
});

// Deployment with constructor parameters
var helloworld = helloworldContract.new('<paramerer1>', '<parameter2>', {
    from: web3.eth.accounts[0],
    data: '<Bytecode>',
    gas: '4700000'
}, function (e, contract) {
    if (typeof contract.address !== 'undefined') {
        console.log('Address: ' + contract.address);
    }
});

// Create an instance
var greet = web3.eth.cointract('<ABI>').at('<Address>');

// Read data
greet.greeting();

// Write data
greet.changeGreeting("Meow!", {
    from: web3.eth.accounts[0],
});