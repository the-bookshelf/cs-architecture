keccak256(...)
// OR
sha3(...) // alias to keccak256

keccak256(...) returns (bytes32);


keccak256("hello", "world")
keccak256("helloworld")
keccak256(0x68656c6c6f776f726c64)
keccak256(68656c6c6f776f726c64)

sha256(...) returns (bytes32);

ecrecover(bytes32 hash, uint8 v, bytes32 r, bytes32 s) returns (address);

pragma solidity ^0.4.23;

contract VerificationContract {
    function verifyAddress(
        bytes32 h, 
        uint8 v, 
        bytes32 r, 
        bytes32 s) public pure returns (address) {
        return ecrecover(h, v, r, s);
    }
}

var message = "Hello World!";
var prefix = "\x19Ethereum Signed Message:\n";
var hash = web3.utils.sha3(prefix + message.length + message);

var address = "0x...";
var signature = await web3.eth.sign(message, address);

var r = signature.slice(0, 66);
var s = '0x' + signature.slice(66, 130);
var v = '0x' + web3.toDecimal(signature.slice(130, 132));


var result = await VerificationContract.verifyAddress.call(hash, v, r, s);

ripemd160(...) returns (bytes20);

addmod(uint x, uint y, uint k) returns (uint);
mulmod(uint x, uint y, uint k) returns (uint);


// abi.encode(...)
// abi.encodePacked(...)
// abi.encodeWithSelector(bytes4 selector, ...)
// abi.encodeWithSignature(string signature, ...)