var bitcore = require('bitcore-lib');
bitcore.Transaction = require('bitcore-lib').Transaction;
bitcore.Script = require('bitcore-lib').Script;

 


var Taddress='mxXKY9HfcZruJp15eGonMz2hzVNjT22sdx'
var pkey='cP3pZnP8a48HEj8WEo6scg57ApxcGR3g5zK75xjmPQVW7faSaemc' 

var g_utxos = [{"address":'n1PoDECeUwbXgktfkNkBcmVXtD2CYUco2c',"txid":'c6758cf22346d3d8b7b6042b7701a5f07d140732bf5b93e1fb92ed250e5b6d20',
"vout":1,"ts":1534009869,"scriptPubKey":"5d935f87","amount":1.0,"confirmations":201,"confirmationsFromCache":false}];

var unlockingScript = bitcore.Script().add('OP_2');

var transaction = new bitcore.Transaction();
 transaction.addInput(new bitcore.Transaction.Input({prevTxId: 'c6758cf22346d3d8b7b6042b7701a5f07d140732bf5b93e1fb92ed250e5b6d20',outputIndex: 1,
  script: unlockingScript      }), unlockingScript, 10000);

transaction = transaction.to(Taddress, 90000000);
transaction = transaction.fee(0.0001*100000000);
console.log("Raw Transaction\n"+transaction);

var sys = require('sys')
var exec = require('child_process').exec;
function puts(error, stdout, stderr) { console.log(stdout) }
exec("bitcoin-cli decoderawtransaction "+transaction, puts);


