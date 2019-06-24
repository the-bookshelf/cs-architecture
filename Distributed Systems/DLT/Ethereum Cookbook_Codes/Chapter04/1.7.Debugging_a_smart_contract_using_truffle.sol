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


// Debug using truffle debug command
// truffle debug 0x4e3bbf1f5097357f8a4e3d42a3377520c409a2804236eeda89173739a46c7a55