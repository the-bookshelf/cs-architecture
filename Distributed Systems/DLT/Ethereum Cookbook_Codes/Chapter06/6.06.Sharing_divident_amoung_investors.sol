pragma solidity^0.4.23;

import "./WinnableDAL.sol";
import "./StandardToken.sol";

contract InvestableDAL is WinnableDAL, StandardToken {
    //...
}


function invest() public {
    //...
}


address investor = msg.sender;
uint tokens = msg.value;

mint(investor, tokens);


uint dividendAmount = 0;


function play(uint _hash) payable public {
    uint dividend = msg.value * (5/100);
    dividendAmount+= dividend;
    uint ticketValue = msg.value - dividend;

    //...
}


mapping(address => bool) payoutStatus;


function getPayout() {
    require(!payoutStatus[msg.sender]);

    uint percentage = balance[msg.sender]/totalSupply;
    uint dividend = dividendAmount * (percentage/100);

    msg.sender.transfer(dividend);

    payoutStatus[msg.sender] = true;
}



pragma solidity^0.4.23;

import "./WinnableDAL.sol";
import "./StandardToken.sol";

contract InvestableDAL is WinnableDAL, StandardToken {

    uint dividendAmount = 0;

    mapping(address => bool) payoutStatus;

    /**
     * @dev Function to invest
     */
    function invest() public {
        address investor = msg.sender;
        uint tokens = msg.value;

        mint(investor, tokens);
    }

    /**
     * @dev Modified play function with dividend
     * @param _hash Guessed number
     */
    function play(uint _hash) payable public {
        uint dividend = msg.value * (5/100);
        dividendAmount+= dividend;
        uint ticketValue = msg.value - dividend;

        //...
    }

    /**
     * @dev Function to get payout
     */
    function getPayout() {
        require(!payoutStatus[msg.sender]);

        uint percentage = balances[msg.sender] / totalSupply;
        uint dividend = dividendAmount * (percentage / 100);

        msg.sender.transfer(dividend);

        payoutStatus[msg.sender] = true;
    }
}
