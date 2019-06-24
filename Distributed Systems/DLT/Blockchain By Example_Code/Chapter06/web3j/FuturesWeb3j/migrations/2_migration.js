var Futures = artifacts.require("CFutures");
 

module.exports = function(deployer) {

 deployer.deploy(Futures);/*.then(function() {

 return deployer.deploy(Futures,11, 7, 77,"0x8f9539c3f78cc24597d74978318d1e6ce7f18a1a","0x8f9539c3f78cc24597d74978318d1e6ce7f18a1a", 15248555,Fuelprice.address);
*/});
};
