uint public bonusPercent = 20;
uint public bonusDuration = 1 days;

function calculateBonus(uint _toknes) public view
    returns (uint bonusTokens) {
    // calculate bonus
}

function calculateBonus(uint _toknes) public view
    returns (uint bonusTokens) {
    if(block.timestamp <= openingTime + bonusDuration) {
        // Token purchased during bonus duration
    } else {
        // Bonus duration finished
    }
}

function calculateBonus(uint _toknes) public view
    returns (uint bonusTokens) {
    if(block.timestamp <= openingTime + bonusDuration) {
        bonusTokens = _tokens.mul(bonusPercent).div(100);
    } else {
        bonusTokens = 0;
    }
}

contract BonusContract {
    using SafeMath for uint;

    // Percentage of bonus tokens to allocate
    uint public bonusPercent = 20;
    // Bonus duration
    uint public bonusDuration = 1 days;

    /** 
    * @dev Calculate the bonus tokens for each purchase
    * @param _tokens Number of tokens purchased
    * @return Amount of bonus tokens 
    */
    function calculateBonus(uint _toknes) public view 
        returns (uint bonusTokens) {
        if(block.timestamp <= openingTime + bonusDuration) {
            // Token purchased during bonus duration
            bonusTokens = _toknes.mul(bonusPercent).div(100);
        } else {
            // Bonus duration finished
            bonusTokens = 0;
        }
    }
}

// Custom data type to store bonus parameter
struct Bonus {
    uint startTime;
    uint endTime;
    uint bonusPercent
}

// Array of fixed length to store 3 bonus levels
Bonus[3] bonus;

constructor() {
    bonus[0].startTime = openingTime;
    bonus[0].endTime = openingTime + 1 days;
    bonus[0].bonusPercent = 30;

    bonus[1].startTime = openingTime + 1 days;
    bonus[1].endTime = openingTime + 2 days;
    bonus[1].bonusPercent = 15;

    ...
}

contract BonusContract {
    using SafeMath for uint;

    // Custom data type to store bonus parameter
    struct Bonus {
        uint startTime;
        uint endTime;
        uint bonusPercent;
    }

    // Array of fixed length to store 3 bonus levels
    Bonus[3] bonus;

    constructor() public {
        bonus[0].startTime = openingTime;
        bonus[0].endTime = openingTime + 1 days;
        bonus[0].bonusPercent = 30;

        bonus[1].startTime = openingTime + 1 days;
        bonus[1].endTime = openingTime + 2 days;
        bonus[1].bonusPercent = 15;

        bonus[2].startTime = openingTime + 2 days;
        bonus[2].endTime = openingTime + 3 days;
        bonus[2].bonusPercent = 5;
    }

    /**
    * @dev Calculate bonus tokens
    * @param _toknes Tokens purchased
    * @return Number of bonus tokens
    */
    function calculateBonus(uint _toknes) public view 
        returns (uint bonusTokens) {

        // Default bonus amount
        bonusTokens = 0;

        if(block.timestamp >= bonus[0].startTime 
            && block.timestamp < bonus[0].endTime) {

            bonusTokens = 
                _toknes.mul(bonus[0].bonusPercent).div(100);

        } else if(block.timestamp >= bonus[1].startTime 
            && block.timestamp < bonus[1].endTime) {

            bonusTokens = 
                _toknes.mul(bonus[1].bonusPercent).div(100);

        } else if(block.timestamp >= bonus[2].startTime 
            && block.timestamp < bonus[2].endTime) {

            bonusTokens = 
                _toknes.mul(bonus[2].bonusPercent).div(100);
        }
    }
}
