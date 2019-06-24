"use strict";
var bitcore = require('bitcore-lib');
var PaymentProtocol = require('bitcore-payment-protocol');


function get_payment_details(rawbody ){
try {
var body = PaymentProtocol.PaymentRequest.decode(rawbody);
var request = (new PaymentProtocol()).makePaymentRequest(body);

var version = request.get('payment_details_version');
var pki_type = request.get('pki_type');
var pki_data = request.get('pki_data');
var serializedDetails = request.get('serialized_payment_details');
var signature = request.get('signature');
var verified = request.verify();
verified=(verified) ? "Valid" : verified;
var decodedDetails = PaymentProtocol.PaymentDetails.decode(serializedDetails);
var details = new PaymentProtocol().makePaymentDetails(decodedDetails);
var network = details.get('network');
var outputs = details.get('outputs');
var time = details.get('time');
var expires = details.get('expires');
var memo = details.get('memo');
var payment_url = details.get('payment_url');
var merchant_data = details.get('merchant_data');
   
$('.details').append('<h2>Invoice :</h2><ul><li> Network : '+network+'</li><li>Transaction Timestamp : '+time+'</li><li>Expiration : '+expires+'</li><li>Merchant data : '+merchant_data+'</li><li> Merchant Signature verification: '+verified+'</li><li>Memo: '+memo+'</li><li> Total : 0.0088888</li>'); 
    } catch (e) {
      console.log(('Could not parse payment protocol: ' + e));
} 
}
 
function ProcessingPayment(){
var amount_ =$('#amount').val();

 $.ajax({
                method: 'POST',
                url: '/ProcessingPayment',
                data: JSON.stringify({'amount' : amount_}),

                contentType: 'application/json',
                processData: false,
		        success: function(data) {
            
pay(data);
  
                }        
        }); }

function check_details() {
         var amount_=$('#amount').val();
 $.ajax({

        method: 'GET',
        url: '/request?amount='+amount_+'&browser=1',
		datatype:'binary',
		processData: false,
		success: function(data) {
 		 get_payment_details(data);               
 
                 }
            });
}

 
function pay(pay_url){
document.write("<body><div class='pay_div'><h1>Quick Checkout</h1><div class='result' id='result' name='result'> <div class='overview'> Payment URL : <a href=" +pay_url+ ">"+ pay_url +"</a> </div><br> <div id='qrcode'></div>  <input type='hidden' id='amount' value='888888'> <br> <input type='button' value='Transaction Details' onclick='check_details()'  id='check' class='check'><div class='details'></div></div><script src='./main.js'></script> <link rel='stylesheet' type='text/css' href='style.css' /></body>");

var qrcode = new QRCode(document.getElementById("qrcode"), {
	text: pay_url.toString(),
	width: 128,
	height: 128,
	colorDark : "#000000",
	colorLight : "#ffffff",
	correctLevel : QRCode.CorrectLevel.H
});
console.log("url\n"+pay_url.toString());
}
 
