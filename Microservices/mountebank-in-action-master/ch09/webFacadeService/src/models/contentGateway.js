'use strict';

require('any-promise/register/q');

var request = require('request-promise-any');

function create (url) {
  function getContent(productIds) {
    var query = 'ids=' + productIds.join(',');
    return request(url + '/content?' + query).then(function (body) {
      return JSON.parse(body);
    });
  }

  return {
    getContent: getContent
  };
}

module.exports = {
  create: create
};

