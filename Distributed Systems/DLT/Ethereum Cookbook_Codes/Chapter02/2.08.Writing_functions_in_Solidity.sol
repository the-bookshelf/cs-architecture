contract Test {
    function functionName() {
        // Do something
    }
}

contract Test {
    function add(uint _a, uint _b) public {
        // Do something
    }
}

contract Test {
    function add(uint _a, uint _b) public pure
        returns (uint sum)
    {
        sum = _a + _b;
    }   
}

contract Test {
    function add(uint _a, uint _b) public pure
        returns (uint)
    {
        uint sum = _a + _b;
        return sum;
    }   
}

contract Test {
    function arithmetics(uint _a, uint _b) public pure
        returns (uint, uint)
    {
        return (_a + _b, _a - _b);
    }   
}

contract Test {
    function addOne(uint _a) public pure
        returns (uint)
    {
        return _a + 1;
    }

    function addTwo(uint _b) public pure
        returns (uint)
    {
        return addOne(_b) + 1;
    }
}

contract Basic {
    function addOne(uint _a) public pure
        returns (uint)
    {
        return _a + 1;
    }
}

contract Advanced {
    Basic basic;
    function addTwo(uint _b) public view
        returns (uint)
    {
        return basic.addOne(_b) + 1;
    }
}

contract test {
    
    mapping(address => uint) donors;
    
    function donate() public payable {
        donors[msg.sender] = msg.value; 
    }
}

contract Test {
    function add(uint _a, uint _b) public {
        // Do something
    }

    function calc() public {
        add({_b: 5, _a:10});
    }
}

contract test {
    function calc(uint _a, uint) public returns (uint) {
        // Do something
    }
}

contract test {
    
    uint multiplier = 10;
    
    function fun(uint _a, uint _b) public view
        returns (uint) {
        return (_a + _b) * multiplier;
    }
}

contract test {
    function fun(uint _a, uint _b) public pure
        returns (uint) {
        return _a + _b;
    }
}

contract Test {
    function fun(uint _a) public {
        // Do something
    }

    function fun(uint _b, uint _c) public {
        // Do something
    }
}