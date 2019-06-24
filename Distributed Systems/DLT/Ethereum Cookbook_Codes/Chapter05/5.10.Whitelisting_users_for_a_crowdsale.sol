mapping(address => bool) public whitelist;

function addToWhitelist(address _investor) external 
    onlyOwner { 
    whitelist[_investor] = true; 
}

function addMultipleToWhitelist(address[] _investors) 
    external onlyOwner { 
        for (uint256 i = 0; i < _investors.length; i++) { 
            whitelist[_investors[i]] = true; 
        }
}

function removeFromWhitelist(address _beneficiary) external 
    onlyOwner { 
    whitelist[_beneficiary] = false; 
}

modifier isWhitelisted(address _investor) { 
    require(whitelist[_investor]); 
    _; 
}

contract WhitelistContract {

    mapping(address => bool) public whitelist;

    /**
    * @dev Check if investor is whitelisted.
    */
    modifier isWhitelisted(address _investor) {
        require(whitelist[_investor]);
        _;
    }

    /**
    * @dev Adds an address to whitelist.
    * @param _investor Address to whitelist
    */
    function addToWhitelist(address _investor) external 
        onlyOwner {
        whitelist[_investor] = true;
    }

    /**
    * @dev Adds list of addresses to whitelist.
    * @param _investors Addresses to be added to the whitelist
    */
    function addManyToWhitelist(address[] _investors) external 
        onlyOwner {
        for (uint256 i = 0; i < _investors.length; i++) {
            whitelist[_investors[i]] = true;
        }
    }

    /**
    * @dev Removes single address from whitelist.
    * @param _investor Address to be removed to the whitelist
    */
    function removeFromWhitelist(address _investor) external 
        onlyOwner {
        whitelist[_investor] = false;
    }

    // Add isWhitelisted modifier to token buying function
    function buyTokens(address _investor, uint256 _weiAmount)
        public isWhitelisted(_investor) {
            // ..
    }

}