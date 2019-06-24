var PacktToken = artifacts.require('./PacktToken.sol');
var PacktTokenSale = artifacts.require('./PacktTokenSale.sol');

module.exports = function (deployer) {
  deployer.deploy(PacktToken, 1000000).then(function () {
    return deployer.deploy(
      PacktTokenSale,
      PacktToken.address,
      1000000000000000);
  });
};
