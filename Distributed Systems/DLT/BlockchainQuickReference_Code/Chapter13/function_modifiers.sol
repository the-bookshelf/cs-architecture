pragma solidity ^0.4.24;
contract Gotham {
address public weapons;
modifier Bank() { // Modifier
require(
msg.sender == coins,
"Only coins can call this."
);
_;
}
function abort() public coinsbuyer { // Modifier usage
// ...
}