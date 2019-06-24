contract('TokenContract', function() {
    // Write tests
});

var TokenContract = artifacts.require("TokenContract.sol");

var TokenContract = artifacts.require("TokenContract.sol"); 

contract('TokenContract', function(accounts) { 
    it("Contract test template", function() { 
        return TokenContract.deployed().then(function(instance) { 
            console.log(instance);
        });
    });
});

var TokenContract = artifacts.require("TokenContract.sol");

contract('TokenContract', function(accounts) {

    it("should allocate 10000 Token to the owner account", function() {
        return TokenContract.deployed().then(function(instance) {
            return instance.getBalance.call(accounts[0]);
        }).then(function(balance) {
            assert.equal(balance.valueOf(), 100000, 
                "100000 wasn't in the first account");
        });
    });

})



var TokenContract = artifacts.require("TokenContract.sol");

contract('TokenContract', function (accounts) {

    it("should allocate 10000 Token to the owner account", function () {
        return TokenContract.deployed().then(function (instance) {
            return instance.getBalance.call(accounts[0]);
        }).then(function (balance) {
            assert.equal(balance.valueOf(), 100000,
                "100000 wasn't in the first account");
        });
    });

    it("should transfer tokens correctly", function () {
        var token;
        var account_one_starting_balance;
        var account_two_starting_balance;
        var account_one_ending_balance;
        var account_two_ending_balance;
        var amount = 10;
        return TokenContract.deployed().then(function (instance) {
            token = instance;
            return token.getBalance.call(accounts[0]);
        }).then(function (balance) {
            account_one_starting_balance = balance.toNumber();
            return token.getBalance.call(accounts[1]);
        }).then(function (balance) {
            account_two_starting_balance = balance.toNumber();
            return token.sendToken(accounts[1], amount, {
                from: accounts[0]
            });
        }).then(function (tx) {
            return token.getBalance.call(accounts[0]);
        }).then(function (balance) {
            account_one_ending_balance = balance.toNumber();
            return token.getBalance.call(accounts[1]);
        }).then(function (balance) {
            account_two_ending_balance = balance.toNumber();
            assert.equal(account_one_ending_balance,
                account_one_starting_balance - amount,
                "Amount wasn't correctly taken from the sender");
            assert.equal(account_two_ending_balance,
                account_two_starting_balance + amount,
                "Amount wasn't correctly sent to the receiver");
        });
    });

    it("should transfer tokens correctly with async", async () => {
        let amount = 10;
        let token = await TokenContract.deployed();
        let balance = await token.getBalance.call(accounts[0]);
        let account_one_starting_balance = balance.toNumber();
        balance = await token.getBalance.call(accounts[1]);
        let account_two_starting_balance = balance.toNumber();
        await token.sendToken(accounts[1], amount, {
            from: accounts[0]
        });
        balance = await token.getBalance.call(accounts[0]);
        let account_one_ending_balance = balance.toNumber();
        balance = await token.getBalance.call(accounts[1]);
        let account_two_ending_balance = balance.toNumber();
        assert.equal(account_one_ending_balance,
            account_one_starting_balance - amount,
            "Amount wasn't correctly taken from the sender");
        assert.equal(account_two_ending_balance,
            account_two_starting_balance + amount,
            "Amount wasn't correctly sent to the receiver");
    });

});

// truffle test ./test/TokenContract.js