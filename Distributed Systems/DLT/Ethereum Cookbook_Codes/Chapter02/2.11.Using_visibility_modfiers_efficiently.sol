pragma solidity ^0.4.21;
contract Visibility {
    // Public state varibles generate an automatic getter
    uint public limit = 110021;

    // Accessible externally and internally
    function changeLimit(uint _newLimit) public {
        limit = _newLimit;
    }
}

// [{
//       "constant":false,
//       "inputs":[{
//             "name":"_newLimit",
//             "type":"uint256"
//          }],
//       "name":"changeLimit", // public function
//       "outputs":[],
//       "payable":false,
//       "stateMutability":"nonpayable",
//       "type":"function"
//   },{
//       "constant":true,
//       "inputs":[],
//       "name":"limit", // public state variable
//       "outputs":[{
//             "name":"",
//             "type":"uint256"
//          }],
//       "payable":false,
//       "stateMutability":"view",
//       "type":"function"
//  }]

pragma solidity ^0.4.21;
contract Visibility {
    // External function
    function f() external {
        // Do something
    }
}

pragma solidity ^0.4.21;

contract Base {
    // Internal state varible
    uint internal limit = 110021;
    
    function update(uint _newLimit) internal {
        limit = _newLimit;
    }
}

contract Visibility is Base {
    function increment() public {
        uint newLimit = limit + 1;
        update(newLimit);
    }
}

pragma solidity ^0.4.21;

contract Visibility {
    // Private state varible
    uint private limit = 110021;
    
    function update(uint _newLimit) public {
        limit = _newLimit;
    }
}