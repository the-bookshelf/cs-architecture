'use strict';

var request = require('request-json'),
  Q = require('q'),
  PATH = 'http/v2.json';

var PUBLIC_API_KEY = "ThavaS7e";

function create (url) {
  console.log('RescueGroups gateway initiated with url ' + url);
  var client = request.createClient(url);

  function getNearbyAnimals(postalCode, maxDistance) {
    var deferred = Q.defer(),
      requestBody = {
        apikey: PUBLIC_API_KEY,
        objectType: "animals",
        objectAction: "publicSearch",
        search:
        {
          resultStart: "0",
          resultLimit: "10",
          resultSort: "animalLocationDistance",
          resultOrder: "asc",
          filters:
            [
              {
                fieldName: "animalStatus",
                operation: "equals",
                criteria: "Available"
              },
              {
                fieldName: "animalLocationDistance",
                operation: "radius",
                criteria: maxDistance
              },
              {
                fieldName: "animalLocation",
                operation: "equals",
                criteria: postalCode
              }
            ],
          filterProcessing: "1",
          fields: ["animalID","animalLocationCoordinates","animalSpecies","animalThumbnailUrl"]
        }
      };

    client.post(PATH, requestBody, function (err, res, body) {
      if (err) {
        deferred.reject(err);
      }
      else {
        var animals = [];

        Object.keys(body.data).forEach(function (key) {
          var animal = body.data[key];
          animals.push({
            id: animal.animalID,
            coords: animal.animalLocationCoordinates,
            species: animal.animalSpecies,
            thumbnail: animal.animalThumbnailUrl
          });
        });

        deferred.resolve({animals: animals});
      }
    });

    return deferred.promise;
  }

  function getAnimalById (id) {
    var deferred = Q.defer(),
      requestBody = {
        apikey: PUBLIC_API_KEY,
        objectType: "animals",
        objectAction: "publicSearch",
        search:
        {
          filters:
            [
              {
                fieldName: "animalID",
                operation: "equals",
                criteria: id
              }
            ],
          filterProcessing: "1",
          fields: ["animalAgeString","animalBirthdate","animalLocation",
            "animalLocationCoordinates","animalLocationDistance","animalLocationCitystate",
            "animalSpecies","animalThumbnailUrl"]
        }
      };

    client.post(PATH, requestBody, function (err, res, body) {
      if (err) {
        deferred.reject(err);
      }
      else {
        var animal = body.data[id];
        deferred.resolve({
          id: id,
          birthdate: animal.animalBirthdate,
          age: animal.animalAgeString,
          postalCode: animal.animalLocation,
          coords: animal.animalLocationCoordinates,
          distance: animal.animalLocationDistance,
          city: animal.animalLocationCitystate,
          species: animal.animalSpecies,
          thumbnail: animal.animalThumbnailUrl
        });
      }
    });

    return deferred.promise;
  }

  return {
    getNearbyAnimals: getNearbyAnimals,
    getAnimalById: getAnimalById
  };
}

module.exports = {
  create: create
};

