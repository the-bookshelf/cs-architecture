pragma solidity^0.4.24;

contract Sample {
    uint value;
    address sender;
    
    function setValue(uint _value) public {
        value = _value;
        sender = msg.sender;
    }
}