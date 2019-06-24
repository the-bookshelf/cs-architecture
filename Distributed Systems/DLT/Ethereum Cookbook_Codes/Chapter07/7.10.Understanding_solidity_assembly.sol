// assembly {
//     // assembly code
// }

let x
let y := 2

function f(uint x) public {
    assembly {
        x := sub(x, 1)
    }
}


assembly {
    for { let i := 0 } lt(i, 10) { i := add(i, 1) } { 
        y := add(y, 1)
    }
}

assembly {
    if lt(x, 0) { 
        x := add(x, 1) 
    }
}

assembly {
    switch x
        case 0 {
            y := add(x, 1)
        }
        case 1 {
            y := add(x, 2)
        }
        default {
            y := 0
        }
}

assembly {
    function power(x, y) -> result {
        switch y
            case 0 {
                result := 1
            }
            case 1 {
                result := x
            }
            default {
                result := power(mul(x, x), div(y, 2))
                switch mod(y, 2)
                    case 1 {
                        result := mul(x, result)
                    }
            }
    }
}

assembly {
    let x := add(1, 2)
    let y := mul(x, 3)
}


// 10 0x80 mload add 0x80 mstore

// mstore(0x80, add(mload(0x80), 10))


pragma solidity ^0.4.24;

/**
 * Library contract with assembly code
 */
library AddressValidator {

    /**
     * Function to verify contract address
     * @param _address Address to verify
     * @returns isContract Returs verification result
     */
    function _isContract(address _address) public
        returns (bool isContract) {

        // Variable to store the code size
        uint codeSize;

        assembly {
            codeSize := extcodesize(_address)
        }

        isContract = codeSize > 0;
    }
}



function power2(x:u256, y:u256) -> result:u256
{
    switch y
    case 0:u256 { result := 1:u256 }
    case 1:u256 { result := x }
    default:
    {
        result := power(mul(x, x), div(y, 2:u256))
        switch mod(y, 2:u256)
            case 1:u256 { result := mul(x, result) }
    }
}