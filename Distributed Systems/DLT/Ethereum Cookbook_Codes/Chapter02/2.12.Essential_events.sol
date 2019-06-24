event Transfer(
    address _from,
    address _to,
    uint _value
);

function transfer(address _to, uint _value) public {
    // Function body
    emit Transfer(msg.sender, _to, _value);
}

event Transfer(
    address indexed _from,
    address indexed _to,
    uint _value
);

event Deposit(
    address _from;
    uint _value;
) anonymous;

pragma solidity ^0.4.21;

contract Token {
    
    address owner;

    event Transfer(address indexed _from, address indexed _to, uint _value);
    
    modifier onlyOwner {
        require(msg.sender == owner);
        _;
    }
    
    function Token() public {
        owner = msg.sender;
    }
    
    mapping(address => uint) public balances;
    
    function mint(address _to, uint _value) public onlyOwner {
        balances[_to] += _value;
        emit Transfer(address(0), _to, _value);
    }
    
    function transfer(address _to, uint _value) public {
        require(balances[msg.sender] >= _value);
        balances[msg.sender] -= _value;
        balances[_to] += _value;
        emit Transfer(msg.sender, _to, _value);
    }
}

// > var tokenContract = web3.eth.contract(<ABI>);
// > var tokenInstance = tokenContract.at(<Address>);

// > var transferEvent = tokenInstance.Transfer({}, {
//     fromBlock: 0, 
//     toBlock: 'latest'
//   });

// > var transferEvent = tokenInstance.Transfer({
//     _from: web3.eth.accounts[0]
//   }, {
//     fromBlock: 0, 
//     toBlock: 'latest'
//   });

// > transferEvent.watch(function(error, result) {
//     console.log(result);
//   });

// > tokenInstance.mint(web3.eth.accounts[1], 100, {
//     from: web3.eth.accounts[0]
//   });

// {
//    "address":"0x94b993cb18bd880e3ea4a278d50b4fb0cb4cb143",
//    "args":{
//       "_from":"0x0000000000000000000000000000000000000000",
//       "_to":"0x87db8fceb028cd4ded9d03f49b89124a1589cab0",
//       "_value":"100"
//    },
//    "blockHash":"0x817ab7205fe54c78b1d3e45358a350669714188bab4af78aaecdf200fa334f67",
//    "blockNumber":1605,
//    "event":"Transfer",
//    "logIndex":0,
//    "removed":false,
//    "transactionHash":"0x5b487f78045ef2e96d460b65a5d0c1a28a4ed12a3d38f0e16ccd529bff1fb42d",
//    "transactionIndex":0
// }

// > var transferLogs = transferEvent.get(function(error, logs){
//     console.log(logs);
//   });


// > transferEvent.stopWatching();
