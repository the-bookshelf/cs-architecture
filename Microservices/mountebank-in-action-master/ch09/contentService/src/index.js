'use strict';

var express = require('express');

var app = express();

var contentDatabase = {
  "2599b7f4": {
    id: "2599b7f4",
    copy: "Treat your dog like the king he is",
    image: "/content/c5b221e2"
  },
  "e1977c9e": {
    id: "e1977c9e",
    copy: "Love your fish; they'll love you back",
    image: "/content/a0fad9fb"
  }
};

app.get('/content', function (req, res) {
  if (!req.query.ids) {
    res.status(400).send('Must send ids query parameter');
  }
  else {
    console.log('[Content service] /content?ids=' + req.query.ids);
    var ids = req.query.ids.split(','),
      content = ids.map(function (id) {
        return contentDatabase[id];
      });

    res.json({ content: content });
  }
});

app.listen(4000, function () {
  console.log('Content service started on port 4000');
});
