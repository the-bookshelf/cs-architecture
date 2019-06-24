pragma solidity ^0.4.23;

contract HeroBattle {
    //...
}

struct Hero {
    string name;
    uint dna;
    uint32 level;
    uint16 winCount;
    uint16 lossCount;
}
    
Hero[] public heroes;

mapping (uint => address) public heroToOwner;
 
modifier ownerOf(uint _heroId) {
    require(msg.sender == heroToOwner[_heroId]);
    _;
}

function _createHero(string _name, uint _dna) external {
    //...
}

uint id = heroes.push(Hero(_name, _dna, 0, 0, 0)) - 1;
heroToOwner[id] = msg.sender;

// Declaration
event NewHero(uint heroId, string name, uint dna);
// Logging
emit NewHero(id, _name, _dna);

function attack(uint _heroId, uint _targetId) 
    external 
    ownerOf(_heroId) {
    //...
}

nonce++;
uint rand = uint(keccak256(now, msg.sender, nonce)) % 100;

if (rand <= 60) { // 60%
    myHero.winCount++;
    myHero.level++;
    enemyHero.lossCount++;
} else {
    myHero.lossCount++;
    enemyHero.winCount++;
}

emit attackWon(_heroId, _targetId, winnerId)


pragma solidity ^0.4.23;

contract HeroBattle {

    struct Hero {
        string name;
        uint dna;
        uint32 level;
        uint16 winCount;
        uint16 lossCount;
    }

    Hero[] public heroes;

    mapping (uint => address) public heroToOwner;

    uint nonce = 0;
    uint attackerProbability = 75;

    event NewHero(uint heroId, string name, uint dna);
    event attackWon(uint attackerId, uint targetId, uint winnerId);

    modifier ownerOf(uint _heroId) {
        require(msg.sender == heroToOwner[_heroId]);
        _;
    }

    /**
     * @dev Create a new hero
     * @param _name Namme of the hero
     * @param _dna DNA of the hero
     */
    function _createHero(string _name, uint _dna) external {
        uint id = heroes.push(Hero(_name, _dna, 0, 0, 0)) - 1;
        heroToOwner[id] = msg.sender;
        emit NewHero(id, _name, _dna);
    }

    /**
     * @dev Adds single address to whitelist.
     * @param _heroId Attacker hero id
     * @param _targetId Target hero id
     */
    function attack(uint _heroId, uint _targetId) 
        external ownerOf(_heroId) {
        Hero storage myHero = heroes[_heroId];
        Hero storage enemyHero = heroes[_targetId];

        uint winnerId;

        nonce++;
        uint rand = uint(keccak256(now, msg.sender, nonce)) % 100;

        if (rand <= attackerProbability) {
          myHero.winCount++;
          myHero.level++;
          enemyHero.lossCount++;
          winnerId = _heroId;
        } else {
          myHero.lossCount++;
          enemyHero.winCount++;
          winnerId = _targetId;
        }

        emit attackWon(_heroId, _targetId, winnerId);
    }
}
