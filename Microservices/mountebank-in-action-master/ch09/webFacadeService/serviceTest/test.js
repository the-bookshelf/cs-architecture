'use strict';

function addSuffixToObjects (suffixes, fields) {
  return suffixes.map(function (suffix) {
    var result = {};
    fields.forEach(function (field) {
      result[field] = field.toUpperCase() + '-' + suffix;
    });
    return result;
  });
}

var imposter = require('./imposter'),
  productPort = 3000;

function createProductImposter (suffixes) {
  var products = addSuffixToObjects(suffixes, ['id', 'name', 'description']);

  return imposter({
    port: productPort,
    protocol: "http",
    name: "Product Service"
  })
    .withStub()
    .matchingRequest({equals: {path: "/products"}})
    .respondingWith({
      statusCode: 200,
      headers: {"Content-Type": "application/json"},
      body: { products: products }
    })
    .create();
}

var contentPort = 4000;

function createContentImposter(suffixes) {
  var contentEntries = addSuffixToObjects(suffixes, ['id', 'copy', 'image']);

  return imposter({
    port: contentPort,
    protocol: "http",
    name: "Content Service"
  })
    .withStub()
    .matchingRequest({
      equals: {
        path: "/content",
        query: {ids: "ID-1,ID-2"}
      }
    })
    .respondingWith({
      statusCode: 200,
      headers: {"Content-Type": "application/json"},
      body: { content: contentEntries }
    })
    .create();
}

require('any-promise/register/q');

var request = require('request-promise-any'),
  assert = require('assert'),
  webFacadeURL = 'http://localhost:2000';

describe('/products', function () {
  it('combines product and content data', function (done) {
    createProductImposter(['1', '2']).then(function () {
      return createContentImposter(['1', '2']);
    }).then(function () {
      return request(webFacadeURL + '/products');
    }).then(function (body) {
      var products = JSON.parse(body).products;

      assert.deepEqual(products, [
        {
          "id": "ID-1",
          "name": "NAME-1",
          "description": "DESCRIPTION-1",
          "copy": "COPY-1",
          "image": "IMAGE-1"
        },
        {
          "id": "ID-2",
          "name": "NAME-2",
          "description": "DESCRIPTION-2",
          "copy": "COPY-2",
          "image": "IMAGE-2"
        }
      ]);
      return imposter().destroyAll();
    }).done(function () {
      done();
    });
  });
});
