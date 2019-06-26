var fs = require('fs');

fs.readFile('input.txt', function (err, data) {
  var lines = data.toString().split('\n');
  console.log(lines.sort());
});

console.log('The end...');
