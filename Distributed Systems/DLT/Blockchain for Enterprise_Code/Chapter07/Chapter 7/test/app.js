let Web3 = require("web3");
let ethereumjsWallet = require("ethereumjs-wallet")
let ethereumjsUtil = require("ethereumjs-util");
let ethereumjsTx = require("ethereumjs-tx");
let sha256 = require("sha256");
let EthCrypto = require('eth-crypto');
let exec = require("child_process").exec;

let web3 = new Web3(new Web3.providers.HttpProvider("http://localhost:8545"));

let healthContract = web3.eth.contract([{"constant":false,"inputs":[{"name":"token","type":"string"},{"name":"patient","type":"address"}],"name":"requestAccess","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"patient","type":"address"},{"name":"hash","type":"bytes32"}],"name":"getToken","outputs":[{"name":"status","type":"int256"},{"name":"read","type":"bool"},{"name":"write","type":"bool"},{"name":"reEncKey","type":"string"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"serviceProvider","type":"address"}],"name":"revokeServiceProviderAccess","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"publicKey","type":"string"}],"name":"addServiceProvider","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"patient","type":"address"},{"name":"hash","type":"string"}],"name":"addEMR","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"hash","type":"bytes32"},{"name":"read","type":"bool"},{"name":"write","type":"bool"},{"name":"reEncKey","type":"string"}],"name":"addToken","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"patient","type":"address"}],"name":"isPatientProfileClosed","outputs":[{"name":"isClosed","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"serviceProvider","type":"address"}],"name":"getServiceProviderPublicKey","outputs":[{"name":"publicKey","type":"string"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"reEncKey","type":"string"},{"name":"newAddress","type":"address"},{"name":"newPublicKey","type":"string"}],"name":"changePatientAccount","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"patient","type":"address"}],"name":"getPatientPublicKey","outputs":[{"name":"publicKey","type":"string"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"s","type":"string"}],"name":"fromHex","outputs":[{"name":"","type":"bytes"}],"payable":false,"stateMutability":"pure","type":"function"},{"constant":false,"inputs":[{"name":"patient","type":"address"},{"name":"serviceProvider","type":"address"}],"name":"getPermission","outputs":[{"name":"read","type":"bool"},{"name":"write","type":"bool"},{"name":"reEncKey","type":"string"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"c","type":"uint256"}],"name":"fromHexChar","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"pure","type":"function"},{"constant":false,"inputs":[{"name":"publicKey","type":"string"}],"name":"addPatient","outputs":[{"name":"reason","type":"int256"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"inputs":[],"payable":false,"stateMutability":"nonpayable","type":"constructor"},{"anonymous":false,"inputs":[{"indexed":false,"name":"hash","type":"bytes32"},{"indexed":false,"name":"patient","type":"address"},{"indexed":false,"name":"serviceProvider","type":"address"}],"name":"tokenVerified","type":"event"},{"anonymous":false,"inputs":[{"indexed":false,"name":"patient","type":"address"},{"indexed":false,"name":"serviceProvider","type":"address"}],"name":"reEncKeyAdded","type":"event"},{"anonymous":false,"inputs":[{"indexed":false,"name":"oldAccountAddress","type":"address"},{"indexed":false,"name":"oldAccountPublicKey","type":"string"},{"indexed":false,"name":"newAccountAddress","type":"address"},{"indexed":false,"name":"newAccountPublicKey","type":"string"},{"indexed":false,"name":"reEncKey","type":"string"}],"name":"patientAccountChanged","type":"event"},{"anonymous":false,"inputs":[{"indexed":false,"name":"patient","type":"address"},{"indexed":false,"name":"serviceProvider","type":"address"},{"indexed":false,"name":"emrHash","type":"string"}],"name":"emrAdded","type":"event"}]);
let health = healthContract.new({
  from: web3.eth.accounts[0],
  data: '0x608060405234801561001057600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555061295d806100606000396000f3006080604052600436106100d0576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806317b9df44146100d55780632dc779b91461015e5780633bb9fc041461024557806348b34ff21461028857806349f9c88c146102f157806356dfdf6f1461037a5780636a8cd0ad146104095780636c8914ec14610464578063700435fc146105205780637d217ec4146105ef5780638e7e34d7146106ab578063910185dd1461078d578063b73127071461087f578063fb87cf94146108c0575b600080fd5b3480156100e157600080fd5b5061015c600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061093d565b005b34801561016a57600080fd5b506101ad600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035600019169060200190929190505050610e64565b60405180858152602001841515151581526020018315151515815260200180602001828103825283818151815260200191508051906020019080838360005b838110156102075780820151818401526020810190506101ec565b50505050905090810190601f1680156102345780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b34801561025157600080fd5b50610286600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506110ad565b005b34801561029457600080fd5b506102ef600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506111e6565b005b3480156102fd57600080fd5b50610378600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506112e3565b005b34801561038657600080fd5b506104076004803603810190808035600019169060200190929190803515159060200190929190803515159060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050611587565b005b34801561041557600080fd5b5061044a600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050611817565b604051808215151515815260200191505060405180910390f35b34801561047057600080fd5b506104a5600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050611870565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156104e55780820151818401526020810190506104ca565b50505050905090810190601f1680156105125780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561052c57600080fd5b506105ed600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050611954565b005b3480156105fb57600080fd5b50610630600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050611cea565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610670578082015181840152602081019050610655565b50505050905090810190601f16801561069d5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156106b757600080fd5b50610712600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050611dce565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610752578082015181840152602081019050610737565b50505050905090810190601f16801561077f5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561079957600080fd5b506107ee600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050611fdc565b60405180841515151581526020018315151515815260200180602001828103825283818151815260200191508051906020019080838360005b83811015610842578082015181840152602081019050610827565b50505050905090810190601f16801561086f5780820380516001836020036101000a031916815260200191505b5094505050505060405180910390f35b34801561088b57600080fd5b506108aa6004803603810190808035906020019092919050505061222c565b6040518082815260200191505060405180910390f35b3480156108cc57600080fd5b50610927600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050612684565b6040518082815260200191505060405180910390f35b60006002836040518082805190602001908083835b6020831015156109775780518252602082019150602081019050602083039250610952565b6001836020036101000a0380198251168184511680821785525050505050509050019150506020604051808303816000865af11580156109bb573d6000803e3d6000fd5b5050506040513d60208110156109d057600080fd5b810190808051906020019092919050505090506001600260008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060020160008360001916600019168152602001908152602001600020600001541415610e5f5760028060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206002016000836000191660001916815260200190815260200160002060000181905550600260008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206002016000826000191660001916815260200190815260200160002060010160009054906101000a900460ff16600260008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160006101000a81548160ff021916908315150217905550600260008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206002016000826000191660001916815260200190815260200160002060010160019054906101000a900460ff16600260008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160016101000a81548160ff021916908315150217905550600260008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060020160008260001916600019168152602001908152602001600020600201600260008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206001019080546001816001161561010002031660029004610db6929190612785565b507f51d2d311dd5095a79cc54726a2734c8347533992552e5bee642c802c94f77e888183336040518084600019166000191681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001935050505060405180910390a15b505050565b60008060006060600260008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206002016000866000191660001916815260200190815260200160002060000154600260008873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206002016000876000191660001916815260200190815260200160002060010160009054906101000a900460ff16600260008973ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206002016000886000191660001916815260200190815260200160002060010160019054906101000a900460ff16600260008a73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060020160008960001916600019168152602001908152602001600020600201808054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156110955780601f1061106a57610100808354040283529160200191611095565b820191906000526020600020905b81548152906001019060200180831161107857829003601f168201915b50505050509050935093509350935092959194509250565b6000600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160006101000a81548160ff0219169083151502179055506000600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160016101000a81548160ff02191690831515021790555050565b3373ffffffffffffffffffffffffffffffffffffffff1661120682611dce565b6040518082805190602001908083835b60208310151561123b5780518252602082019150602081019050602083039250611216565b6001836020036101000a03801982511681845116808217855250505050505090500191505060405180910390206001900473ffffffffffffffffffffffffffffffffffffffff1614156112e05780600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000190805190602001906112de92919061280c565b505b50565b60011515600260008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160019054906101000a900460ff161515141561158357600260008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060040160408051908101604052808381526020013373ffffffffffffffffffffffffffffffffffffffff1681525090806001815401808255809150509060018203906000526020600020906002020160009091929091909150600082015181600001908051906020019061143392919061288c565b5060208201518160010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505050507f6bbd72ef456cc2bbf275e13615b562e6e5e9093316749cd74249d4f32d75eee1823383604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200180602001828103825283818151815260200191508051906020019080838360005b8381101561154657808201518184015260208101905061152b565b50505050905090810190601f1680156115735780820380516001836020036101000a031916815260200191505b5094505050505060405180910390a15b5050565b6000600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206002016000866000191660001916815260200190815260200160002060000154148015611648575060001515600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060030160009054906101000a900460ff161515145b15611811576001600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600201600086600019166000191681526020019081526020016000206000018190555082600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206002016000866000191660001916815260200190815260200160002060010160006101000a81548160ff02191690831515021790555081600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206002016000866000191660001916815260200190815260200160002060010160016101000a81548160ff02191690831515021790555080600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060020160008660001916600019168152602001908152602001600020600201908051906020019061180f92919061280c565b505b50505050565b6000600260008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060030160009054906101000a900460ff169050919050565b6060600160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156119485780601f1061191d57610100808354040283529160200191611948565b820191906000526020600020905b81548152906001019060200180831161192b57829003601f168201915b50505050509050919050565b6001600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060030160006101000a81548160ff0219169083151502179055508173ffffffffffffffffffffffffffffffffffffffff166119cf82611dce565b6040518082805190602001908083835b602083101515611a0457805182526020820191506020810190506020830392506119df565b6001836020036101000a03801982511681845116808217855250505050505090500191505060405180910390206001900473ffffffffffffffffffffffffffffffffffffffff161415611ce55780600260008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000019080519060200190611aa792919061280c565b507fc88aeb99efd4bc071b8e9cc5550a3d22e49f76ca4fd22a55a254b78313cd720433600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600001848487604051808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001806020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018060200180602001848103845288818154600181600116156101000203166002900481526020019150805460018160011615610100020316600290048015611c045780601f10611bd957610100808354040283529160200191611c04565b820191906000526020600020905b815481529060010190602001808311611be757829003601f168201915b5050848103835286818151815260200191508051906020019080838360005b83811015611c3e578082015181840152602081019050611c23565b50505050905090810190601f168015611c6b5780820380516001836020036101000a031916815260200191505b50848103825285818151815260200191508051906020019080838360005b83811015611ca4578082015181840152602081019050611c89565b50505050905090810190601f168015611cd15780820380516001836020036101000a031916815260200191505b509850505050505050505060405180910390a15b505050565b6060600260008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015611dc25780601f10611d9757610100808354040283529160200191611dc2565b820191906000526020600020905b815481529060010190602001808311611da557829003601f168201915b50505050509050919050565b60608060606000849250600060028451811515611de757fe5b06141515611df457600080fd5b60028351811515611e0157fe5b046040519080825280601f01601f191660200182016040528015611e345781602001602082028038833980820191505090505b509150600090505b60028351811515611e4957fe5b04811015611fd157611edb8360018360020201815181101515611e6857fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000027f0100000000000000000000000000000000000000000000000000000000000000900461222c565b6010611f648584600202815181101515611ef157fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000027f0100000000000000000000000000000000000000000000000000000000000000900461222c565b02017f0100000000000000000000000000000000000000000000000000000000000000028282815181101515611f9657fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a905350806001019050611e3c565b819350505050919050565b6000806060600260008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff16600260008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010160008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff16600260008873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010160008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600101808054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156122185780601f106121ed57610100808354040283529160200191612218565b820191906000526020600020905b8154815290600101906020018083116121fb57829003601f168201915b505050505090509250925092509250925092565b60007f30000000000000000000000000000000000000000000000000000000000000007effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827f0100000000000000000000000000000000000000000000000000000000000000027effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff19161015801561234a57507f39000000000000000000000000000000000000000000000000000000000000007effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827f0100000000000000000000000000000000000000000000000000000000000000027effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff191611155b1561239c577f30000000000000000000000000000000000000000000000000000000000000007f010000000000000000000000000000000000000000000000000000000000000090048203905061267f565b7f61000000000000000000000000000000000000000000000000000000000000007effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827f0100000000000000000000000000000000000000000000000000000000000000027effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916101580156124b857507f66000000000000000000000000000000000000000000000000000000000000007effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827f0100000000000000000000000000000000000000000000000000000000000000027effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff191611155b1561250d577f61000000000000000000000000000000000000000000000000000000000000007f0100000000000000000000000000000000000000000000000000000000000000900482600a0103905061267f565b7f41000000000000000000000000000000000000000000000000000000000000007effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827f0100000000000000000000000000000000000000000000000000000000000000027effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff19161015801561262957507f46000000000000000000000000000000000000000000000000000000000000007effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827f0100000000000000000000000000000000000000000000000000000000000000027effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff191611155b1561267e577f41000000000000000000000000000000000000000000000000000000000000007f0100000000000000000000000000000000000000000000000000000000000000900482600a0103905061267f565b5b919050565b60003373ffffffffffffffffffffffffffffffffffffffff166126a683611dce565b6040518082805190602001908083835b6020831015156126db57805182526020820191506020810190506020830392506126b6565b6001836020036101000a03801982511681845116808217855250505050505090500191505060405180910390206001900473ffffffffffffffffffffffffffffffffffffffff1614156127805781600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600001908051906020019061277e92919061280c565b505b919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106127be57805485556127fb565b828001600101855582156127fb57600052602060002091601f016020900482015b828111156127fa5782548255916001019190600101906127df565b5b509050612808919061290c565b5090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061284d57805160ff191683800117855561287b565b8280016001018555821561287b579182015b8281111561287a57825182559160200191906001019061285f565b5b509050612888919061290c565b5090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106128cd57805160ff19168380011785556128fb565b828001600101855582156128fb579182015b828111156128fa5782518255916020019190600101906128df565b5b509050612908919061290c565b5090565b61292e91905b8082111561292a576000816000905550600101612912565b5090565b905600a165627a7a72305820f37e06769f75e231cb078e48ba4bceb81141d67bf752628cc6c6b0b349da18c50029',
  gas: '4700000'
}, function(e, contract) {
  if (typeof contract.address !== 'undefined') {
    let healthContractInstance = healthContract.at(contract.address);

    //Generate Patient's Keys
    let patient_wallet = ethereumjsWallet.generate();

    //Register the Patient on blockchain.
    let data = healthContractInstance.addPatient.getData(patient_wallet.getPublicKey().toString('hex'));
    let nonce = web3.eth.getTransactionCount(patient_wallet.getAddressString())

    let rawTx = {
      gasPrice: web3.toHex(web3.eth.gasPrice),
      gasLimit: web3.toHex(4700000),
      from: patient_wallet.getAddressString(),
      nonce: web3.toHex(nonce),
      data: data,
      to: contract.address
    };

    let privateKey = ethereumjsUtil.toBuffer("0x" + patient_wallet.getPrivateKey().toString('hex'), 'hex');
    let tx = new ethereumjsTx(rawTx);
    tx.sign(privateKey);

    web3.eth.sendRawTransaction("0x" + tx.serialize().toString('hex'), function(error, result) {
      if (error) {
        console.log(error)
        res.status(500).send({
          error: "An error occured"
        })
      } else {
        console.log("Patient Pub Key: " + healthContractInstance.getPatientPublicKey.call(patient_wallet.getAddressString()))

        //Generate Service Provider's Keys
        let hospital_wallet = ethereumjsWallet.generate();

        //Register the Service Provider on blockchain
        let data = healthContractInstance.addServiceProvider.getData(hospital_wallet.getPublicKey().toString('hex'));
        let nonce = web3.eth.getTransactionCount(hospital_wallet.getAddressString())

        let rawTx = {
          gasPrice: web3.toHex(web3.eth.gasPrice),
          gasLimit: web3.toHex(4700000),
          from: hospital_wallet.getAddressString(),
          nonce: web3.toHex(nonce),
          data: data,
          to: contract.address
        };

        let privateKey = ethereumjsUtil.toBuffer("0x" + hospital_wallet.getPrivateKey().toString('hex'), 'hex');
        let tx = new ethereumjsTx(rawTx);
        tx.sign(privateKey);

        web3.eth.sendRawTransaction("0x" + tx.serialize().toString('hex'), function(error, result) {
          if (error) {
            console.log(error)
          } else {
            console.log("Hospital Pub Key: " + healthContractInstance.getServiceProviderPublicKey.call(hospital_wallet.getAddressString()))

            let token = "yr238932";
            let tokenHash = "0x" + sha256(token);

            //Generate private key like npre. It has a extra character 0x00 in beginning
            let secKeyA = Buffer.concat([new Buffer([0x00]), patient_wallet.getPrivateKey()]).toString('base64')
            //Generate another private key to share with service provider
            let temp_wallet = ethereumjsWallet.generate();
            let secKeyB = Buffer.concat([new Buffer([0x00]), temp_wallet.getPrivateKey()]).toString('base64')

            exec('python3 ./generate_reEncKey.py ' + secKeyA + " " + secKeyB, (error, stdout, stderr) => {
              if (error !== null) {
                console.log(error)
              } else {
                let reEncKey = stdout.substr(2).slice(0, -2)

                console.log("Re-Encryption Key: " + reEncKey)

                //Add token to blockchain
                let data = healthContractInstance.addToken.getData(tokenHash, true, true, reEncKey);
                let nonce = web3.eth.getTransactionCount(patient_wallet.getAddressString())

                let rawTx = {
                  gasPrice: web3.toHex(web3.eth.gasPrice),
                  gasLimit: web3.toHex(4700000),
                  from: patient_wallet.getAddressString(),
                  nonce: web3.toHex(nonce),
                  data: data,
                  to: contract.address
                };

                let privateKey = ethereumjsUtil.toBuffer("0x" + patient_wallet.getPrivateKey().toString('hex'), 'hex');
                let tx = new ethereumjsTx(rawTx);
                tx.sign(privateKey);

                web3.eth.sendRawTransaction("0x" + tx.serialize().toString('hex'), function(error, result) {
                  if (error) {
                    console.log(error)
                  } else {
                    console.log("Token Info: " + healthContractInstance.getToken.call(patient_wallet.getAddressString(), tokenHash, {
                      from: patient_wallet.getAddressString()
                    }))

                    //Get access to patient's data
                    let data = healthContractInstance.requestAccess.getData(token, patient_wallet.getAddressString());
                    let nonce = web3.eth.getTransactionCount(hospital_wallet.getAddressString())

                    let rawTx = {
                      gasPrice: web3.toHex(web3.eth.gasPrice),
                      gasLimit: web3.toHex(4700000),
                      from: hospital_wallet.getAddressString(),
                      nonce: web3.toHex(nonce),
                      data: data,
                      to: contract.address
                    };

                    let privateKey = ethereumjsUtil.toBuffer("0x" + hospital_wallet.getPrivateKey().toString('hex'), 'hex');
                    let tx = new ethereumjsTx(rawTx);
                    tx.sign(privateKey);

                    web3.eth.sendRawTransaction("0x" + tx.serialize().toString('hex'), function(error, result) {
                      if (error) {
                        console.log(error)
                      } else {
                        console.log("Permission Info: " + healthContractInstance.getPermission.call(patient_wallet.getAddressString(), hospital_wallet.getAddressString(), {
                          from: hospital_wallet.getAddressString()
                        }))

                        let emr = JSON.stringify({
                          "Blood Group": "O+",
                          "type": "Blood Report"
                        });
                        let emrHash = sha256(emr);

                        let data = healthContractInstance.addEMR.getData(patient_wallet.getAddressString(), emrHash);
                        let nonce = web3.eth.getTransactionCount(hospital_wallet.getAddressString())

                        let rawTx = {
                          gasPrice: web3.toHex(web3.eth.gasPrice),
                          gasLimit: web3.toHex(4700000),
                          from: hospital_wallet.getAddressString(),
                          nonce: web3.toHex(nonce),
                          data: data,
                          to: contract.address
                        };

                        let privateKey = ethereumjsUtil.toBuffer("0x" + hospital_wallet.getPrivateKey().toString('hex'), 'hex');
                        let tx = new ethereumjsTx(rawTx);
                        tx.sign(privateKey);

                        web3.eth.sendRawTransaction("0x" + tx.serialize().toString('hex'), function(error, result) {
                          if (error) {
                            console.log(error)
                          } else {
                            //Generate Public Key like npre. It's compressed and has a extra character 0x01 in beginning
                            let compressedPublicKey = Buffer.concat([new Buffer([0x01]), Buffer.from(EthCrypto.publicKey.compress(patient_wallet.getPublicKey().toString("hex")), 'hex')]).toString("base64")

                            exec('python3 ./encrypt.py ' + compressedPublicKey + " '" + emr + "'", (error, stdout, stderr) => {
                              if (error !== null) {
                                console.log(error)
                              } else {
                                //Assume we are pushing encrypted data to proxy re-encryption server
                                let encryptedEMR = stdout.substr(2).slice(0, -2);
                                console.log("Encrypted Message: " + encryptedEMR)

                                //Assume that proxy re-encryption server re-encrypting data when requested by authorized service provider
                                exec('python3 ./re_encrypt.py ' + reEncKey + " " + encryptedEMR, (error, stdout, stderr) => {
                                  if (error !== null) {
                                    console.log(error)
                                  } else {
                                    let reEncryptedEMR = stdout.substr(2).slice(0, -2)
                                    console.log("Re-Encrypted Message: " + reEncryptedEMR)

                                    //Assume service provider decrypting the re-encrypted data provided by the proxy re-encryption server
                                    exec('python3 ./decrypt.py ' + secKeyB + " " + reEncryptedEMR, (error, stdout, stderr) => {
                                      if (error) {
                                        console.log(error)
                                      } else {
                                        let decrypted_message = stdout.substr(2).slice(0, -2)

                                        console.log("Decrypted Message: " + decrypted_message)

                                        //Generate a new key for patient
                                        let new_patient_wallet = ethereumjsWallet.generate();

                                        let secKeyA = Buffer.concat([new Buffer([0x00]), patient_wallet.getPrivateKey()]).toString('base64')
                                        let secKeyB = Buffer.concat([new Buffer([0x00]), new_patient_wallet.getPrivateKey()]).toString('base64')

                                        exec('python3 ./generate_reEncKey.py ' + secKeyA + " " + secKeyB, (error, stdout, stderr) => {
                                          if (error !== null) {
                                            console.log(error)
                                          } else {
                                            let reEncKey = stdout.substr(2).slice(0, -2)

                                            console.log("Re-encryption Key for Patient's new Wallet: " + reEncKey)

                                            //Change patient's key
                                            let data = healthContractInstance.changePatientAccount.getData(reEncKey, new_patient_wallet.getAddressString(), new_patient_wallet.getPublicKey().toString('hex'));
                                            let nonce = web3.eth.getTransactionCount(patient_wallet.getAddressString())

                                            let rawTx = {
                                              gasPrice: web3.toHex(web3.eth.gasPrice),
                                              gasLimit: web3.toHex(4700000),
                                              from: patient_wallet.getAddressString(),
                                              nonce: web3.toHex(nonce),
                                              data: data,
                                              to: contract.address
                                            };

                                            let privateKey = ethereumjsUtil.toBuffer("0x" + patient_wallet.getPrivateKey().toString('hex'), 'hex');
                                            let tx = new ethereumjsTx(rawTx);
                                            tx.sign(privateKey);

                                            web3.eth.sendRawTransaction("0x" + tx.serialize().toString('hex'), function(error, result) {
                                              if (error) {
                                                console.log(error)
                                              } else {
                                                let events = healthContractInstance.allEvents({
                                                  fromBlock: 0,
                                                  toBlock: 'latest'
                                                });
                                                events.get(function(error, logs) {
                                                  for (let count = 0; count < logs.length; count++) {
                                                    console.log("Event Name: " + logs[count].event + " and Args: " + JSON.stringify(logs[count].args))
                                                  }
                                                });
                                              }
                                            })
                                          }
                                        })
                                      }
                                    })
                                  }
                                })
                              }
                            });
                          }
                        })
                      }
                    })
                  }
                })
              }
            })
          }
        })
      }
    })
  }
})
