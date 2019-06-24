// npm install truffle-contract
// <script type="text/javascript" src="web3.js"></script> 
// <script type="text/javascript" src="./dist/truffle-contract.min.js"></script>

var provider = new Web3.providers.HttpProvider("http://localhost:8545"); 
var contract = require("truffle-contract");

var MyContract = contract({
    // Optional. Defaults to "Contract"
    contract_name: "MyContract",
    // Required. Application binary interface.
    abi: [], 
    // Optional. Binary without resolve library links. 
    unlinked_binary: "...", 
    // Optional. Deployed address of contract.
    address: "...", 
    // Optional. ID of network being saved within abstraction. 
    network_id: "...", 
    // Optional. ID of default network this
    default_network: "..."
});

var MyContract = TruffleContract({
    //...
});

var contractInstance; 

MyContract.deployed().then(function(instance) { 
    var contractInstance = instance; 
    return contractInstance.contractFunction(); 
}).then(function(result) { 
    // Do something
});



MyContract.setProvider(provider);

var provider = new Web3.providers.HttpProvider("http://localhost:8545");
var contract = require("truffle-contract");
var TokenContract = contract({
    contract_name: "TokenContract",
    abi: [],
    unlinked_binary: "...",
    address: "..."
});

TokenContract.setProvider(provider);
var account_one = "0x1...";
var account_two = "0x2...";
var contract_address = "0xC...";
var token;

TokenContract.at(contract_address).then(function (instance) {
    token = instance;
    return token.sendTokens(account_two, 100, {
        from: account_one
    });
}).then(function (result) {
    return token.getBalance.call(account_two);
}).then(function (balance_of_account_two) {
    console.log("Account two:" + balance_of_account_two);
    return token.sendTokens(account_one, 50, {
        from: account_two
    });
}).then(function (result) {
    return token.getBalance.call(account_two)
}).then(function (balance_of_account_two) {
    console.log("Account two: " + balance_of_account_two);
}).catch(function (err) {
    console.log(err);
});

// npm install truffle-artifactor

var artifactor = require("truffle-artifactor");

var contract_data = { 
    contract_name: "TokenContract", 
    abi: [], 
    unlinked_binary: "...", 
    address: "...", 
    network_id: "...", 
    default_network: "..."
};

artifactor.save(contract_data, "./TokenContract.sol.js") 
    .then(function () { 
        // Success 
    });

var TokenContract = require("./TokenContract.sol.js");

TokenContract.setProvider(provider);