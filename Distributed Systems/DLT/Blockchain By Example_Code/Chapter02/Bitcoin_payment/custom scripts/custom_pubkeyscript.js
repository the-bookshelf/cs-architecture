var bitcore = require('bitcore-lib');
bitcore.Transaction = require('bitcore-lib').Transaction;
bitcore.Script = require('bitcore-lib').Script;


var lockingscript = bitcore.Script().add('OP_13')
.add('OP_ADD')
.add('OP_15')
.add('OP_EQUAL')

var utxo='56eeed0220bd77b3f9805e18725fc111977b13a6ef824f9cceeb211eeefdacfe'
var Saddress='mxXKY9HfcZruJp15eGonMz2hzVNjT22sdx'
var pkey='cPJCt9r5eu9GJz1MxGBGgmZYTymZqpvVCZ6bBdqQYQQ5PeW4h74d'
var Taddress='n1PoDECeUwbXgktfkNkBcmVXtD2CYUco2c'

var g_utxos = [{"address":Saddress,"txid":utxo,
"vout":0,"scriptPubKey":"210330b8e88054629399e6c90b37503f07fbc1f83aa72444dd2cfd9050c3d08d75fcac","amount":50.0}];

var transaction = new bitcore.Transaction();
transaction = transaction.from(g_utxos);
transaction = transaction.to(Taddress, 4000000000);
transaction = transaction.fee(0.0001*100000000);

transaction = transaction.addOutput(new bitcore.Transaction.Output({
    script: lockingscript,
    satoshis: 1000000000,
address:Taddress
  }));
transaction=transaction.sign(pkey);
console.log("Raw Transaction\n"+transaction);

var sys = require('sys')
var exec = require('child_process').exec;
function puts(error, stdout, stderr) { console.log(stdout) }
exec("bitcoin-cli decoderawtransaction "+transaction, puts);

