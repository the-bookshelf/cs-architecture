// require(<external_contract_call>);

pragma solidity ^0.4.24; 

contract BecomeTheKing { 

    address currentKing; 
    uint highestBid; 

    function() payable {
        // Verify the value sent
        require(msg.value > highestBid); 

        // Transfer the previous bid back
        require(currentKing.send(highestBid));

        // Update the king and value
        currentKing = msg.sender; 
        highestBid = msg.value; 
    } 
}

pragma solidity ^0.4.24;

// Attacker contract
contract AlwaysTheKing { 

    // Call the function to become the king 
    function becomeKing(address _address) payable {
        _address.call.value(msg.value); 
    }

    // Always revert when some value is sent
    function() payable { 
        revert(); 
    } 
}

pragma solidity ^0.4.24; 

contract BecomeTheKing { 

    address currentKing; 
    uint highestBid;

    mapping(address => uint) balances;

    // Function to withdraw previous bids
    function withdraw() public {
        uint balance = balances[msg.sender];
        require(balance > 0);
        balances[msg.sender] = 0;
        msg.sender.transfer(balance);
    }

    function() public payable {
        require(msg.value > highestBid); 

        // Save the previous bid for withdrawal
        balances[msg.sender] = highestBid;

        currentKing = msg.sender; 
        highestBid = msg.value; 
    }
}