// npm install web3
// npm install web3@0.20.6
// npm view web3 versions

// <script src="./dist/web3.min.js"></script>
var Web3 = require("web3");

// For v1.x.x
var Eth = require("web3-eth");
var Bzz = require("web3-bzz");
var Shh = require("web3-shh");

// For v0.2x.x
var web3 = new Web3(
    new Web3.providers.HttpProvider("http://localhost:8545")
);
// For v1.x.x
var web3 = new Web3("ws://localhost:8546");
var eth = new Eth("ws://localhost:8546");
var bzz = new Bzz("http://localhost:8500");
var shh = new Shh("ws://localhost:8546");

// For v0.2x.x
console.log(web3.eth.coinbase);
console.log(web3.eth.getBalance("<address>"));
// For v1.x.x
web3.eth.getCoinbase().then(console.log);
web3.eth.getBalance("<address>").then(console.log);