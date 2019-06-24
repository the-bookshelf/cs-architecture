App = {
  web3Provider: null,
  contracts: {},
  account: '0x0',
  loading: false,
  tokenPrice: 0,
  tokensSold: 0,
  tokensAvailable: 500000,

  init: function() {
    return App.initWeb3();
  },

  initWeb3: function() {
    if (typeof web3 !== 'undefined') {
      App.web3Provider = web3.currentProvider;
      web3 = new Web3(web3.currentProvider);
    } else {
      App.web3Provider = new Web3.providers.HttpProvider('http://localhost:7545');
      web3 = new Web3(App.web3Provider);
    }
    return App.initContracts();
  },

  initContracts: function() {
    $.getJSON("PacktTokenSale.json", function(packtTokenSale) {
      App.contracts.PacktTokenSale = TruffleContract(packtTokenSale);
      App.contracts.PacktTokenSale.setProvider(App.web3Provider);
      App.contracts.PacktTokenSale.deployed().then(function(packtTokenSale) {
        console.log("Dapp Token Sale Address:", packtTokenSale.address);
      });
    }).done(function() {
      $.getJSON("PacktToken.json", function(packtToken) {
        App.contracts.PacktToken = TruffleContract(packtToken);
        App.contracts.PacktToken.setProvider(App.web3Provider);
        App.contracts.PacktToken.deployed().then(function(packtToken) {
          console.log("Dapp Token Address:", packtToken.address);
        });

        App.listenForEvents();
        return App.render();
      });
    });
  },

  // Listen for events emitted from the contract
  listenForEvents: function() {
    App.contracts.PacktTokenSale.deployed().then(function(instance) {
      instance.Sell({}, {
        fromBlock: 0,
        toBlock: 'latest',
      }).watch(function(error, event) {
        console.log("event triggered", event);
        App.render();
      });
    });
  },

  render: function() {
    if (App.loading) {
      return;
    }
    App.loading = true;

    const loader  = $('#contentLoader');
    const content = $('#content');

    loader.show();
    content.hide();

    web3.eth.getCoinbase(function(err, account) {
      if (err === null) {
        App.account = account;
        $('#accountAddress').html(account);
      }
    });

    App.contracts.PacktTokenSale.deployed().then(function(instance) {
      packtTokenSaleInstance = instance;
      return packtTokenSaleInstance.tokenPrice();
    }).then(function(tokenPrice) {
      App.tokenPrice = tokenPrice;
      $('#tokenPrice').html(web3.fromWei(App.tokenPrice, "ether").toNumber());
      return packtTokenSaleInstance.tokensSold();
    }).then(function(tokensSold) {
      App.tokensSold = tokensSold.toNumber();
      $('#tokensSold').html(App.tokensSold);
      $('#tokensAvailable').html(App.tokensAvailable);

      var progress = (Math.ceil(App.tokensSold) / App.tokensAvailable) * 100;
      $('#progress').css('width', progress + '%');

      App.contracts.PacktToken.deployed().then(function(instance) {
        packtTokenInstance = instance;
        return packtTokenInstance.balanceOf(App.account);
      }).then(function(balance) {
        $('#tokenBalance').html(balance.toNumber());
        return packtTokenInstance.name();
      }).then(function(name) {
        $('#tokenName').html(name);
        return packtTokenInstance.symbol();
      }).then(function(symbol) {
        $('#tokenSymbol').html(symbol);
        App.loading = false;
        loader.hide();
        content.show();
      });
    });
  },

  buyTokens: function() {
    $('#content').hide();
    $('#contentLoader').show();
    const numberOfTokens = $('#numberOfTokens').val();
    console.log(numberOfTokens);
    App.contracts.PacktTokenSale.deployed().then(function(instance) {
      return instance.buyTokens(numberOfTokens, {
        from: App.account,
        value: numberOfTokens * App.tokenPrice,
      });
    }).then(function(result) {
      console.log(result);
      $('form').trigger('reset');
    });
  }
};

App.init();
