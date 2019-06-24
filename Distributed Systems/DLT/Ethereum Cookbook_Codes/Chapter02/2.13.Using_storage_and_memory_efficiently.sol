uint storage sum;

uint memory calc;

pragma solidity ^0.4.22;

contract Storage {

    struct Name {
        string fName;
        string lName;
    }

    mapping(address => Name) public names;

    function setName(string _fName,string _lName) public {
        // Declared as a storage pointer
        Name n = names[msg.sender];
        // Modifies state variable
        n.fName = _fName;
        n.lName = _lName;
    }
}

pragma solidity ^0.4.22;

contract Memory {

    struct Name {
        string fName;
        string lName;
    }

    mapping(address => Name) public names;

    function setName(string _fName,string _lName) public {
        Name memory n = Name({
            fName: _fName,
            lName: _lName
        });
        names[msg.sender] = n;
    }
}