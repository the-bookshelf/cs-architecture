pragma solidity^0.4.24;

contract ERC20 {

    // ...

    function approve(address _spender, uint256 _value) 
        public returns (bool) {
        allowed[msg.sender][_spender] = _value;
        emit Approval(msg.sender, _spender, _value);
        return true;
    }

    //...

}

// ERC20.approve(address(Y), A); // from: X
// ERC20.approve(address(Y), B); // from: X
// ERC20.transferFrom(address(Z), A); // from: Y
// ERC20.transferFrom(address(Z), B); // from: Y
// ERC20.approve(address(Y), 0); // from: X
// ERC20.approve(address(Y), B); // from: X

pragma solidity^0.4.24;

contract ERC20 { 

    function approve(address _spender, uint256 _value) { }

    // Function to increase allowance
    function increaseApproval(
        address _spender, 
        uint256 _addedValue)
        public returns (bool) 
    {
        // Uses safeMath.sol
        allowed[msg.sender][_spender] = 
            (allowed[msg.sender][_spender].add(_addedValue));
        emit Approval(
            msg.sender, 
            _spender, 
            allowed[msg.sender][_spender]
        );
        return true;
    }

    // Function to decrease allowance
    function decreaseApproval(
        address _spender, 
        uint256 _subtractedValue) 
        public returns (bool) 
    { 
        uint256 oldValue = allowed[msg.sender][_spender]; 
        if (_subtractedValue > oldValue) { 
            allowed[msg.sender][_spender] = 0; 
        } else { 
            allowed[msg.sender][_spender] = 
                oldValue.sub(_subtractedValue); 
        } 
        emit Approval(
            msg.sender, 
            _spender, 
            allowed[msg.sender][_spender]
        ); 
        return true; 
    }

    // ...

}