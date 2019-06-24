// truffle install <package_name>@<version>
// truffle install

// pragma solidity ^0.4.23; 

// import "owned/ownable.sol"; 

// contract MyContract is ownable { 
//     ...
// }



var MyPackage = artifacts.require("package/contract");
var MyContract = artifacts.require("MyContract");
module.exports = function (deployer) {
    deployer.deploy(MyPackage).then(function () {
        return deployer.deploy(MyContract, MyPackage.address);
    });
};

// npm install <package>

var contract = require("truffle-contract");
var data = require("npm-library/build/contracts/contract.json");

var SimpleNameRegistry = contract(data);


// {
//     "package_name": "test",
//     "version": "0.0.3",
//     "description": "Test contract to check EthPM",
//     "authors": [ "Manoj <email>" ],
//     "keywords": [
//         "ethereum", "test", "ethpm"
//     ],
//     "dependencies": {
//         "ownable": "^0.1.3"
//     },
//     "license": "MIT"
// }

// truffle publish