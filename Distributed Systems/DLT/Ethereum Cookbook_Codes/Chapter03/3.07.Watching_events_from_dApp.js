// pragma solidity ^0.4.22;
// contract eventEmitter {
//     event simpleEvent(address _sender);
//     event indexedEvent(address indexed _sender, uint _id);
//     function simpleEmit() public {
//         // do something
//         emit simpleEvent(msg.sender);
//     }
//     function indexedEmit(uint _id) public {
//         // do something
//         emit indexedEvent(msg.sender, _id);
//     }
// }


// *** FILTER EVENTS ***

// For v0.2x.x
contractInstance.MyEvent([options]).watch([callback]);
// For v1.x.x
contractInstance.events.MyEvent([options][, callback])


// For web3.js v0.2x.x
var eventContract = web3.eth.contract(abi);
var eventContractInstance = eventContract.at(address);
// Create an event instance
var simpleEventInstance = myContractInstance.simpleEvent({}, {
    fromBlock: 0,
    toBlock: 'latest'
});
// Start watching the event
simpleEventInstance.watch(function (error, result) {
    console.log(result);
});

// Stops watching the event
simpleEventInstance.stopWatching();

// For web3.js v1.x.x
eventContractInstance.events.simpleEvent({
    filter: {},
    fromBlock: 0
}, function (error, event) {
    console.log(event);
})
// For web3.js v1.x.x
eventContractInstance.events.simpleEvent({
    filter: {},
    fromBlock: 0
}).on('data', function (event) {
    console.log(event);
}).on('changed', function (event) {
    console.log(event);
}).on('error', console.error);

// For web3.js 0.2x.x
var indexedEventInstance = eventContractInstance.indexedEvent(
    {
        _sender: '0xce5C2D181f6DD99091351f6E6056c333A969AEC9'
    }, {
        fromBlock: 0,
        toBlock: 'latest'
    });
// Logs event emitted from a specific address
simpleEventInstance.watch(function (error, result) {
    console.log(result);
});


// For web3.js 1.x.x
eventContractInstance.events.indexedEvent({
    filter: {
        _sender: [
            '0xce5C2D181f6DD99091351f6E6056c333A969AEC9',
            '0xD0D18F4A02beb7E528cE010742Db1Cc992070135'
        ] // Use an array for OR condition
    },
    fromBlock: 0
}).on('data', function (event) {
    console.log(event);
}).on('error', console.error);


// *** GET PAST EVENTS ***

// For web3.js 0.2x.x
var simpleEventInstance = eventContractInstance.simpleEvent({}, {
    fromBlock: 0,
    toBlock: 'latest'
});

// All past logs
var eventResults = simpleEventInstance
    .get(function (error, logs) {
        console.log(logs);
    });

// For web3.js 1.x.x - Syntax
// contractInstance.getPastEvents(event[, options][, callback])

// For web3.js 1.x.x - Example
eventContractInstance.getPastEvents('simpleEvent', {
    filter: {},
    fromBlock: 0,
    toBlock: 'latest'
}, function (error, events) {
    console.log(events);
}).then(function (events) {
    console.log(events); // same result as the callback
});


// *** LISTEN TO ALL EVENTS ***
// For web3.js 0.2x.x
var events = eventContractInstance.allEvents({
    fromBlock: 0,
    toBlock: 'latest'
});
events.watch(function (error, result) {
    //...
});
events.get(function (error, logs) {
    //...
});

// For web3.js 1.x.x
eventContractInstance.getPastEvents({
    filter: {},
    fromBlock: 0
}).on('data', function (event) {
    console.log(event);
}).on('error', console.error);