web3.shh

var identity = web3.shh.newIdentity();

web3.shh.post({
    // ...
});


shh.post({
    "from": identity,
    "topics": [ web3.fromAscii("whisper app") ],
    "payload": [ 
        web3.fromAscii("Ethereum"), 
        web3.fromAscii("Whisper") 
    ],
    "ttl": 100,
    "priority": 1000
});

var listner = shh.watch({
    "topics": [
        web3.fromAscii("whisper app"), 
        identity 
    ],
    "to": identity
});

listner.arrived(function(message) {
    // Executed when a new message received
    console.log(web3.toAscii(message.payload))
    console.log(message.from);
});