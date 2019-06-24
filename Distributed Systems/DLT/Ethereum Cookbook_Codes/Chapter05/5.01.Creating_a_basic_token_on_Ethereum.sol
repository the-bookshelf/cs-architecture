pragma solidity ^0.4.23;

contract ERC20 {
     ..
}

pragma solidity ^0.4.23;
import "./math/SafeMath.sol";

contract ERC20 {
    using SafeMath for uint256;
}

// Total number of tokens in existence
uint256 totalSupply_;

pragma solidity ^0.4.23;
import "./math/SafeMath.sol";

contract ERC20 {
    using SafeMath for uint256;

    // The total number of tokens in existence
    uint256 totalSupply_;

    function totalSupply() public view returns (uint256) { 
        return totalSupply_; 
    }
}

mapping(address => uint256) balances;


pragma solidity ^0.4.23;
import "./math/SafeMath.sol";

contract ERC20 {
    using SafeMath for uint256;

    mapping(address => uint256) balances;

    /** 
    * @dev To get the balance of the specified address. 
    * @param _owner The address to query the the balance of. 
    * @return An uint256 representing the amount owned by the passed address. 
    */
    function balanceOf(address _owner) public view 
        returns (uint256) { 
        return balances[_owner]; 
    }
}

pragma solidity ^0.4.23;
import "./math/SafeMath.sol";

contract ERC20 {
    using SafeMath for uint256;

    mapping(address => uint256) balances;
    uint256 totalSupply_; 

    constructor(uint _totalSupply) {
        totalSupply_ = _totalSupply;
        // Assigns all tokens to the owner
        balances[msg.sender] = _totalSupply;
    }

    /** 
    * @dev An uint256 representing the total number of tokens. 
    */
    function totalSupply() public view returns (uint256) { 
        return totalSupply_;
    }

    /** 
    * @dev Gets the balance of the specified address. 
    * @param _owner The address to query the the balance of. 
    * @return An uint256 representing the amount owned by 
    * the passed address. 
    */
    function balanceOf(address _owner) public view 
        returns (uint256) { 
        return balances[_owner]; 
    }
}