// Create a contract object
var MyContract = web3.eth.contract("<ABI>");
// Create an instance with address
var contractInstance = MyContract.at("<Address>");

var contractInstance = new web3.eth.Contract(
    "<ABI>",
    "<Address>"
);


 // Solidity contract
// contract Test {
//     function sample (uint _a) pure public returns (uint) {
//         return _a * 2;
//     }
// }

// For v0.2x.x
var result = contractInstance.sample(10);
console.log(result) // 20
// For v1.x.x
MyContract.methods.sample(10).call()
    .then(console.log); // 20

// Solidity contract
// contract Test {
//     function sample () pure public
//         returns (string testString, uint testNumber) {
//         return ("hello", 100);
//     } 
// }

// For v0.2x.x
var result = contractInstance.sample(); console.log(result);
// Output
// {
//     '0': 'hello',
//     '1': '100' 
// }
// For v1.x.x
MyContract.methods.sample().call()
    .then(console.log);
// Output
// Result {
//     '0': 'hello',
//     '1': '100',
//     testString: 'hello',
//     testNumber: '100'
// }