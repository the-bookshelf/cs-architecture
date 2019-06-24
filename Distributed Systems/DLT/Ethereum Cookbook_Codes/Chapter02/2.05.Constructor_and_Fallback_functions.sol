contract A {
    constructor() {
        // Constructor
    }
}

contract A {
    address owner;

    constructor() {
        owner = msg.sender;
    }
}

contract C {
    uint n;

    constructor(uint _n) internal {
        n = _n;
    }
}

contract X {
    // Default constructor
    constructor() public { }
}

contract A {
    uint someValue;
    constructor(uint _value) public {
        someValue = _value;
    }
}

contract B is A(10) {
     // Base contract argument during inheritance
}

contract C is A {
    constructor(uint _anotherValue) First(_anotherValue) public {
        // Base contract argument in constructor
    }
}

function() {
    // fallback function
}

function() payable {
    // fallback function which can receive Ether
}

function() payable {
    // takes only 2300 gas.
    emit EtherReceived(msg.sender);
}

contract Receiver {
    uint count;
    
    event EtherReceived(address indexed from);

    function() payable {
        count++;
        emit EtherReceived(msg.sender);
    }
}