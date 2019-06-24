// function function_name(<function_type_parameters>)
//     <internal | external>
//     <pure | view | payable>
//     returns (<function_return_types>) {
//     // ...
// }

function calculate (
    uint num, // uint type
    function (uint) pure returns (uint) fun // function type
    ) { ... }
    
function calculate (
    uint num, 
    function (uint) pure returns (uint) fun
    ) internal pure returns (uint r) {
    r = fun(num);
}

function square(uint x) 
    internal pure returns (uint) {
    return x * x;
}


pragma solidity ^0.4.16;

/**
 * Contract which uses function as a type
 */
contract Sample {
  
    function calc(uint num) 
        public pure returns (uint) {
        return calculate(num, square);
    }

    function square(uint x) 
        internal pure returns (uint) {
        return x * x;
    }

    function calculate(
        uint self, 
        function (uint) pure returns (uint) f)
        internal pure returns (uint r) {
        r = f(self);
    }
}
