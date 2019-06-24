pragma solidity^0.4.24;

/**
 * Basic storage contract
 */
contract State {
    uint public result;
}

pragma solidity^0.4.24;

/**
 * Basic logic contract
 * Increments the state variable value by 1
 */
contract AddOne {

    uint result;

    function increment() public {
        result = result + 1;
    }
}

pragma solidity^0.4.24;

contract Proxy is State {
    //...
}

address logicContract;

function upgrade(address _newLogicContract) public {
    logicContract = _newLogicContract;
}


pragma solidity^0.4.24;

/**
 * Proxy contract which handles state and logic
 * Includes the ability to update the logic
 */
contract Proxy is State {
    address logicContract;

    /**
    * @dev Function to change the logic contract
    * @param _newLogicContract address of new logic contract
    */
    function changeLogic(address _newLogicContract) public {
        logicContract = _newLogicContract;
    }

   /**
    * @dev Fallback function to redirect calls
    */
    function fallback() public {
        require(logicContract.delegatecall(msg.data));
    }
}


// proxyContract.changeLogic(<logic_contract_address>);

// proxyContract.sendTransaction({
//     input: <function_signature>
//     ...
// });


/**
 * Logic contract
 * Increments the state variable value by 2
 */
contract AddTwo {

    uint result;

   /**
    * @dev 
    */
    function increment() public {
        result = result + 2;
    }
}