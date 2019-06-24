pragma solidity ^0.4.24;


import "./oraclizeAPI_0.4.sol";

 


contract CFutures  is usingOraclize {
    

    address user;
    event DepositEv(uint amount, address sender);
    address hedger;
    address speculator;
 
 
    struct asset{
        uint id;
        uint Quantity;
        uint price;
        address seller;
        address buyer;
        uint date;
        
    }
 
asset TradedAsset;
    uint public fuelPriceUSD;

    event NewOraclizeQuery(string description);
    event NewDieselPrice(string price);

   
    function __callback(bytes32 myid, string result) public {
        require(msg.sender == oraclize_cbAddress());
        NewDieselPrice(result);
        fuelPriceUSD = parseInt(result, 2);  
    }

    function updateprice() public payable {
        NewOraclizeQuery("Oraclize query was sent, standing by for the answer..");
        oraclize_query("URL", "xml(https://www.fueleconomy.gov/ws/rest/fuelprices).fuelPrices.diesel");
    }
    
    function CFutures(uint assetID, uint Quantity, uint price,address buyer, address seller, uint date) public{
 
        TradedAsset.id=assetID;
        TradedAsset.Quantity=Quantity;
        TradedAsset.price=price;
        TradedAsset.seller=seller;
	speculator=seller;
	hedger=buyer;
        TradedAsset.buyer=buyer;
        TradedAsset.date=date;
 
         updateprice();   
 

    }
    
 function deposit() public payable returns(bool){
     require(msg.value==TradedAsset.Quantity*TradedAsset.price);
     DepositEv(msg.value,msg.sender);
     return true;
 }

    modifier onlyhedger(){
        require(msg.sender==hedger);
         _;
    }

    function sellcontract(address newhedger)public onlyhedger returns(bool){
        hedger=newhedger;
        return true;
    }
    
    function getpaid() public returns(bool){
        require(now >= TradedAsset.date && address(this).balance>0);
        speculator.transfer(address(this).balance);
        return true;
    }
    
    function offset() public view returns (uint){
     
    return TradedAsset.Quantity*(fuelPriceUSD-TradedAsset.price);
    }
     

  
 
}
