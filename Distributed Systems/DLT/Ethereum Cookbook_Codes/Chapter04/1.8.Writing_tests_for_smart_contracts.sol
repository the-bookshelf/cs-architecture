// contract TestContract {
//     ...
// }

// contract TestContract {
//     testCase1() { ... }
//     testCase2() { ... }
// }



import "truffle/Assert.sol";
import "truffle/DeployedAddresses.sol";
import "../contracts/TokenContract.sol";

contract TestTokenContract {

    function testInitialBalance() {
        TokenContract token = TokenContract(DeployedAddresses.TokenContract());
        uint expected = 100000;
        Assert.equal(token.getBalance(msg.sender), expected, "Owner should have 100000 TokenContract initially");
    }
}

contract TestHooks {
    uint value;

    function beforeEach() {
        value = 1;
    }

    function beforeEachIncrement() {
        value++;
    }

    function testSomeValue() {
        uint expected = 2;
        Assert.equal(value, expected, "value should be 2");
    }
}