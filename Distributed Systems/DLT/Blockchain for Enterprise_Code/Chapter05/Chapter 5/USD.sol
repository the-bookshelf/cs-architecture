pragma solidity ^0.4.19;

contract USD {
    
    mapping (address => uint) balances;
    mapping (address => mapping (address => uint)) allowed;
    address owner;
    
    function USD() {
        owner = msg.sender;
    }
    
    function issueUSD(address to, uint amount) {
        if(msg.sender == owner) {
            balances[to] += amount;
        } 
    }
    
    function transferUSD(address to, uint amount) {
        if(balances[msg.sender] >= amount) {
            balances[msg.sender] -= amount;
            balances[to] += amount;
        }
    }
    
    function getUSDBalance(address account) view returns (uint balance) {
        return balances[account];
    }
    
    function approve(address spender, uint amount) {
        allowed[spender][msg.sender] = amount;
    }
    
    function transferUSDFrom(address from, address to, uint amount) {
        if(allowed[msg.sender][from] >= amount && balances[from] >= amount) {
            allowed[msg.sender][from] -= amount;
            balances[from] -= amount;
            balances[to] += amount;
        }
    }
}