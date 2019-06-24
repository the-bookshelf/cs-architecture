bool isAvailable = false;

// uint/int are aliases for uint256/int256
uint256 a = 9607111;
int32 b = 102;

address owner = 0x05ee546c1a62f90d7acbffd6d846c9c54c7cf94c;

owner.transfer(1 ether); // Throws an exception if failed

owner.send(1 ether); // Returns true or false

address productStore = 0x4db26171199535b2e4bae6615c9b8ffe33c41d74;
productStore.call("getProduct", "product_id_001");

productStore.call.gas(2000000).value(1 ether).("buyProduct", "product_id_001");

contract A {
    uint value;
    address public sender;
    // address of contract B
    address a = 0xef55bfac4228981e850936aaf042951f7b146e41; 

    function makeDelegateCall(uint _value) public {
        // Value of A is modified
        a.delegatecall(bytes4(keccak256("setValue(uint)")), _value);
    }
}

contract B {
     uint value;
     address public sender;

     function setValue(uint _value) public {
        value = _value;
        // msg.sender is preserved in delegatecall. 
        // It was not available in callcode.
        sender = msg.sender;
     }
}

uint[] dynamicSizeArray; 
uint[7] fixedSizeArray;


int8 a = 1;
uint b = a + 1200;

int u = -1;
uint v = uint(v);