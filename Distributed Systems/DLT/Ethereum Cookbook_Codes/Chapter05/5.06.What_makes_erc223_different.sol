pragma solidity ^0.4.23; 

contract ERC223Interface { 
    uint public totalSupply; 
    function balanceOf(address who) view returns (uint); 
    function transfer(address to, uint value) public; 
    function transfer(address to, uint value, bytes data) public; 
    event Transfer(address indexed from, address indexed to, 
        uint value, bytes data); 
}

pragma solidity ^0.4.23;

contract ERC223Receiver {
    function tokenFallback(address _from, uint _value, bytes _data);
}


uint codeLength;
assembly {
    codeLength := extcodesize(_to)
}

if (codeLength > 0) {
    // Contract
} else {
    // Externally owned account
}

if (codeLength > 0) {
    // Require proper transaction handling.
    ERC223Receiver receiver = ERC223Receiver(_to);
    receiver.tokenFallback(msg.sender, _value, _data);
}

function transfer(address to, uint value, bytes data) { 
    uint codeLength; 
    assembly { 
        codeLength := extcodesize(_to) 
    } 

    balances[msg.sender] = balances[msg.sender].sub(_value); 
    balances[_to] = balances[_to].add(_value); 

    if (codeLength > 0) { 
        ERC223Receiver receiver = ERC223Receiver(_to); 
        receiver.tokenFallback(msg.sender, _value, _data); 
    } 
}

function transfer(address _to, uint256 _value) public 
    returns (bool) {
    balances[msg.sender] = balances[msg.sender].sub(_value); 
    balances[_to] = balances[_to].add(_value); 
    emit Transfer(msg.sender, _to, _value); 
    return true; 
}