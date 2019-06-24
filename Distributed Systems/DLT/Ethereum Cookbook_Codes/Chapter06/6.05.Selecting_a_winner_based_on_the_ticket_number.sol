pragma solidity^0.4.23;

import "./DAL.sol";

contract WinnableDAL is DAL {
    //...
}

function verifyBet(Bet _bet, uint24 _result) pure
    internal returns(uint)  {
    //..
}

uint24 userBet = uint24(_bet.betHash);
uint24 actualValue = userBet ^ _result;

uint24 predictions = 
    ((actualValue & 0xF)     == 0 ? 1 : 0) +
    ((actualValue & 0xF0)    == 0 ? 1 : 0) +
    ((actualValue & 0xF00)   == 0 ? 1 : 0) +
    ((actualValue & 0xF000)  == 0 ? 1 : 0) +
    ((actualValue & 0xF0000) == 0 ? 1 : 0);
    

if (predictions == 5) 
    return _bet.value * 10000;
if (predictions == 4)
    return _bet.value * 1000;
if (predictions == 3)
    return _bet.value * 100;
if (predictions == 2)
    return _bet.value * 10;
return 0;

function pay(address _winner, uint _amount) internal {
    // Check if the contract has enough balance
    require(address(this).balance > _amount);

    // Transfer the amount to the user
    _winner.transfer(_amount);
}

function isWon(address _user) public {
    //...
}

Bet storage userBet = bets[_user];
require(userBet.validateBlock >= block.number);

uint24 futureHash = 
    uint24(blockhash(userBet.validateBlock));

uint prize = verifyBet(userBet, futureHash);

if(prize > 0) {
    pay(_user, prize);
}

delete bets[_user];




contract WinnableDAL is DAL {

    /**
     * @dev Function to verify the bet
     * @param _bet bet object
     * @param _result future block hash
     * @returns uint indicating the winning amount
     */
    function verifyBet(Bet _bet, uint24 _result) pure internal 
        returns(uint) {
        uint24 userBet = uint24(_bet.betHash);
        uint24 actualValue = userBet ^ _result;

        uint24 predictions = 
            ((actualValue & 0xF) == 0 ? 1 : 0) +
            ((actualValue & 0xF0) == 0 ? 1 : 0) +
            ((actualValue & 0xF00) == 0 ? 1 : 0) +
            ((actualValue & 0xF000) == 0 ? 1 : 0) +
            ((actualValue & 0xF0000) == 0 ? 1 : 0);

        if (predictions == 5) 
            return _bet.value * 10000;
        if (predictions == 4)
            return _bet.value * 1000;
        if (predictions == 3)
            return _bet.value * 100;
        if (predictions == 2)
            return _bet.value * 10;
        return 0;
    }

    /**
     * @dev Function to pay an user
     * @param _wiinner Address of the receiver
     * @param _amount Amount to transfer
     */
    function pay(address _winner, uint _amount) internal {
        require(address(this).balance > _amount);
        _winner.transfer(_amount);
    }

    /**
     * @dev Function to check the winning status
     * @param _user Address of the user
     */
    function isWon(address _user) public {
        Bet storage userBet = bets[_user];
        require(userBet.validateBlock >= block.number);

        uint24 futureHash = 
            uint24(blockhash(userBet.validateBlock));
        uint prize = verifyBet(userBet, futureHash);

        if(prize > 0) {
            pay(_user, prize);
        }
        delete bets[_user];
    }
}
