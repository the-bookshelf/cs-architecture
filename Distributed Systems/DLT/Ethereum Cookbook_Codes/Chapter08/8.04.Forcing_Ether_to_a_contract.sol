require(this.balance == 1 ether);

contract SelfDestructable {

    function kill(address _target) public {
        selfdestrcut(_target);
    }

}

// rightmost 160 bits of
sha3(rlp.encode([sender_address, nonce]))

sha3(rlp.encode([0x1.., 10]))

pragma solidity^0.4.24;

/**
 * @dev Retrieve money stuck in the contract address
 **/
contract GetEtherBack {

    // Destroy the contract and send Ether to the creator
    constructor() public {
        selfdestruct(msg.sender);
    }
}