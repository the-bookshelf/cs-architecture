function fun(uint _a) {
    uint[] x;
}


pragma solidity ^0.4.24;

// Contract to demonstrate uninitialized storage pointer bug
contract StorageContract { 
    // Storage variable at location 0
    uint stateVaribale;

    // Storage variable at location 1
    uint[] arrayData;

    // Function which has an uninitialized storage variable
    function fun() public {
        // Storage variable which points to location 0
        uint[] x;
        // Modifies value at location 0
        x.push(0);
        // Modifies value at location 1
        arrayData = x;
    }
}
