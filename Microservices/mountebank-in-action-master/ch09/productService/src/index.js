'use strict';

var express = require('express');

var app = express();

app.get('/products', function (req, res) {
  console.log('[Product service] /products');
  res.sendFile(__dirname + '/products.json');
});

app.listen(3000, function () {
  console.log('Product service started on port 3000');
});
