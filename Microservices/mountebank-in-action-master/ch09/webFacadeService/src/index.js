'use strict';

var express = require('express'),
  productServiceURL = process.env['PRODUCT_SERVICE_URL'],
  contentServiceURL = process.env['CONTENT_SERVICE_URL'],
  productsGateway = require('./models/productsGateway').create(productServiceURL),
  contentGateway = require('./models/contentGateway').create(contentServiceURL),
  productCatalog = require('./models/productCatalog').create(productsGateway, contentGateway);

var app = express();

app.get('/products', function (request, response) {
  productCatalog.retrieve().then(function (results) {
    response.json({ products: results });
  }, function (err) {
    response.statusCode = 500;
    response.send(err);
  });
});

app.listen(2000, function () {
  console.log('Web facade service started on port 2000');
});
