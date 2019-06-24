pragma solidity ^0.4.21;

contract HelloWorld {
    string message = "hello world";

    function setMessage(string msg_)  public {
        message = msg_;
    }
    function getMessage() public view returns (string){
        return message;
    }
}

