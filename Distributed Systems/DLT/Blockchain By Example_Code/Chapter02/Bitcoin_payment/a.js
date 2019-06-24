'use strict';

var bitcore_lib = require('bitcore-lib');
var PaymentProtocol = require('bitcore-payment-protocol');
var express = require('express');
var bodyParser = require('body-parser');
var URI = require('bitcore-lib/lib/uri');
var request=require("request");
const https = require('https');
var fs=require("fs");


/*********************General settings***************************/

var dcert= fs.readFileSync('./keys/cert.der');
var mcert= fs.readFileSync('./keys/cert.pem'); // for Https server
var mkey= fs.readFileSync('./keys/key.pem');


var credentials = {key: mkey, cert: mcert};
var app = express();

bitcore_lib.Networks.defaultNetwork = bitcore_lib.Networks.testnet;// the project run only on testnet
var Merchant_address ="mhc5YipxN6GhRRXtgakRBjrNUCbz6ypg66";

/* Alternatively you can generate random address using 
var privateKey =bitcore_lib.PrivateKey();//you can use a specific private key as argument
var publicKey = bitcore_lib.PublicKey(privateKey);
bitcore_lib.Address(publicKey, bitcore_lib.Networks.defaultNetwork ));
*/
/*********************Server's IP********************************/ 
var os = require('os');

var interfaces = os.networkInterfaces();
var addresses = [];
for (var k in interfaces) {
    for (var k2 in interfaces[k]) {
        var address = interfaces[k][k2];
        if (address.family === 'IPv4' && !address.internal) {
            addresses.push(address.address);
        }
    }
}

var IP=addresses[0];
var port=8883;
var http_port=3000;

/*****************Payment URI composition*********************************/

function compose_uri(amount_to_pay){
var pay_url = "http://"+IP+":"+http_port+"/request";
var uriString = new URI({
  address: Merchant_address,
  amount : amount_to_pay, // in satoshis
  message: 'payment request'
});

var paymentUri = uriString+"&r="+pay_url;
return paymentUri;
}
/*****************************************************************/

app.get("/", function(req, res){
  res.send('Bitcoin Payment protocol');
});

/************first action*****************************************/
var path    = require("path");
app.use(express.static(path.join(__dirname + '/views')));//Now, you can load the files that are in the views directory:


app.get('/checkout',function(req,res){
       
   res.sendFile(path.join(__dirname+'/views/index.html'));

});

/************Payment Processing **********************************************/

app.use(bodyParser.json());

app.post("/ProcessingPayment", function(req, res){
var amount_=req.body.amount; 
var resp=compose_uri(amount_)+"?amount="+amount_;
res.send(resp);
});


// app.use(bodyParser.json());   
 var urlencodedParser = bodyParser.urlencoded({ extended: false });

 app.get("/request", urlencodedParser, function(req, res){
  
 var amount = req.query.amount;
 amount = (amount === undefined) ? 0 : amount; // set amount to 0 if undefined
 
/**************prepare output to request payment *************************************/
// define the refund outputs
  var merchant_outputs = []; // Where payment should be sent
  var outputs = new PaymentProtocol().makeOutput();
  outputs.set('amount', amount);
  var script = bitcore_lib.Script.buildPublicKeyHashOut(Merchant_address.toString());
  outputs.set('script', script.toBuffer());
  merchant_outputs.push(outputs.message);

 /***** make payment detail* PaymentRequest message, which contains meta-information about the merchant and a digital signature. *************/
  var details = new PaymentProtocol().makePaymentDetails();
  var now = Date.now() / 1000 | 0;
  details.set('network', 'test');
  details.set('outputs', merchant_outputs);
  details.set('time', now);
  details.set('expires', now + 60 * 60 * 24);
  details.set('memo', 'A payment request from the merchant.');
  details.set('payment_url', "http://"+IP+":"+http_port+"/payment?id=12345");
  details.set('merchant_data', new Buffer("Transaction N 12345")); // identify the request

/************** form the request + sign it ****************************************************/
  var request = new PaymentProtocol().makePaymentRequest();
  request.set('payment_details_version', 1);
  var certificates = new PaymentProtocol().makeX509Certificates();
  certificates.set('certificate',dcert);
  request.set('pki_type', 'x509+sha256');
  request.set('pki_data', certificates.serialize());
  request.set('serialized_payment_details', details.serialize());
  request.sign(mkey);
  var rawbody = request.serialize();

  res.set({
    'Content-Type': PaymentProtocol.PAYMENT_REQUEST_CONTENT_TYPE,
    'Content-Length': request.length,
    'Content-Transfer-Encoding': 'binary'
  });
/******************response For Browser******************************************************************/
 if(req.query.browser==1){
 var buf = new Buffer(rawbody, 'binary').toString('base64');
 res.contentType(PaymentProtocol.PAYMENT_REQUEST_CONTENT_TYPE);
 res.send(buf);

}
/********************response for bitcoin client**********************************************************/
else 
{res.status(200).send(rawbody); }
 
});

/*****************Receive a payment  ************************************************************/
  var rawBodyParser = bodyParser.raw({type: PaymentProtocol.PAYMENT_CONTENT_TYPE});
  app.post("/payment", rawBodyParser, function(req, res){
 
/************in case sender create payment using bitcore*****************************************/
  var body = PaymentProtocol.Payment.decode(req.body);
  var payment = new PaymentProtocol().makePayment(body);
  var transaction = payment.get('transactions');
  var refund_to = payment.get('refund_to'); //output where a refund should be sent. 
  var memo = payment.get('memo');
//extract and brodcast tx to network
/***********************send back ACK********************************************************/
  var ack = new PaymentProtocol().makePaymentACK();
  ack.set('payment', payment.message);
  ack.set('memo', 'Payment processed,Thank you ;) \n invoice ID :'+req.query.id);
//store invoice details in database  
  var rawack = ack.serialize();
  res.set({
  'Content-Type': PaymentProtocol.PAYMENT_ACK_CONTENT_TYPE,
  'Content-Length': rawack.length,
  });
  res.send(rawack);

});

/*********************** Invoicing: Message displayed on invoice*****************************/
app.get("/invoice", urlencodedParser, function(req, res){
 
 var invoice_id = req.query.id;
 var detail="details about the invoice N:"+invoice_id; //Request details from database  
 res.send(detail);
});

/*********************** Node server *****************************/
app.listen(http_port, function(){
  console.log("-http Server listening on :"+IP+":"+ http_port);
});

https.createServer(credentials, app).listen(port, function(){
  console.log("-https Server listening on :"+IP+":"+ port);
});/**/
