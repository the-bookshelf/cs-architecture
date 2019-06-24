struct Person { }

struct Person {
    uint16 id;
    bytes32 fName;
    bytes32 lName;
}

struct Person {
    uint16 id;
    bytes32 fName;
    bytes32 lName;
    Contact phone;
}

struct Contact {
    uint countryCode;
    uint number;
}

Person owner;
Person[] contributors;

contributors[2].fName = "John";
contributors[2].lName = "Doe";

Person storage contributor = contributors[2];
contributor.fName = "John";
contributor.lName = "Doe";

Contact memory myContact = Contact(91, 23232323);
// OR
Contact memory myContact = Contact({
    countryCode: 91,
    number: 23232323
});


pragma solidity ^0.4.21;

// This contract is for illustration purpose only.
contract CrowdFunding {

     // Defines a new type with two fields.
     struct Funder {
         address addr;
         uint amount;
         bool isApproved;
     }

     // Array of user defined type
     Funder[] contributors;

     function contribute() public payable {
         // Creates a new temporary memory struct
         // Initialises with the given values
         // You can also use Funder(msg.sender, msg.value).
         Funder memory contributor = Funder({
             addr: msg.sender, 
             amount: msg.value,
             isApproved: false
         });
 
         contributors.push(contributor);
     }
 
     // Function that changes a specific value of struct
     function approve(uint id) public {
         Funder storage contributor = contributors[id];
         contributor.isApproved = true;
     }

     // Function which returns struct value
     function getContributor(uint id) public view 
         returns (address, uint, bool) {
         Funder memory contributor = contributors[id];
         return (contributor.addr, 
                 contributor.amount, 
                 contributor.isApproved);
     }
}


enum Direction {
    // Options
}

enum Direction {
    North,
    East,
    South,
    West
}

Direction path = Direction.North;

function getCurrentChoice() view public 
    returns (uint) {
    return uint(path);
}

pragma solidity ^0.4.23;

contract Navigation {

    // enum declaration
    enum Direction {
        North,
        East,
        South,
        West
    }

    // Default direction
    Direction path = Direction.North;

    // Function which accepts enum as input
    function changeDirection(Direction dir) public {
        path = dir;
    }

    // Function which returns enum. Since enum is not part of ABI, return type will be changed to uint
    function getCurrentDirection() view public 
        returns (Direction) {
        return path;
    }
}