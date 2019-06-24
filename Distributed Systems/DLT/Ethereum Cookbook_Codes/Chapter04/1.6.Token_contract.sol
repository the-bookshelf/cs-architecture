pragma solidity ^0.4.23;

contract TokenContract {
    mapping (address => uint) balances;

    event Transfer(address indexed _from, address indexed _to, uint256 _value);
    
    constructor() public {
        balances[msg.sender] = 100000;
    }
    
    function sendToken(address receiver, uint amount) public returns(bool) {
        require(balances[msg.sender] < amount);
        balances[msg.sender] -= amount;
        balances[receiver] += amount;
        emit Transfer(msg.sender, receiver, amount);
        return true;
    }
    
    function getBalance(address addr) public view returns(uint) { 
        return balances[addr];
    }
}