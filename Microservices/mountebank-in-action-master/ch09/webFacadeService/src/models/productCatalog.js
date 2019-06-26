'use strict';

function create(productsGateway, contentGateway) {
  function retrieve () {
    var products;

    return productsGateway.getProducts().then(function (response) {
      products = response.products;

      var productIds = products.map(function (product) {
        return product.id;
      });
      return contentGateway.getContent(productIds);
    }).then(function (response) {
      var contentEntries = response.content;

      products.forEach(function (product) {
        var contentEntry = contentEntries.find(function (entry) {
          return entry.id === product.id;
        });
        product.copy = contentEntry.copy;
        product.image = contentEntry.image;
      });

      return products;
    });
  }

  return {
    retrieve: retrieve
  };
}

module.exports = {
  create: create
};
