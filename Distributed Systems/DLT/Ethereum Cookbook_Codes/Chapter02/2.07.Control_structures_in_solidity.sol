pragma solidity ^0.4.23;

contract test {
    function isValid(uint input) public pure returns (bool) {
        if (input > 10) {
            return true;
        } else {
            return false;
        }
    } 
}

contract test {
    function refund(address[] users) public {
        uint i = 0;
        while (i < users.length) {
            users[i].transfer(1 ether);
            i++; 
        }
    }
}

contract test {
    function refund(address[] users) public {
        for(uint i = 0; i < users.length; i++) {
            users[i].transfer(1 ether);
        } 
    }
}

contract test {
    function refund(address[] users) public {
        uint input = 0;
        do {
            users[input].transfer(1 ether);
            input++;
        } while (input < 10);
    }
}

contract test{
    function refund(address[] users) public {
        for (uint i = 0; i < 20; i++) {
            if (i % 2 == 0)
                continue;
            users[i].transfer(1 ether);
        }
    }
}

contract test
    function isValid(uint value) public pure returns (bool) {
        bool result = (value > 10) ? true : false;
        return result;
    } 
}
