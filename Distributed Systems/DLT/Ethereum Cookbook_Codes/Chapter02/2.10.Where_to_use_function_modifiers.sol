modifier modifierName {
    // modifier definition
}

modifier onlyOwner {
    require(msg.sender == owner);
    _;
}

contract Test {
    
    address owner;
    
    constructor() public {
        owner = msg.sender;
    }
    
    modifier onlyBy(address user) {
        require(msg.sender == user);
        _;
    }
    
    function donate() onlyBy(owner) public {
        // do something
    }
}

contract modifierContract {
    address owner;
    constructor() {
        owner == msg.sender;
    }

    modifier onlyOwner {
        require(msg.sender == owner);
        _;
    }

    modifier valueGreaterThan(uint value) {
        require(msg.value > value);
        _;
    }

    function sendEther() onlyOwner valueGreaterThan(1 ether) public {
        // Function body
    }
}

contract Ownership {

    address owner;
    
    function Ownership() public {
        owner = msg.sender;
    }

    modifier onlyOwner {
        require(msg.sender == owner);
        _;
    }
}

contract Donate is Ownership {

    bool locked;
    modifier noReentrancy() {
        require(!locked);
        locked = true;
        _;
        locked = false;
    }

    function claim() onlyOwner public {
        require(msg.sender.call());
    }

    function donate(address _user) noReentrancy public {
        require(_user.call());
    }

}