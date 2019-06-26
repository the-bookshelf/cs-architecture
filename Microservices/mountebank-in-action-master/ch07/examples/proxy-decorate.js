function (request, response) {
  var fs = require('fs'),
    currentValue = parseInt(response.headers['x-rate-limit-remaining']),
    decrement = 25;

  if (fs.existsSync('rate-limit.txt')) {
      currentValue = parseInt(fs.readFileSync('rate-limit.txt'));
  }

  if (currentValue <= 0) {
    response.statusCode = 429;
    response.body = {
      errors: [{ code: 88, message: 'Rate limit exceeded' }]
    };
    response.headers['x-rate-limit-remaining'] = 0;
  }
  else {
    fs.writeFileSync('rate-limit.txt', currentValue - decrement);
    response.headers['x-rate-limit-remaining'] = currentValue - decrement;
  }
}
