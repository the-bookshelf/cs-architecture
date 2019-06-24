var PacktToken = artifacts.require('./PacktToken');
var PacktTokenSale = artifacts.require('./PacktTokenSale');

contract('PacktTokenSale', async (accounts) => {
  let owner = accounts[0];
  let buyer = accounts[1];
  let tokensToSell = 500000;
  let tokenPrice = 1000000000000000;
  let numberOfTokens;

  it ('initialises the contract with the correct values', async () => {
    let tokenSaleInstance = await PacktTokenSale.deployed();

    let tokenSaleAddress = await tokenSaleInstance.address;
    assert.notEqual(tokenSaleAddress, 0x0, 'has an address');

    let tokenAddress = await tokenSaleInstance.tokenContract();
    assert.notEqual(tokenAddress, 0x0, 'has an address');

    let tokenPrice = await tokenSaleInstance.tokenPrice();
    assert.equal(tokenPrice,
                 1000000000000000,
                 'sets the correct token price');
  });

  it ('allows users to buy tokens', async () => {
    let tokenSaleInstance = await PacktTokenSale.deployed();
    let tokenInstance = await PacktToken.deployed();

    // Transfer half of the tokens to the sale contract.
    let success = await tokenInstance.transfer(
      tokenSaleInstance.address, tokensToSell, { from: owner });

    numberOfTokens = 50;
    let receipt = await tokenSaleInstance.buyTokens(
      numberOfTokens, { from: buyer, value: numberOfTokens * tokenPrice });

    let buyerBalance = await tokenInstance.balanceOf(buyer);
    assert.equal(buyerBalance, numberOfTokens);

    let contractBalance = await tokenInstance.balanceOf(
      tokenSaleInstance.address);
    assert.equal(contractBalance, tokensToSell - numberOfTokens);
  });
});
