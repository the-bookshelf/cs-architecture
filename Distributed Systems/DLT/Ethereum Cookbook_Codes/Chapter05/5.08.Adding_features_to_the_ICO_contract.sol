uint256 public cap;


constructor(uint256 _cap, ....) 
    public {
    // Other validations and assignment

    require(_cap > 0); 
    cap = _cap;
}

function buyTokens(address _investor) public payable {
    require(weiRaised.add(_weiAmount) <= cap);
    //..
}

function capReached() public view 
    returns (bool) { 
    return weiRaised >= cap; 
}

uint256 public openingTime; 
uint256 public closingTime;

constructor(uint256 _openingTime, uint256 _closingTime) public { 
    require(_openingTime >= block.timestamp); 
    require(_closingTime >= _openingTime); 

    openingTime = _openingTime; 
    closingTime = _closingTime; 
}

modifier onlyWhileOpen { 
    require(block.timestamp >= openingTime 
        && block.timestamp <= closingTime); 
    _; 
}

function buyTokens(address _investor) public payable
    onlyWhileOpen {
    //...
}

function hasClosed() public view returns (bool) { 
    return block.timestamp > closingTime; 
}

contract additionalFeatues {
    uint256 public openingTime;
    uint256 public closingTime;
    uint256 public cap;
 
     /**
     * @dev Reverts if not in crowdsale time range.
     */
     modifier onlyWhileOpen {
         require(block.timestamp >= openingTime 
            && block.timestamp <= closingTime);
         _;
     }
 
     /**
     * @dev Constructor, takes crowdsale opening and closing times.
     * @param _openingTime Crowdsale opening time
     * @param _closingTime Crowdsale closing time
     */
 
    constructor(uint256 _openingTime, uint256 _closingTime, 
        uint256 _cap) public {
        require(_openingTime >= block.timestamp);
        require(_closingTime >= _openingTime);
        require(_cap > 0);
 
        cap = _cap;
        openingTime = _openingTime;
        closingTime = _closingTime;
    }
 
    /**
     * @dev Checks whether the period in which the crowdsale is 
     8 open has already elapsed.
     * @return Whether crowdsale period has elapsed
     */
     function hasClosed() public view returns (bool) {
         return block.timestamp > closingTime;
     }
 
     /**
     * @dev Checks whether the cap has been reached.
     * @return Whether the cap was reached
     */
     function capReached() public view returns (bool) {
         return weiRaised >= cap;
     }
 
     function buyTokens(address _investor, address spender)
         onlyWhileOpen {
         // ...
     }
}


mapping(address => uint256) public contributions; 
mapping(address => uint256) public caps;

require(contributions[_beneficiary].add(_weiAmount) <= caps[_beneficiary]);


function getUserCap(address _beneficiary) public 
    view returns (uint256) { 
    return caps[_beneficiary]; 
}

function getUserContribution(address _beneficiary) public 
    view returns (uint256) { 
    return contributions[_beneficiary]; 
}


function setUserCap(address _beneficiary, uint256 _cap) 
    external onlyOwner { 
    caps[_beneficiary] = _cap; 
} 

function setGroupCap( address[] _beneficiaries, uint256 _cap ) 
    external onlyOwner { 
    for (uint256 i = 0; i < _beneficiaries.length; i++) { 
        caps[_beneficiaries[i]] = _cap; 
    } 
}