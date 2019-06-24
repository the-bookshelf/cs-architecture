module.exports = {
    networks: {
        development: {
            host: "localhost",
            port: 8545,
            network_id: "*" // Match any network id
        }
    }
};

networks: { 
    development: { 
        host: "127.0.0.1", 
        port: 8545,
        network_id: "*" // match any network 
    },
    test: {
        host: "55.55.55.55",
        port: 8545, 
        network_id: 1, // Ethereum main network  
    }
}

truffle migrate --network test


networks: { 
    ropsten: { 
        provider: new HDWalletProvider(mnemonic, 
                    "https://ropsten.infura.io/"), 
        network_id: "3" 
    }, 
    development: { 
        provider: new HDWalletProvider(mnemonic,
                    "http://localhost:8545/"), 
        network_id: "*"
    }
}

networks: { 
    ropsten: { 
        provider: function() { 
            return new HDWalletProvider(mnemonic, 
                "https://ropsten.infura.io/"); 
        }, 
        network_id: "3"
   }, 
   test: { 
        provider: function() { 
            return new HDWalletProvider(mnemonic, 
                "http://localhost:8545/"); 
        }, 
        network_id: "*"
    }
}

networks: { 
    development: { 
        provider: function() { 
            return new HDWalletProvider(mnemonic, 
                        "http://localhost:8545/"); 
        },
        port: 8545,
        network_id: 1208,
        from: "0xB0108b70A181eD91cb1D8d8c822419F0e439f724",
        gas: 560000
    },
    test: {
        host: "55.55.55.55",
        port: 8545, 
        network_id: 1,
        gas: 470000,
        gasPrice: 20000000000
    }
}

module.exports = { 
    contracts_build_directory: "./build_output", 
    networks: { 
        development: { 
            host: "localhost", 
            port: 8545, 
            network_id: "*" 
        } 
    } 
};

module.exports = { 
    contracts_build_directory: "../../build_output", 
};


module.exports = { 
    contracts_build_directory: "C:\\Users\\username\\build_output", 
};

solc: { 
    optimizer: { 
        enabled: true, 
        runs: 200 
    } 
}

mocha: { 
    useColors: true,
    ui: 'tdd',
    reporter: 'list'
}