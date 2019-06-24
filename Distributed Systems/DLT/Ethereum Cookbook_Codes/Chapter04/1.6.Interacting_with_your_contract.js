TokenContract.deployed().then(function(instance) { 
    console.log(instance); 
});


var TokenContract = artifacts.require("TokenContract.sol");

var from_address = "0xa...";
var to_address = "0xb...";

var tokenContract;

TokenContract.deployed().then(function (instance) {
    tokenContract = instance;
    return tokenContract.sendToken(to_address, 500, {
        from: from_address,
        value: 10000 // in wei
    });
}).then(function (result) {
    console.log(result.tx); // Transaction hash
    console.log(result.logs); // Event logs
    console.log(result.receipt); // Transaction receipt

    for (var i = 0; i < result.logs.length; i++) {
        if (result.logs[i].event == "Transfer") {
            console.log("Event raised!");
        }
    }
}).catch(function (e) {
    // Transaction failed
    console.log(e);
})

var account = "0xf17f52151EbEF6C7334FAD080c5704D77216b732";

TokenContract.deployed().then(function (instance) {
    return instance.getBalance.call(account);
}).then(function (result) {
    // Returns result
    console.log(result.toNumber())
}).catch(function (e) {
    // Exception
    console.log(e);
});


// Deploy the contract using new()
TokenContract.new().then(function (instance) {
    // New contract instance
    console.log(instance.address);
}).catch(function (err) {
    // Exception
});

// Connect to existing instance using at keyword
var instance = TokenContract.at("0x...");