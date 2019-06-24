loadScript('../ensutils-testnet.js');

var time = testRegistrar.expiryTimes(web3.sha3('packt'));
new Date(time.toNumber() * 1000);

ens.setResolver(
    namehash('packt.test'),
    publicResolver.address,
    { from: eth.accounts[0] }
);

publicResolver.setAddr(
    namehash('packt.test'),
    '0x111...', // Or any other address 
    { from: eth.accounts[0] }
);

var address = ens.resolver(namehash('packt.test'));
resolverContract.at(address).addr(namehash('packt.test'));

ethRegistrar.entries(web3.sha3('packt'))[0]