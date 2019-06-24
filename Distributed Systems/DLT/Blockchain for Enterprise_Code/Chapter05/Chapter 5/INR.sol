pragma solidity ^0.4.19;

contract INR {
    
    mapping (address => uint) balances;
    mapping (address => mapping (address => uint)) allowed;
    address owner;
    
    function INR() {
        owner = msg.sender;
    }
    
    function issueINR(address to, uint amount) {
        if(msg.sender == owner) {
            balances[to] += amount;
        } 
    }
    
    function transferINR(address to, uint amount) {
        if(balances[msg.sender] >= amount) {
            balances[msg.sender] -= amount;
            balances[to] += amount;
        }
    }
    
    function getINRBalance(address account) view returns (uint balance) {
        return balances[account];
    }
    
    function approve(address spender, uint amount) {
        allowed[spender][msg.sender] = amount;
    }
    
    function transferINRFrom(address from, address to, uint amount) {
        if(allowed[msg.sender][from] >= amount && balances[from] >= amount) {
            allowed[msg.sender][from] -= amount;
            balances[from] -= amount;
            balances[to] += amount;
        }
    }
}