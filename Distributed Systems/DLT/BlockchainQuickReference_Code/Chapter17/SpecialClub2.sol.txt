pragma solidity ^0.4.23;

contract SpecialClub {
    address public owner;
    // we set members to true if they are a member, false otherwise.
    mapping(address => bool) public members;
    mapping(address => bool) internal requests;
    mapping(address => mapping(address => bool)) votedOn;
    mapping(address => uint8) votes;
    
    constructor() public {
        owner = msg.sender;
    }
    
    modifier onlyOwner() {
      require(msg.sender == owner);
      _;
    }
    
    modifier onlyMember() {
        require(members[msg.sender] == true);
        _;
    }
    
    function approveMembership(address _address) onlyOwner external {
        members[_address] = true;
        requests[_address] = false;
        emit GrantedMembership(_address);
    } 
    
    function requestOwnership() external {
        requests[msg.sender] = true;
        emit RequestToJoin(msg.sender);
    }
    
    function voteInMember(address _address) onlyMember external {
        //don't allow re-votes
        require(!votedOn[_address][msg.sender]);
        votedOn[_address][msg.sender] = true;
        votes[_address] = votes[_address] + 1;
        if (votes[_address] >= 5) {
            members[_address] = true;
            requests[_address] = false;
            emit GrantedMembership(_address);
        }
    }
    
    event RequestToJoin(address _address);
    event GrantedMembership(address _address);
}