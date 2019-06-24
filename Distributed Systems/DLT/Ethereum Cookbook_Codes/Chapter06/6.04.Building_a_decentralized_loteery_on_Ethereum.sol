pragma solidity ^0.4.17;

// Decentralized autonomous lottery
contract DAL {
    // ...
}

struct Bet {
    // Bet amount
    uint256 value;
    // Guessed numbers
    uint32 betHash;
    // Future block to validate the bet
    uint32 validateBlock;
}

// Mapping to store bet against an user.
mapping(address => Bet) bets;

function play(uint _hash) payable public {
    //...
}

require(msg.value <= 1 ether && msg.value >= 0.1 ether);


uint24 bethash = uint24(_hash);

uint256 blockNumber = block.number + 3;

bets[msg.sender] = Bet({ 
    value: msg.value, 
    betHash: uint32(bethash), 
    validateBlock: uint32(blockNumber) 
});

// Event declaration
event LogBet(
    address indexed player, 
    uint256 bethash, 
    uint256 blocknumber, 
    uint256 betsize
);

// Event log
emit LogBet(
    msg.sender, 
    uint(bethash), 
    blockNumber, 
    msg.value
);

function () payable external {
    play(uint(keccak256(msg.sender, block.number)));
}



pragma solidity^0.4.23;

contract DAL {

    // Data structure to store the bet details
    struct Bet {
        uint256 value;
        uint32 betHash;
        uint32 validateBlock;
    }

    // Mapping of user and their bet
    mapping(address => Bet) bets;

    // Event to track each bet
    event LogBet(
        address indexed player, 
        uint256 bethash, 
        uint256 blocknumber, 
        uint256 betsize
    );

    /**
     * @dev Function to place a bet
     * @param _hash Number guessed
     */
    function play(uint _hash) payable public {
        require(msg.value <= 1 ether && 
                msg.value >= 0.1 ether);

        uint24 bethash = uint24(_hash);
        uint256 blockNumber = block.number + 3;

        bets[msg.sender] = Bet({ 
            value: msg.value, 
            betHash: uint32(bethash), 
            validateBlock: uint32(blockNumber) 
        });

        emit LogBet(
            msg.sender, 
            uint(bethash), 
            blockNumber, 
            msg.value
        );
    }

    /**
     * @dev Fallback function which acts as a shortcut
     * to the token buying process
     */
    function () payable external {
        play(uint(keccak256(msg.sender, block.number)));
    }

}
