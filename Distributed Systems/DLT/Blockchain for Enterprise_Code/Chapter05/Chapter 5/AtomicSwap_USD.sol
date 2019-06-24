pragma solidity ^0.4.19;

import "./USD.sol";

contract AtomicSwap_USD {
    
    struct AtomicTxn {
        address from;
        address to;
        uint lockPeriod;
        uint amount;
    }
    
    mapping (bytes32 => AtomicTxn) txns;
    USD USDContract;
    
    event usdLocked(address to, bytes32 hash, uint expiryTime, uint amount);
    event usdUnlocked(bytes32 hash);
    event usdClaimed(string secret, address from, bytes32 hash);
    
    function AtomicSwap_USD(address usdContractAddress) {
        USDContract = USD(usdContractAddress); 
    }
    
    function lock(address to, bytes32 hash, uint lockExpiryMinutes, uint amount) {
        USDContract.transferUSDFrom(msg.sender, address(this), amount);
        txns[hash] = AtomicTxn(msg.sender, to, block.timestamp + (lockExpiryMinutes * 60), amount);
        usdLocked(to, hash, block.timestamp + (lockExpiryMinutes * 60), amount);
    }
    
    function unlock(bytes32 hash) {
        if(txns[hash].lockPeriod < block.timestamp) {
            USDContract.transferUSD(txns[hash].from, txns[hash].amount);
            usdUnlocked(hash);
        }
    }
    
    function claim(string secret) {
        bytes32 hash = sha256(secret);
        USDContract.transferUSD(txns[hash].to, txns[hash].amount);
        usdClaimed(secret, txns[hash].from, hash);
    }
    
    function calculateHash(string secret) returns (bytes32 result) {
        return sha256(secret);
    }
}