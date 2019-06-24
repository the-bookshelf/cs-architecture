pragma solidity ^0.4.24;

contract AbstractContrct {
    // Function without declaration
    function f() public returns (uint);

    // Function with declaration
    function c() public returns (uint) {
        return 0;
    }
}


pragma solidity ^0.4.24;

// Abstract contract
contract AbstractContrct {
    function f() public returns (uint);
}

// Contract with functions implemented
contract InheritedContract is AbstractContrct {
    function f() public returns (uint) {
        return 11;
    }
}


interface InterfaceContract {
    // ...
}

interface ERC20 {
    function transfer(address target, uint amount) public;
    function transferFrom(address source, address target, uint amount) public;
    // ...
}

interface ERC20 {
    function transfer(address target, uint amount) public;
}

contract Token is ERC20 {
    function transfer(address target, uint amount) public {
    //...
    }
}

interface ERC20 {
    function transfer(address target, uint amount) public;
}

contract Token {
    constructor(address _token) public {
        ERC20 token = ERC20(_token);
    }
}