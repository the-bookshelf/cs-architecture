pragma solidity ^0.4.21;

contract HelloWorld {
    // Here goes your smart contract code
}

function printSomething() {
    // things to do
}

return "hello world";

function printSomething() returns (string) { }

pragma solidity ^0.4.21;

contract HelloWorld {
    function printSomething() returns (string) {
        return "hello world";
    }
}

string textToPrint = "hello world";

function changeText(string _text) {
    textToPrint = _text;
}

function printSomething() returns (string) {
    return textToPrint;
}

function printSomething() view returns (string) { }


// Target compiler version
pragma solidity ^0.4.21;

contract HelloWorld {
    // State variable
    string textToPrint = "hello world";

    // State changing function
    function changeText(string _text) public {
        textToPrint = _text;
    }

    // Read-only function
    function printSomething() public view returns (string) {
        return textToPrint;
    }
}