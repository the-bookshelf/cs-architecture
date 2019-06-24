uint[10] numbers;

struct Person {
    uint id;
    string name;
}
Person[] candidates;

// array of 3 dynamic arrays of bool
bool[][3] table;

contract test {
    address[] contributors;

    function donate() public payable {
        contributors.push(msg.sender);
    }
    
    function getContributorsCount() view public 
        returns (uint) {
        return contributors.length; 
    }
}

contract test {

    struct Person {
        uint id;
        string name;
    }
    
    mapping(address => Person) employees;
    
    mapping(address => uint) balances;
    
    function insert(address _employee, uint _id, 
        string _name, uint _balance) public {
        employees[_employee] = Person({
            id: _id,
            name: _name
        });
        balances[_employee] = _balance;
    }
}

contract test {
    
    mapping(address => mapping(uint => bytes32)) dataStore;
    
    function insert(uint _id, bytes32 _value) public {
        dataStore[msg.sender][_id] = _value;
    }
}