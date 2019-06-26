'use strict';

var assert = require('assert'),
  sinon = require('sinon'),
  Q = require('q'),
  productCatalog = require('../../src/models/productCatalog');

describe('productCatalog', function () {
  describe('#retrieve', function () {
    it('should merge results from product and content gateways', function (done) {
      var productsResult = {
            products: [
              { id: 1, name: 'PRODUCT-1' },
              { id: 2, name: 'PRODUCT-2' }
            ]
        },
        productsGateway = { getProducts: sinon.stub().returns(Q(productsResult)) },
        contentResults = {
          content: [
            { id: 1, copy: 'COPY-1', image: 'IMAGE-1' },
            { id: 2, copy: 'COPY-2', image: 'IMAGE-2' }
          ]
        },
        contentGateway = { getContent: sinon.stub().withArgs([1, 2]).returns(Q(contentResults)) },
        catalog = productCatalog.create(productsGateway, contentGateway);

      catalog.retrieve().done(function (result) {
        assert.deepEqual(result, [
          { id: 1, name: 'PRODUCT-1', copy: 'COPY-1', image: 'IMAGE-1' },
          { id: 2, name: 'PRODUCT-2', copy: 'COPY-2', image: 'IMAGE-2' },
        ]);

        done();
      });
    });
  });
});
