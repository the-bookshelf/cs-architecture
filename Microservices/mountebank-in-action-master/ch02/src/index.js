'use strict';

require('any-promise/register/q');

var express = require('express'),
    request = require('request-promise-any'),
    Q = require('q');

// config
var productServiceURL = 'http://localhost:3000',
    contentServiceURL = 'http://localhost:4000';

function getProductsWithContent () {
    var products;

    return request(productServiceURL + '/products').then(function (body) {
        products = JSON.parse(body).products;

        var productIds = products.map(function (product) {
                return product.id;
            }),
            contentQuery = '?ids=' + productIds.join(',');

        return request(contentServiceURL + '/content' + contentQuery);
    }).then(function (body) {
        var contentEntries = JSON.parse(body).content;

        products.forEach(function (product) {
            var contentEntry = contentEntries.find(function (entry) {
                return entry.id === product.id;
            });
            product.copy = contentEntry.copy;
            product.image = contentEntry.image;
        });

        return Q(products);
    });
}

var app = express();

app.get('/products', function (req, res) {
    getProductsWithContent().then(function (results) {
        res.send({ products: results });
    }, function (err) {
        console.log(err);
        res.statusCode = 500;
        res.send(err);
    });
});

app.listen(2000, function () {
    console.log('Service started on port 2000');
});
