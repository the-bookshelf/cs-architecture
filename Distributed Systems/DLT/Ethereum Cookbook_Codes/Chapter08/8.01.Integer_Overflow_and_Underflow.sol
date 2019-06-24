pragma solidity ^0.4.23;

contract TokenContract {
    mapping (address => uint) balances;

    event Transfer(
        address indexed _from, 
        address indexed _to, 
        uint256 _value
    );

    function sendToken(address receiver, uint amount) 
        public returns(bool) {
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

balances[receiver] += amount;

balances[msg.sender] -= amount;

balanceOf[_to] + _value >= balanceOf[_to]

function sendToken(address receiver, uint amount) 
    public returns(bool) {
    require(balances[msg.sender] < amount);
    require(balanceOf[_to] + _value >= balanceOf[_to]);

    balances[msg.sender] -= amount;
    balances[receiver] += amount;

    emit Transfer(msg.sender, receiver, amount);
    return true;
}

pragma solidity ^0.4.24;

library SafeMath {
    /**
     * @dev Function to add two numbers
     */
    function add(uint256 a, uint256 b) 
        internal pure returns (uint256 c) {
        c = a + b;
        assert(c >= a);
        return c;
    }

    function sub(...) { ... }
    function mul(...) { ... }
    function div(...) { ... }
}


import "./contracts/math/SafeMath.sol";

using SafeMath for uint256;

function sendToken(address receiver, uint amount) 
    public returns(bool) {
    require(balances[msg.sender] < amount);
    balances[msg.sender] = balances[msg.sender].sub(amount);
    balances[receiver] = balances[receiver].add(amount);
    emit Transfer(msg.sender, receiver, amount);
    return true;
}
