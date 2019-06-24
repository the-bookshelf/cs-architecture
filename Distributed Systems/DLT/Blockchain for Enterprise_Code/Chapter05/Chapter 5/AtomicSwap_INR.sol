pragma solidity ^0.4.19;

import "./INR.sol";

contract AtomicSwap_INR {
    
    struct AtomicTxn {
        address from;
        address to;
        uint lockPeriod;
        uint amount;
    }
    
    mapping (bytes32 => AtomicTxn) txns;
    INR INRContract;
    
    event inrLocked(address to, bytes32 hash, uint expiryTime, uint amount);
    event inrUnlocked(bytes32 hash);
    event inrClaimed(string secret, address from, bytes32 hash);
    
    function AtomicSwap_INR(address inrContractAddress) {
        INRContract = INR(inrContractAddress); 
    }
    
    function lock(address to, bytes32 hash, uint lockExpiryMinutes, uint amount) {
        INRContract.transferINRFrom(msg.sender, address(this), amount);
        txns[hash] = AtomicTxn(msg.sender, to, block.timestamp + (lockExpiryMinutes * 60), amount);
        inrLocked(to, hash, block.timestamp + (lockExpiryMinutes * 60), amount);
    }
    
    function unlock(bytes32 hash) {
        if(txns[hash].lockPeriod < block.timestamp) {
            INRContract.transferINR(txns[hash].from, txns[hash].amount);
            inrUnlocked(hash);
        }
    }
    
    function claim(string secret) {
        bytes32 hash = sha256(secret);
        INRContract.transferINR(txns[hash].to, txns[hash].amount);
        inrClaimed(secret, txns[hash].from, hash);
    }
    
    function calculateHash(string secret) returns (bytes32 result) {
        return sha256(secret);
    }
}
