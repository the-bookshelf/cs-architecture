pragma solidity^0.4.24;

contract Payout {
    // Arbitrary length array to store addresses
    address[] private addresses;
    
    // Mapping to store payout value
    mapping (address => uint) public payouts;
    
    // Function to transfer payouts
    function payoutAll() public {
        for(uint8 i = 0; i < addresses.length; i++) {
            require(addresses[i].send(payouts[addresses[i]]));
        }
    }
}



function payoutAll() public {
    for(uint256 i = 0; i < addresses.length; i++) {
        require(addresses[i].send(payouts[addresses[i]]));
    }
}


pragma solidity^0.4.24;

contract Payout {

    // Using struct over mapping
    struct Recipient {
        address addr;
        uint256 value;
    }

    Recipient[] recipients;

    // State variable for batch operation
    uint256 nextIndex;

    // Function to transfer payout
    function payoutAll() public {
        uint256 i = nextIndex;

        while (i < recipients.length && gasleft() > 200000) {
          recipients[i].addr.send(recipients[i].value);
          i++;
        }

        nextIndex = i;
    }
}