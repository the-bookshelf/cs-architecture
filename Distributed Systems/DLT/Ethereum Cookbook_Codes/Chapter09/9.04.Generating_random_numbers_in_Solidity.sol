pragma solidity^0.4.24;

// Contract to generate random number
// Uses timestamp and difficulty
// Not recommended
contract RNG {
    function generateRandom() view returns (uint) {
        return uint256(keccak256(
            abi.encodePacked(block.timestamp, block.difficulty)
        ));
    }
}

pragma solidity^0.4.24;

contract Lottery {

    // Structure of a bid
    struct Bid {
        uint num;
        uint blockNum;
    }

    // Mapping to store bods
    mapping(address => Bid) bids;

    // Function to select the winner based on future block hash
    function isWon() public view returns (bool) {
        uint bid = bids[msg.sender].num;
        uint blockNum = bids[msg.sender].blockNum;

        require(blockNum >= block.number);

        uint winner = uint(keccak256(
            abi.encodePacked(blockhash(blockNum + 3))
        ));

        return (bid == winner);
    }
}