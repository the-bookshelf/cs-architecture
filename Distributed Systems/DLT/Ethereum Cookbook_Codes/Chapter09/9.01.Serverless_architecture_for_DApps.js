// // Solidity contract
// contract Test {
//     function getData() returns(string data) {
//         return "Hello world!";
//     }
// }

//Web3JS
var TestContract = new web3.eth.contract("<abi>", "<address>");
TestContract.methods.getData().call().then(console.log);

if (typeof web3 !== 'undefined') {
    // Use Mist/MetaMask's provider
    web3 = new Web3(web3.currentProvider);
} else {
    // if no provider is available
    web3 = new Web3(new Web3.providers.HttpProvider("http://localhost:8545"));
}

TestContract.events.TestEvent({
    filter: {  },
    fromBlock: 0
}, function(error, event){ 
    console.log(event); 
})
