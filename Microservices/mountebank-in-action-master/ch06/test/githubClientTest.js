'use strict';

//require('any-promise/register/q');

var request = require('request-promise'),
  assert = require('assert'),
  authBaseURL = process.env.MBSTAR_BASE_AUTH_URL,
  clientId = process.env.GH_BASIC_CLIENT_ID,
  authorizeURL = authBaseURL + '/login/oauth/authorize?scope=user:email&client_id=' + clientId;

describe('githubClient', function () {
  it('should navigate OAuth flow correctly', function (done) {
    request(authorizeURL).then(function (body) {
      assert.strictEqual(body, 'You have NOT starred the mountebank repo. Time to fix that!');
    }).done(function () { done(); }, done);
  });
});
