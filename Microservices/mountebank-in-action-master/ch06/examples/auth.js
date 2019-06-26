function (request, state, logger, callback) {
  var http = require('http'),
    options = {
      method: 'GET',
      hostname: 'localhost',
      port: 3000,
      path: '/callback?code=TEST-CODE'
    },
    httpRequest = http.request(options, function (response) {
      var body = '';
      response.setEncoding('utf8');
      response.on('data', function (chunk) {
        body += chunk;
      });
      response.on('end', function () {
        callback({ body: body });
      });
    });

  httpRequest.end();
}
