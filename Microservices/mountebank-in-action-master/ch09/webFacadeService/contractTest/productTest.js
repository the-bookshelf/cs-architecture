'use strict';

require('any-promise/register/q');

var productServiceURL = "http://localhost:3000",
  request = require('request-promise-any'),
  assert = require('assert');

function assertString (obj) {
  assert.ok(obj !== null && typeof obj === 'string');
}

function assertISODate (obj) {
  assertString(obj);
  assert.ok(/201\d-[01]\d-[0123]\d/.test(obj), 'not ISO date');
}

describe('Product service', function () {
  it('should return products with correct fields', function () {
    return request(productServiceURL + '/products').then(function (body) {
      var products = JSON.parse(body).products;
      assert.ok(products.length > 0, 'catalog empty');

      products.forEach(function (product) {
        assertString(product.name);
        assertString(product.description);
        assertISODate(product.availabilityDates.start);
        assertISODate(product.availabilityDates.end);
      });
    });
  });
});
