pragma solidity ^0.4.24;

contract VulnerableContract {

    function deposit() public {
        // Code to accept Ether
    }

    function withdraw() public {
        // Code to transfer Ether
    }

}


pragma solidity ^0.4.24;

contract ControlledContract {

    // Simplified withdraw tracker
    // Can include amount for more precise tracking
    mapping(address => uint) lastWithdraw;

    // Modifier to limit the rate of withdraw
    modifier verifyWithdraw() {
        require(lastWithdraw[msg.sender] + 1 days > now);
        _;
    }

    function deposit() public {
        // Code to accept Ether
    }

    function withdraw(uint _value) verifyWithdraw public {
        // Code to transfer Ether
        require(_value < 1 ether);
        lastWithdraw[msg.sender] = now;
    }

}


pragma solidity ^0.4.24;

contract ControlledContract {
    bool pause;
    address owner;

    modifier onlyOwner() {
        require(msg.sender == owner);
        _;
    }

    modifier whenNotPaused { 
        require(!pause);
        _; 
    }

    function pauseContract() onlyOwner public {
        pause = true;
    }

    function unPauseContract() onlyOwner public {
        pause = true;
    }

    function deposit() whenNotPaused public {
        // some code
    }

    function withdraw() whenNotPaused public {
        // some code
    }
}


pragma solidity ^0.4.24;

contract ControlledContract {

    struct WithdrawalReq {
        uint value;
        uint time;
    }

    mapping (address => uint) balances;

    mapping (address => WithdrawalReq) requests;

    uint constant delay = 7 days;

    function requestWithdrawal() public {
        require(balances[msg.sender] > 0);
        uint amountToWithdraw = balances[msg.sender];
        balances[msg.sender] = 0;
        requests[msg.sender] = WithdrawalReq(
            amountToWithdraw,
            now
        );
    }

    function withdraw() public {
        require(now > requests[msg.sender].time + delay);
        uint amountToWithdraw = requests[msg.sender].value;
        requests[msg.sender].value = 0;
        msg.sender.transfer(amountToWithdraw);
    }
}