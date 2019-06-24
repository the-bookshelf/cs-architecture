package org.chapter6.BitcoinJClient;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.protocols.payments.PaymentProtocol;
import org.bitcoinj.protocols.payments.PaymentSession;
import org.bitcoinj.uri.BitcoinURI;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.ListenableFuture;

public class Cbip70 {
 

 final static NetworkParameters params = TestNet3Params.get();
 private static org.bitcoin.protocols.payments.Protos.PaymentRequest paymentRequest;
 private static final Logger log = LoggerFactory.getLogger(Cbip70.class);


public static void main( String[] args ) throws InsufficientMoneyException
{
	BriefLogFormatter.init(); //logging verbosity BriefLogFormatter.initVerbose();
	WalletAppKit kit = new WalletAppKit(params, new File("."), "walletappkit3"); // create and load a spv wallet

	kit.startAsync();
	kit.awaitRunning();
	Address CustomerAddress=kit.wallet().currentReceiveAddress();
	log.info("Client's address : " + CustomerAddress);
	log.info("Client's Balance : "+kit.wallet().getBalance());


	//String url ="bitcoin:mhc5YipxN6GhRRXtgakRBjrNUCbz6ypg66?r=http://bip70.com:3000/request?amount=998888";
	String url="bitcoin:mhc5YipxN6GhRRXtgakRBjrNUCbz6ypg66?amount=0.10888888&message=payment%20request&r=http://bip70.com:3000/request?amount=10888888";
	
	if(Float.parseFloat(String.valueOf(kit.wallet().getBalance()))==0.0)
		log.warn("Please send some testnet Bitcoins to your address "+kit.wallet().currentReceiveAddress());

	else sendPaymentRequest(url, kit); 

  //listing pending transactions
	 Collection<Transaction> pendingTransactions = kit.wallet().getPendingTransactions();
     int messagePending=0;
     for (Transaction tx : pendingTransactions) {
    	 System.out.println("pending Transactions : \n"+tx);
    	SendRequest request = SendRequest.forTx(tx);
    	request.feePerKb = Coin.valueOf(3000);// request.feePerKb = Transaction.REFERENCE_DEFAULT_MIN_TX_FEE; // Or you can make other highter fee to spped it up

    	  /*kit.wallet().completeTx(request);
    	  kit.wallet().commitTx(request.tx);
    	  Wallet.SendResult sendResult = new Wallet.SendResult();
    	  sendResult.tx = request.tx;
    	  sendResult.broadcast = kit.peerGroup().broadcastTransaction(request.tx);
    	  sendResult.broadcastComplete = sendResult.broadcast.future();*/
    	  
    	   /* Updates the wallet with the given transaction: puts it into the pending pool, sets the spent flags and runs the onCoinsSent/onCoinsReceived event listener. Used in two situations:
When we have just successfully transmitted the tx we created to the network.
When we receive a pending transaction that didn't appear in the chain yet, and we did not create it.
    	   */
    	
    	  //
/* */

         }

   
	/* log.info("Stopping The node...");
    kit.stopAsync();
    kit.awaitTerminated();*/
}

private static void sendPaymentRequest(String location, WalletAppKit k) {

try {
	
if (location.startsWith("http") ||location.startsWith("bitcoin")) {
 

BitcoinURI paymentRequestURI = new BitcoinURI(location);

ListenableFuture<PaymentSession> future = PaymentSession.createFromBitcoinUri(paymentRequestURI,true);
 

PaymentSession session = future.get();

if (session.isExpired())
	log.warn("request expired!");

else {
send(session, k);
//System.exit(1);

}
 

} else { 
	//Try to open the payment request as a file.
log.info("Try to open the payment request as a file");
} 
}
catch (Exception e) {
System.err.println( e.getMessage());

}
 
}



private static void send(PaymentSession session,WalletAppKit k) {

try {

	/*log.info("Payment Request");
	log.info("Amount to Pay: " + session.getValue().toFriendlyString());
	log.info("Date: " + session.getDate());
	log.info("Message from merchant: " + session.getMemo());
	PaymentProtocol.PkiVerificationData identity = session.verifyPki();

if (identity != null) {

	log.info("Payment requester: " + identity.displayName);
	log.info("Certificate authority: " + identity.rootAuthorityName);

}

else{

System.out.println("PKI not Verified");

}*/

final SendRequest request = session.getSendRequest();
request.feePerKb = Coin.valueOf(3000); //3000 sztoshi instead 1000

k.wallet().completeTx(request);
//request.feePerKb = Transaction.REFERENCE_DEFAULT_MIN_TX_FEE;
String customerMemo = "Nice Website";
Address refundAddress = new Address(params,"mfcjN5E6vp2NWpMvH7TM2xvTywzRtNvZWR");
ListenableFuture<PaymentProtocol.Ack> future = session.sendPayment(ImmutableList.of(request.tx),refundAddress, customerMemo); // sendPayment does NOT broadcast the transactions to the bitcoin network
System.out.println("88 from merchant :"+request.tx.toString());

if (future != null) {

PaymentProtocol.Ack ack = future.get();
/*k.wallet().commitTx(request.tx);
*/System.out.println("Memo from merchant :"+ack.getMemo());

	log.info("Stopping The node...");
   // k.stopAsync();
   // k.awaitTerminated();
} /*else {
// if bitcoin URI doesn't contain a payment url, we broadcast directly the list of signed transactions.
Wallet.SendResult sendResult = new Wallet.SendResult();
sendResult.tx = request.tx;
sendResult.broadcast = k.peerGroup().broadcastTransaction(request.tx);
sendResult.broadcastComplete = sendResult.broadcast.future();

}*/

} catch (Exception e) {

System.err.println("Failed to send payment " + e.getMessage());
//System.exit(1);

} 





}
}



