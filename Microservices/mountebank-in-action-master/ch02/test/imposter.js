'use strict';

require('any-promise/register/q');
var request = require('request-promise-any');

module.exports = function (options) {
  var config = options || {};
  config.stubs = [];

  function create () {
    return request({
      method: "POST",
      uri: "http://localhost:2525/imposters",
      json: true,
      body: config
    });
  }

  function destroy () {
    return request({
      method: "DELETE",
      uri: "http://localhost:2525/imposters/" + config.port
    });
  }

  function destroyAll () {
    return request({
      method: "DELETE",
      uri: "http://localhost:2525/imposters"
    });
  }

  function withStub () {
    var stub = { responses: [], predicates: [] },
      builders = {
        matchingRequest: function (predicate) {
          stub.predicates.push(predicate);
          return builders;
        },
        respondingWith: function (response) {
          stub.responses.push({ is: response });
          return builders;
        },
        create: create,
        withStub: withStub
      };

    config.stubs.push(stub);
    return builders;
  }

  return {
    withStub: withStub,
    create: create,
    destroy: destroy,
    destroyAll: destroyAll
  };
};
