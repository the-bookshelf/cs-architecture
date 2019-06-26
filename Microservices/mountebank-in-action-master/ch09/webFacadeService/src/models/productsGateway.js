'use strict';

require('any-promise/register/q');

var request = require('request-promise-any');

function create (url) {
  function getProducts() {
    var products;

    return request(url + '/products').then(function (body) {
      return JSON.parse(body);
    });
  }

  return {
    getProducts: getProducts
  };
}

module.exports = {
  create: create
};

