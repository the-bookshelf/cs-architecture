pragma solidity^0.4.23;

contract Storage {
    // ...
}
contract Parent {
    // Creation of new contract
    Address storeAddress = new Storage();
    // ...
}

// Storage store = new Storage();

pragma solidity^0.4.23;

contract Storage {
    uint public num;
    function changeNum(uint _num) public {
        num = _num;
    }
}

contract Parent {
    Storage store = new Storage();
    function interact() public {
        store.changeNum(10);
    }
}

pragma solidity^0.4.23;

contract Storage {
    uint public num;
    constructor(uint _num) public {
        num = _num;
    }
}

contract Parent {
    Storage store = new Storage(10);
}

pragma solidity^0.4.23;

contract Storage {
    uint public num;
    constructor(uint _num) payable public {
        num = _num;
    }
}

contract Parent {
    function createStore() public payable {
        Storage store = (new Storage).value(msg.value)(10);
    }
}

pragma solidity^0.4.23;

contract Storage {
    uint public num;
    constructor(uint _num) payable public {
        num = _num;
    }
}

contract Parent {
    Storage store;
    function changeStore(address _storeAddress) public payable {
        store = Storage(_storeAddress);
    }
}

pragma solidity^0.4.23;

contract Storage {
    uint public num;
    constructor(uint _num) payable public {
        num = _num;
    }
}

contract Parent {
    Storage store;
    function changeStore(Storage _store) public payable {
        store = _store;
    }
}

// <address>.call(<function_signature>, parameters...);

pragma solidity^0.4.23;

contract Storage {
    uint public num;
    function changeValue(uint _num) payable public {
        num = _num;
    }
}

contract Parent {
    function changeStoreValue(address _store, uint _value) public payable {
        _store.call.gas(100000).value(1 ether)(bytes4(keccak256("changeValue(uint256)")), _value);
    }
}

pragma solidity^0.4.23;

contract Storage {
    uint public num;
    function changeValue(uint _num) payable public {
        num = _num;
    }
}

contract Parent {
    uint public num;

    function changeStoreValue(address _store, uint _value) public payable {
        _store.delegatecall.gas(100000)(bytes4(keccak256("changeValue(uint256)")), _value);
    }
}