contract A {
...
}
contract B is A {
...
}

pragma solidity ^0.4.23;

contract A {

    uint public value;

    function changeValue() public { 
        value = 1; 
    }
}

contract B is A {

    function changeValue() public { 
        value = 2; 
    }
}


pragma solidity ^0.4.23;

/**
 * First parent contract
 */ 
contract A {

    uint public value;

    function changeValue() public { 
        value = 1; 
    }
}

/**
 * Second parent contract
 */ 
contract B {

    uint public value;

    function changeValue() public { 
        value = 2; 
    }
}

/**
 * Contract which inherits 2 base contracts
 */ 
contract C is A, B {

    function changeValue() public { 
        value = 3; 
        super.changeValue();
    }
}


pragma solidity ^0.4.23;

/**
 * Parent contract
 */ 
contract A {
    
    uint public value;
    
    constructor(uint _value) public { 
        value = _value; 
    }
}

/**
 * Constructor parameter is specified during inheritance
 */ 
contract B is A(1) {
    
    constructor() public { 
        // ...
    }
}

/**
 * Constructor parameter is specified in the constructor
 */ 
contract C is A {
    
    constructor() A(10) public { 
        // ...
    }
}