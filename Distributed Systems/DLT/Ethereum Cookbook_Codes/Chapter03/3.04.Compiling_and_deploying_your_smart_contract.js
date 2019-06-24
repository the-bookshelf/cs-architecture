// npm install solc --save

// pragma solidity ^0.4.21;
// contract HelloWorld {
//     string textToPrint = "hello world";
//     function changeText(string _text) public {
//         textToPrint = _text;
//     }
//     function printSomething() public view returns (string) {
//         return textToPrint;
//     }
// }

var contract = "pragma solidity ^0.4.21; contract HelloWorld { string textToPrint = 'hello world'; function changeText(string _text) public { textToPrint = _text; } function printSomething() public view returns (string) { return textToPrint; } }";

var solc = require("solc");
var output = solc.compile(contract, 1);

var contracts = {
    "library.sol": "library Lib { function f() pure returns (uint) { return 1;} }",
    "contract.sol": "import 'library.sol'; contract Test { function g() pure { Lib.f(); } } "
};
var output = solc.compile({ sources: contracts }, 1);

for (var contractName in output.contracts) {
    console.log(contractName);
    // Bytecode
    console.log(output.contracts[contractName].bytecode);
    // ABI
    console.log(output.contracts[contractName].interface);
}

var bytecode = output.contracts["HelloWorld"].bytecode;
web3.eth.sendTransaction({
    from: "0xce5C2D181f6DD99091351f6E6056c333A969AEC9",
    data: bytecode,
    gas: "4700000"
}, function (err, transactionHash) {
    if (!err)
        console.log(transactionHash);
});

var bytecode = output.contracts["HelloWorld"].bytecode;
web3.eth.sendTransaction({
    from: "0xce5C2D181f6DD99091351f6E6056c333A969AEC9",
    data: bytecode,
    gas: "4700000"
}).on('receipt', function (receipt) {
    console.log(receipt);
});


// For v0.2x.x
var helloworldContract = web3.eth.contract(abi);
var helloworld = helloworldContract.new({
    from: "0xce5C2D181f6DD99091351f6E6056c333A969AEC9",
    data: byteCode,
    gas: 4700000
}, function (e, contract) {
    if (typeof contract.address !== "undefined") {
        console.log("address: " + contract.address);
    }
});

// For v1.x.x
var helloWorld = new web3.eth.Contract(abi);
helloWorld.deploy({
    data: byteCode,
    arguments: [] // Constructor arguments
}).send({
    from: "0x1234567890123456789012345678901234567891",
    gas: 4700000
}).on("error", function (error) {
    console.error(error);
}).on("receipt", function (receipt) {
    console.log(receipt.contractAddress);
});

// For v0.2x.x
var gas = web3.eth.estimateGas({
    data: byteCode
});
console.log(gas);
// For v1.x.x
helloWorld.deploy({
    data: byteCode,
    arguments: []
}).estimateGas(function (err, gas) {
    console.log(gas);
});
