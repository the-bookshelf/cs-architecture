var PacktToken = artifacts.require('./PacktToken');

contract('PacktToken', async (accounts) => {

  it ('initialises the contract with the correct values', async () => {
    let instance = await PacktToken.deployed();

    let name = await instance.name();
    assert.equal(name, 'Packt ERC20 Token', 'has the correct name');

    let symbol = await instance.symbol();
    assert.equal(symbol, 'PET', 'has the correct symbol');
  });

  it ('allocates the total supply on deployment', async () => {
    let instance = await PacktToken.deployed();

    let supply = await instance.totalSupply();
    assert.equal(supply, 1000000, 'sets the correct total supply');

    let balance = await instance.balanceOf(accounts[0]);
    assert.equal(balance, 1000000, 'allocates the initial balance to the owner');
  });

  // it ('transfers tokens', async () => {
  //   let instance = await PacktToken.deployed();
  //
  // });

});
