pragma solidity^0.4.24;

contract MultiSig {
    // Mapping to save the ownership status
    mapping (address => bool) public isOwner;
    // Owners array
    address[] public owners;

    // Returns list of owners.
    function getOwners() public view returns (address[]) {
        return owners;
    }

    // Modifier to restrict access
    modifier onlyOwner(address owner) {
        require(isOwner[owner]);
        _;
    }
}


uint public required;

constructor(address[] _owners, uint _required) public {
    for (uint i = 0; i < _owners.length; i++) {
        require(!(isOwner[_owners[i]] || _owners[i] == 0));
        isOwner[_owners[i]] = true;
    }
    owners = _owners;
    required = _required;
}

struct Transaction {
    address destination;
    uint value;
    bytes data;
    bool executed;
}

mapping (uint => Transaction) public transactions;
// Stores the confirmations from each address
mapping (uint => mapping (address => bool)) public confirmations;
// Total transaction count for calculating id
uint public transactionCount;


function addTransaction(
    address destination, 
    uint value, 
    bytes data) 
    internal returns (uint transactionId) 
{
    transactionId = transactionCount;
    transactions[transactionId] = Transaction({
        destination: destination,
        value: value,
        data: data,
        executed: false
    });
    transactionCount += 1;
    emit Submission(transactionId);
}

function isConfirmed(uint transactionId) 
    public constant returns (bool) 
{
    uint count = 0;
    for (uint i=0; i<owners.length; i++) {
        if (confirmations[transactionId][owners[i]])
            count += 1;
        if (count == required)
            return true;
    }
}


function executeTransaction(uint transactionId) public {
    if(isConfirmed(transactionId)) {
        Transaction storage trnx = transactions[transactionId];
        if (trnx.destination.call.value(trnx.value)(trnx.data)) {
            trnx.executed = true;
            emit Execution(transactionId);
        }
    }
}


function confirmTransaction(uint transactionId) 
    public onlyOwner(msg.sender) 
{
    require(!confirmations[transactionId][msg.sender]);
    confirmations[transactionId][msg.sender] = true;
    emit Confirmation(msg.sender, transactionId);
    executeTransaction(transactionId);
}


pragma solidity ^0.4.24;

contract MultiSig {

    struct Transaction {
        address destination;
        uint value;
        bytes data;
        bool executed;
    }

    mapping (uint => Transaction) public transactions;
    mapping (uint => mapping (address => bool)) public confirmations;
    uint public transactionCount;

    mapping (address => bool) public isOwner;
    address[] public owners;

    uint public required;

    event Confirmation(
        address indexed sender, 
        uint indexed transactionId
    );
    event Submission(uint indexed transactionId);
    event Execution(uint indexed transactionId);

    modifier onlyOwner(address owner) {
        require(isOwner[owner]);
        _;
    }

    // Fallback function to accept deposits
    function() public payable { }

    // Constructor sets owners and required confirmations.
    constructor(address[] _owners, uint _required) public {
        for (uint i=0; i<_owners.length; i++) {
            require(!(isOwner[_owners[i]] || _owners[i] == 0));
            isOwner[_owners[i]] = true;
        }
        owners = _owners;
        required = _required;
    }

    // Allows an owner to submit and confirm a transaction.
    function submitTransaction(
        address destination, 
        uint value, 
        bytes data) 
        public returns (uint transactionId) 
    {
        transactionId = addTransaction(destination, value, data);
        confirmTransaction(transactionId);
    }

    // Allows an owner to confirm a transaction.
    function confirmTransaction(uint transactionId) 
        public onlyOwner(msg.sender) 
    {
        require(!confirmations[transactionId][msg.sender]);
        confirmations[transactionId][msg.sender] = true;
        emit Confirmation(msg.sender, transactionId);
        executeTransaction(transactionId);
    }

    // Allows anyone to execute a confirmed transaction.
    function executeTransaction(uint transactionId) public {
        if(isConfirmed(transactionId)) {
            Transaction storage trnx = transactions[transactionId];
            if (trnx.destination.call.value(trnx.value)(trnx.data)) {
                trnx.executed = true;
                emit Execution(transactionId);
            }
        }
    }

    // Returns the confirmation status of a transaction.
    function isConfirmed(uint transactionId) 
        public constant returns (bool) 
    {
        uint count = 0;
        for (uint i=0; i<owners.length; i++) {
            if (confirmations[transactionId][owners[i]])
                count += 1;
            if (count == required)
                return true;
        }
    }

    // Adds a new transaction to the transaction mapping
    function addTransaction(
        address destination, 
        uint value, 
        bytes data) 
        internal returns (uint transactionId) 
    {
        transactionId = transactionCount;
        transactions[transactionId] = Transaction({
            destination: destination,
            value: value,
            data: data,
            executed: false
        });
        transactionCount += 1;
        emit Submission(transactionId);
    }

    // Returns list of owners.
    function getOwners() public view returns (address[]) {
        return owners;
    }
}
