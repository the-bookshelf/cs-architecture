package org.chapter6.futures;



import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Scanner;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.chapter6.futures.CFutures;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

 
 
public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_700_000);
    private static final BigInteger CUSTOM_GAS_PRICE = Convert.toWei("140", Convert.Unit.GWEI).toBigInteger();
	private Scanner input;

    public static void main(String[] args) throws Exception {
        new App().run();
    }
 
    private static long ContractDuration(int days) {
    	
    	Calendar cal = Calendar.getInstance();
    	long timestamp = cal.getTimeInMillis()+(days*24*3600);
        log.info("contract expires at :" +timestamp);

    	return timestamp;
    }
    
    
    private static CFutures deployFutures(Web3j web3j,Credentials credentials,int assetID_, BigInteger  Quantity,  BigInteger price, String buyer,  String seller,  long date_) throws Exception {
    	


        BigInteger assetID=BigInteger.valueOf(assetID_);
        BigInteger date=BigInteger.valueOf(date_);     

        CFutures CFuture_ = CFutures.deploy(web3j, credentials,  CUSTOM_GAS_PRICE,  GAS_LIMIT,  assetID,  Quantity,  price, buyer,  seller,  date/*,contract1.getContractAddress()*/).send();
   	 	String CFuturesAddress=CFuture_.getContractAddress();
 
   	 
        log.info("View contract at https://ropsten.etherscan.io/address/" + CFuturesAddress);

        return CFuture_;
    }
    
    private boolean send(Web3j web3j, Credentials credentials,String destination, int Weivalue) throws Exception{
    	
        TransactionReceipt transferReceipt = Transfer.sendFunds(web3j, credentials,destination, BigDecimal.valueOf(Weivalue), Convert.Unit.ETHER).sendAsync().get();
        log.info("Transaction complete : " + transferReceipt.getTransactionHash());
        return true;
    }
	
 
    private void run() throws Exception {
    	
    	
  
      	Credentials credentials =
        WalletUtils.loadCredentials("your password","src/yourwalletfile.json");
    	
        log.info(credentials.getAddress()+" Credentials loaded");
      
        Web3j web3j = Web3j.build(new HttpService("https://ropsten.infura.io"));
        
        EthGetBalance AccountBalance = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
        log.info("The accounts balance "+AccountBalance.getBalance()+" wei");    
        
        BigInteger Quantity=BigInteger.valueOf(120000);
        BigInteger Price=BigInteger.valueOf(300);

        


        input = new Scanner(System.in);
        CFutures CFutureInstance = null;
        Scanner sc2 = new Scanner(System.in);
        String loadedCAddress;
        	
        System.out.println("Please make your choice:");
        System.out.println("1 – Deploy New Cfutures contract.");
        System.out.println("2 – Load contract");
        System.out.println("3 – Make deposit");
        System.out.println("4 – current Fuel price");
        System.out.println("5 – Update Fuel price");
        System.out.println("6 – Investment offset");
        System.out.println("0 - exit");
        
            int selection;
            
            choice: while(input.hasNextInt()){
            	selection = input.nextInt();
        	  switch (selection) {
              case 0:
                  break choice; 
              case 1:   	 	 	
            	  deployFutures(web3j,credentials,1001, Quantity, Price, "0x97dd3033c7f979a623f32c0a6818bef92b248bc0", "0x97dd3033c7f979a623f32c0a6818bef92b248bc0",ContractDuration(10));
        	  // you can read the seller and buyer addresses as inputs instead using fixed values
            	  break ;
      
              case 2:
                  System.out.println("Please provide the contract's address to load:");
                  loadedCAddress= sc2.nextLine();
            	  CFutureInstance = CFutures.load(loadedCAddress, web3j, credentials, CUSTOM_GAS_PRICE, GAS_LIMIT);
  	              log.info("Contract loaded: " + CFutureInstance.getContractAddress());

            	  break ;
              case 3:
	    	     TransactionReceipt DepositReceipt=CFutureInstance.deposit(Price.multiply(Quantity)).send();
	    	     log.info("Transaction complete, view it at https://ropsten.etherscan.io/tx/" + DepositReceipt.getTransactionHash());
	    	     for (CFutures.DepositEvEventResponse event : CFutureInstance.getDepositEvEvents(DepositReceipt)) {
	
	    	            log.info("Deposit event detected:" + event.amount+" wei has ben deposited");
	
	    	            log.info("The funds have been sent by: " + event.sender);
	    	            
	    	        }
	    	     break;
              case 4:
		          BigInteger  LastfuelPrice=CFutureInstance.fuelPriceUSD().send();
		          Integer fuelPriceUSD = LastfuelPrice.intValue();
		          log.info("Last fuel Price Fuel price According to the Oracle is: " + fuelPriceUSD);
		          break;
    	  
              case 5:
		    	  BigInteger txcost = Convert.toWei("0.01", Convert.Unit.ETHER).toBigInteger();
		    	  TransactionReceipt UpdateReceipt=CFutureInstance.updateprice(txcost).send();
		    	  for (CFutures.NewDieselPriceEventResponse event : CFutureInstance.getNewDieselPriceEvents(UpdateReceipt)) {
		
		    		  log.info("The oil price has been updated:" + event.price);
		    	  }
		    	  break;
		    	  
    		  case 6:
    			  BigInteger  Offset=CFutureInstance.offset().send();
    			  log.info("your offset is "+Offset.intValue());
          		break;
    	      default:
    	            System.out.println("Please make your choice");
        	  }   

            }
  
       /* EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, CFutureInstance.getContractAddress().substring(2));
        web3j.ethLogObservable(filter).subscribe(Clog -> {
                  
        	log.info("contract log"+ Clog.toString());
          
        });*/
       
       
    }
}
