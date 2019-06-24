pragma solidity ^0.4.21;

/**
 * @title SchedulerInterface
 * @dev The base contract that the higher contracts: 
 * BaseScheduler, BlockScheduler and TimestampScheduler 
 * all inherit from.
 */
contract SchedulerInterface {
    function schedule(
        address _toAddress, 
        bytes _callData, 
        uint[8] _uintArgs
    ) public payable returns (address);

    function computeEndowment(
        uint _bounty, 
        uint _fee, 
        uint _callGas, 
        uint _callValue, 
        uint _gasPrice
    ) public view returns (uint);
}

pragma solidity ^0.4.24;

import "./SchedulerInterface.sol";

contract DelayedPayment {
    SchedulerInterface public scheduler;
}

constructor(address _scheduler) public payable {
    scheduler = SchedulerInterface(_scheduler);
}

_toAddress = address(this); // address of scheduling contract

_callData = ""; // or specify any function signature


contract ScheduledPayment {

    SchedulerInterface public scheduler;

    address public scheduledTransaction;

    constructor(address _scheduler) public payable {
        scheduler = SchedulerInterface(_scheduler);
    }

    function scheduleTransaction(uint _numBlocks) public {
        uint lockedUntil = block.number + _numBlocks;

        scheduledTransaction = scheduler.schedule.value(0.1 ether) (
            address(this),
            "",
            [
                200000,
                0,
                255,
                lockedUntil,
                20000000000 wei,
                20000000000 wei,
                20000000000 wei,
                30000000000 wei
            ]
        );
    }

    function () public payable {
        // ...
    }
}