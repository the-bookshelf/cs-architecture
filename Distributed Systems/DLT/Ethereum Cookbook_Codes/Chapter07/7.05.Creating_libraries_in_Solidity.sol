// libary libName {
//     ....
// }

// <library>.<function>();

library lib {
    function getBal() returns (uint) {
        return address(this).balance;
    }
}

contract Sample {
    function getBalance() returns (uint) {
        return lib.getBal();
    }
 
    function() payable {}
}

pragma solidity ^0.4.23;

/**
 * Calc library for addition and subtraction
 * This library is for illustration purpose only. 
 * Do not use it in any contracts.
 */ 
library calc {

   /**
    * @dev Function to add two numbers
    * @param a first uint to add
    * @param b second uint to add
    */
    function add(uint a, uint b) public 
        returns (uint) {
        return a + b;
    }

   /**
    * @dev Function to find difference between two numbers
    * @param a uint value to subtract from
    * @param b uint value to subtract
    */
    function sub(uint a, uint b) public 
        returns (uint) {
        return a - b;
    }
}

/**
 * Contract which uses the calc library
 */ 
contract Sample {

   /**
    * @dev Function to test addition
    * @return Boolean to denote success/failure
    */
    function testAdd() public returns (bool) {
        uint result = calc.add(5, 1);
        return (result == 5 + 1);
    }

   /**
    * @dev Function to test subtraction
    * @return Boolean to denote success/failure
    */
    function testSub() public returns (bool) {
        uint result = calc.sub(5, 1);
        return (result == 5 - 1);
    }
}

library Lib {
    // ...
}

contract A {
    // Lib is the library and B is the type
    using Lib for B; 
    // ...
}


pragma solidity ^0.4.23;

/**
 * Calc library for addition and subtraction
 * This library is for illustration purpose only. 
 * Do not use it in any contracts.
 */ 
library calc {

   /**
    * @dev Function to add two numbers
    * @param a first uint to add
    * @param b second uint to add
    */
    function add(uint a, uint b) public 
        returns (uint c) {
        c = a + b;
        assert(c >= a);
        return c;
    }

   /**
    * @dev Function to find difference between two numbers
    * @param a uint value to subtract from
    * @param b uint value to subtract
    */
    function sub(uint a, uint b) public 
        returns (uint) {
        assert(b <= a);
        return a - b;
    }
}

/**
 * Contract which uses the calc library
 */ 
contract Sample {
    
    using calc for uint;

   /**
    * @dev Function to test addition
    * @return Boolean to denote success/failure
    */
    function testAdd() public returns (bool) {
        return (uint(5).add(1) == 5 + 1);
    }

   /**
    * @dev Function to test subtraction
    * @return Boolean to denote success/failure
    */
    function testSub() public returns (bool) {
        return (uint(5).sub(1) == 5 - 1);
    }
}



// --libraries "SafeMath:0x.. StringUtils:0x..."
// --libraries <filename>
