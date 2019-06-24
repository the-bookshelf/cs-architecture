pragma solidity^ 0.4.8;

contract Migrations {
    address public owner;
    // A getter function with the signature `last_completed_migration()` is required.
    // It should return an uint
    
    uint public last_completed_migration;
    
    modifier restricted() {
        if (msg.sender == owner)
            _;
    }
    
    function Migrations() {
        owner = msg.sender;
    }
    
    // A function with the signature `setCompleted(uint)` is required.
    function setCompleted(uint completed) restricted {
        last_completed_migration = completed;
    }
    
    function upgrade(address new_address) restricted {
        Migrations upgraded = Migrations(new_address);
        upgraded.setCompleted(last_completed_migration);
    }
}


// ./migrations/1_intial_migration.js //
// var Migrations = artifacts.require("Migrations");
// module.exports = function(deployer) {
//     deployer.deploy(Migrations);
// };

// truffle migrate