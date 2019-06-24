pragma solidity^0.4.23;

import "./InvestableDAL.sol";

contract AffiliateDAL is InvestableDAL {
    //...
}


mapping(address => uint) affiliate;


function getAffiliatePay() public {
    //...
}


uint amount = affiliate[msg.sender];
require(amount > 0);

msg.senser.transfer(amount);
affiliate[msg.sender] = 0;


function play(uint _hash, address _affiliate) 
    payable public {
    //...
}


uint ticketValue = msg.value;

if(_affiliate != address(0)) {
    uint affiliateAmount = msg.value * (1/100);
    affiliate[msg.sender] += affiliateAmount;
    ticketValue = msg.value - affiliateAmount;
}

//...



pragma solidity^0.4.23;

import "./InvestableDAL.sol";

contract AffiliateDAL is InvestableDAL {

    mapping(address => uint) affiliate;

    /**
     * @dev Function to get affiliate payout
     */
    function getAffiliatePay() public {
        uint amount = affiliate[msg.sender];
        require(amount > 0);

        msg.senser.transfer(amount);
        affiliate[msg.sender] = 0;
    }

    /**
     * @dev Modified play function with affiliate
     * @param _hash Guessed number
     * @param _affiliate Address of the affiliate
     */
    function play(uint _hash, address _affiliate) payable public {
        uint ticketValue = msg.value;

        if(_affiliate != address(0)) {
            uint affiliateAmount = msg.value * (1/100);
            affiliate[msg.sender] += affiliateAmount;
            ticketValue = msg.value - affiliateAmount;
        }

        //...
    }
}
