mapping(address => uint[]) kitties;

function getKitties(address _owner) returns(uint[]) {
    return kitties[_owner];
}

mapping(uint => address) kittyToOwner;

modifier onlyOwner(uint _id) {
    require(kittyToOwner[_id] == msg.sender);
    _;
}

function transfer(uint _id, address _to) onlyOwner(_id) {
    // Code to transfer the kitty
}

ecrecover(hash, v, r, s);