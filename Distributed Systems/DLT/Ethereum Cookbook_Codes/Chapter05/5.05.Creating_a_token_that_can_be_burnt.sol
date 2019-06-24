pragma solidity ^0.4.23;

import "./ERC20.sol";

contract Burnable is ERC20 {
    ...
}


function burn(uint256 _value) public { }

require(_value <= balances[msg.sender]);


balances[_who] = balances[_who].sub(_value); 
totalSupply_ = totalSupply_.sub(_value);


// Event declaration
event Burn(address indexed burner, uint256 value);

// Emit the event during burning process
emit Burn(msg.sender, _value);

emit Transfer(msg.sender, address(0), _value);


pragma solidity ^0.4.23;
import "./ERC20.sol";

contract BurnableToken is BasicToken {
    event Burn(address indexed burner, uint256 value);

    /**
    * @dev Burns a specific amount of tokens.
    * @param _value The amount of token to be burned.
    */
    function burn(uint256 _value) public {
        _burn(msg.sender, _value);
    }
    
    /**
    * @dev Internal function for burning tokens.
    * @param _user Address to burn the tokens from.
    * @param _value Amount of token to be burned.
    */
    function _burn(address _user, uint256 _value) internal {
        require(_value <= balances[_user]);
        
        balances[_user] = balances[_user].sub(_value);
        totalSupply_ = totalSupply_.sub(_value);
        
        emit Burn(_user, _value);
        emit Transfer(_user, address(0), _value);
    }
}