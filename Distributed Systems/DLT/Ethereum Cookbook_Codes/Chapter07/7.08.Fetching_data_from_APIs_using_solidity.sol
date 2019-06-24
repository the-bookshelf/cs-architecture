pragma solidity ^0.4.24;

contract OracleRng {
    uint public random;
    ...
}

event LogOracle();
event LogNewRandom(uint random);

function setRandom() public {
    emit LogOracle();
}

function _callback(uint _random) public {
    random = _random;
    emit LogNewRandom(_random);
}


pragma solidity ^0.4.24;

/**
 * Random number generator contract
 * using oracles
 */
contract OracleRng {

    uint public random;

    event LogOracle();
    event LogNewRandom(uint random);

    /**
     * Function to emit the oracle event
     */
    function setRandom() public {
        emit LogOracle();
    }

    /**
     * Function called by the oracle service
     * @param _random Random value returned by the oracle
     */
    function _callback(uint _random) public {
        random = _random;
        emit LogNewRandom(_random);
    }
}

var oracle = oracleRng.setRandom();

// watch for logs
oracle.watch(function(error, result){
    ...
});

// Use some logic or external API call to generate the number.
var random = 1232;

oracleRng._callback(random, {
    from: "0x.."
});


var oracle = oracleRng.setRandom();

// watch for logs
oracle.watch(function(error, result) {
    // Make API calls to generate random number
    var random = 1232;
    // Callback into source contract
    oracleRng._callback(random, {
        from: "0x.."
    });
});


pragma solidity ^0.4.24;

contract OracleRng {

    // Address of trusted oracle
    address trustedOracle;

    // Modifier to restrict oracle calls
    modifier onlyTrustedOracle() {
        require(msg.sender == trustedOracle);
        _;
    }

    // Function to change trusted oracle
    function setTrustedOracle(address _oracle) {
        trustedOracle = _oracle;
    }

    function _callback(uint _random) public 
        onlyTrustedOracle {
        //...
    }
}
