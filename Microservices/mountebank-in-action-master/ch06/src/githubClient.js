'use strict';

// Adapted from https://developer.github.com/v3/guides/basics-of-authentication/
// You'll need to register your own application and set the
// GH_BASIC_CLIENT_ID and GH_BASIC_CLIENT_SECRET environment variables

var path = require('path'),
  app = require('express')(),
  clientId = process.env.GH_BASIC_CLIENT_ID,
  clientSecret = process.env.GH_BASIC_CLIENT_SECRET,
  authBaseURL = process.env.MBSTAR_BASE_AUTH_URL || 'https://github.com',
  apiBaseURL = process.env.MBSTAR_BASE_API_URL || 'https://api.github.com';

app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.get('/', function (request, response) {
  response.render('index', { clientId: clientId, baseURL: authBaseURL });
});

//require('request-promise-any/node_modules/any-promise/register/q');
//var httpRequest = require('request-promise-any');
var httpRequest = require('request-promise');

app.get('/callback', function (request, response) {
  var url = require('url'),
    query = url.parse(request.url, true).query,
    util = require('util'),
    postBody = util.format('client_id=%s&client_secret=%s&code=%s',
      clientId, clientSecret, query.code);

  httpRequest({
    method: 'POST',
    uri: authBaseURL + '/login/oauth/access_token',
    body: postBody,
    headers: { Accept: 'application/json' }
  }).then(function (body) {
    var accessToken = JSON.parse(body).access_token;
    return httpRequest({
      method: 'GET',
      uri: apiBaseURL + '/user/starred/bbyars/mountebank',
      headers: {
        Authorization: 'token ' + accessToken,
        Accept: 'application/vnd.github.v3+json',
        'User-Agent': 'mountebank'
      },
      resolveWithFullResponse: true
    });
  }).then(function (githubResponse) {
    //if (githubResponse.statusCode === 204) {
      response.send('You have already starred the mountebank repo');
    //}
  }).catch(function () {
    response.send('You have NOT starred the mountebank repo. Time to fix that!');
  }).done();
});

app.listen(3000, function () {
  console.log('github client listening on port 3000');
});
