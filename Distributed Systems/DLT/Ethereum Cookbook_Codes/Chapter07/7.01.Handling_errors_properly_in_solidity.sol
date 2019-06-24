// require()
// Older and deprecated method
if (msg.sender != owner) {
    throw;
}

require(toAddress != address(0));
require(targetContract.send(amountInWei));
require(balances[msg.sender] >= amount);
require(condition, "Error Message");


// revert()
if (msg.sender != owner) {
    revert();
}

if (!condition) {
    revert("Error Message");
}

// assert()
// syntax
assert(condition);

function add(uint _a, uint _b) returns (uint) {
    uint x = _a + _b;
    assert(x > _b);
    returns x;
}

assert(address(this).balance >= totalSupply);