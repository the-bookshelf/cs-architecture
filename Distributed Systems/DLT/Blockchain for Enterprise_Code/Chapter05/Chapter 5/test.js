var generateSecret = function () {
    return Math.random().toString(36).substr(2, 9);
};

var web3 = new Web3(new Web3.providers.HttpProvider("http://localhost:8545"));

var RBI_Address = "0x92764a01c43ca175c0d2de145947d6387205c655";
var FRS_Address = "0xbc37e7ba9f099ba8c61532c6fce157072798fe77";
var BOA_Address = "0x104803ea6d8696afa6e7a284a46a1e71553fcf12";
var ICICI_Address = "0x84d2dab0d783dd84c40d04692e303b19fa49bf47";

var usdContract_ABI = /* Put JSON here */;
var usdContract_Bytecode = "0x606..."
var atomicswapUSD_ABI = /* Put JSON here */;
var atomicswapUSD_Bytecode = "0x606..."
var inrContract_ABI = /* Put JSON here */;
var inrContract_Bytecode = "0x606..."
var atomicswapINR_ABI = /* Put JSON here */;
var atomicswapINR_Bytecode = "0x606..."

var usdContract = web3.eth.contract(usdContract_ABI);
var usd = usdContract.new({
  from: FRS_Address, 
   data: usdContract_Bytecode, 
   gas: "4700000"
}, function (e, contract){
  if (typeof contract.address !== 'undefined') {
    var usdContractAddress = contract.address;
    var usdContractInstance = usdContract.at(usdContractAddress)
    var atomicswap_usdContract = web3.eth.contract(atomicswapUSD_ABI);
    var atomicswap_usd = atomicswap_usdContract.new(usdContractAddress, {
        from: FRS_Address, 
        data: atomicswapUSD_Bytecode, 
        gas: "4700000"
    }, function (e, contract){
        if (typeof contract.address !== 'undefined') {
            var atomicSwapUSDAddress = contract.address;
            var atomicSwapUSDContractInstance = atomicswap_usdContract.at(atomicSwapUSDAddress);

            var inrContract = web3.eth.contract(inrContract_ABI);
        
        var inr = inrContract.new({
            from: RBI_Address, 
            data: inrContract_Bytecode, 
            gas: "4700000"
        }, function (e, contract){
            if(typeof contract.address !== 'undefined') {
                var inrContractAddress = contract.address;
                var inrContractInstance = inrContract.at(inrContractAddress)
            var atomicswap_inrContract = web3.eth.contract(atomicswapINR_ABI);
            var atomicswap_inr = atomicswap_inrContract.new(
                inrContractAddress, {
                from: RBI_Address, 
                data: atomicswapINR_Bytecode, 
                gas: '4700000'
            }, function (e, contract){
                if (typeof contract.address !== 'undefined') {
                    var atomicSwapINRAddress = contract.address;
                    var atomicSwapINRContractInstance = atomicswap_inrContract.at(atomicSwapINRAddress);

                    //Issue USD
                    usdContractInstance.issueUSD.sendTransaction(BOA_Address, 1000, {from: FRS_Address}, function(e, txnHash){

                      //Fetch USD Balance
                      console.log("Bank of America's USD Balance is : " + usdContractInstance.getUSDBalance.call(BOA_Address).toString())
                      
                      //Issue INR
                      inrContractInstance.issueINR.sendTransaction(ICICI_Address, 1000, {from: RBI_Address}, function(e, txnHash){

                        //Fetch INR Balance
                        console.log("ICICI Bank's INR Balance is : " + inrContractInstance.getINRBalance.call(ICICI_Address).toString())

                        //Generate Secret and Hash
                        var secret = generateSecret();
                        var hash = atomicSwapUSDContractInstance.calculateHash.call(secret, {from: BOA_Address});

                        //Give Access to Smart Contract
                        usdContractInstance.approve.sendTransaction(atomicSwapUSDAddress, 1000, {from: BOA_Address}, function(e, txnHash){

                          //Give Access to Smart Contract
                          inrContractInstance.approve.sendTransaction(atomicSwapINRAddress, 1000, {from: ICICI_Address}, function(e, txnHash){

                            //Lock 1000 USD for 30 min
                            atomicSwapUSDContractInstance.lock.sendTransaction(ICICI_Address, hash, 30, 1000, {from: BOA_Address, gas: 4712388}, function(e, txnHash){

                              //Fetch USD Balance
                              console.log("USD Atomic Exchange Smart Contracts holds : " + usdContractInstance.getUSDBalance.call(atomicSwapUSDAddress).toString())

                              //Lock 1000 INR for 15 min
                              atomicSwapINRContractInstance.lock.sendTransaction(BOA_Address, hash, 15, 1000, {from: ICICI_Address, gas: 4712388}, function(e, txnHash){

                                //Fetch INR Balance
                                console.log("INR Atomic Exchange Smart Contracts holds : " + inrContractInstance.getINRBalance.call(atomicSwapINRAddress).toString())

                                atomicSwapINRContractInstance.claim(secret, {
                                  from: BOA_Address, gas: 4712388
                                }, function(error, txnHash){
                                  
                                  //Fetch INR Balance
                                  console.log("Bank of America's INR Balance is : " + inrContractInstance.getINRBalance.call(BOA_Address).toString())

                                  atomicSwapUSDContractInstance.claim(secret, {
                                    from: ICICI_Address, gas: 4712388
                                  }, function(error, txnHash){
                                    
                                    //Fetch USD Balance
                                    console.log("ICICI Bank's USD Balance is : " + usdContractInstance.getUSDBalance.call(ICICI_Address).toString())
                                  })

                                })
                                
                              })
                            })
                          })
                          
                        })
                      })
                      
                    }) 
                }
            })
            }
        })
        }
    })
  }
})