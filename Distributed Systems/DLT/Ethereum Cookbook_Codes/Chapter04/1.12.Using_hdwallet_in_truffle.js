// npm install truffle-hdwallet-provider
var HDWalletProvider = require("truffle-hdwallet-provider");

// 12 word mneminic to create randomness
var mnemonic = "cat oreo water ...";

var provider = new HDWalletProvider(mnemonic, "http://localhost:8545");

var addressIndex = 7;
var provider = new HDWalletProvider(mnemonic, "http://localhost:8545", addressIndex);

var HDWalletProvider = require("truffle-hdwallet-provider");

var mnemonic = "cat oreo water ...";

module.exports = {
    networks: {
        development: {
            host: "localhost",
            port: 8545,
            network_id: "*"
         },
         ropsten: {
             provider: new HDWalletProvider(mnemonic, "https://ropsten.infura.io/"),
             network_id: 3
         }
    }
};