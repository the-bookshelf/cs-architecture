function handlePUT(req, res) {
    // Return Forbidden message for POST Request
    res.status(403).send('Forbidden!');
}

function handleGET(req, res) {
    // Retrieve URL query parameters
    var zip = req.query.zip;
    var countrycode = req.query.countrycode;
    var apikey = req.query.apikey;

    // Create complete OpenWeatherMap URL using user-provided parameters
    var baseUrl = 'http://api.openweathermap.org/data/2.5/weather';
    var completeUrl = baseUrl + "?zip=" + zip + "," + countrycode + "&appid=" + apikey;
    console.log("Request URL--> " + completeUrl)

    // Import the sync-request module to invoke HTTP request
    var weatherServiceRequest = require('sync-request');

    // Invoke OpenWeatherMap API
    var weatherServiceResponse = weatherServiceRequest('GET', completeUrl);
    var ststusCode = weatherServiceResponse.statusCode;
    console.log("RESPONSE STATUS -->" + ststusCode);

    // Check if response was error or success reponse
    if (ststusCode < 300) {
        console.log("JSON BODY DATA --->>" + weatherServiceResponse.getBody());
        // For Success response, return back appropriate status code, content-type and body
        console.log("Setting response content type to json");
        res.setHeader('Content-Type', 'application/json');
        res.status(ststusCode);
        res.send(weatherServiceResponse.getBody());
    } else {
        console.log("ERROR RESPONSE -->" + ststusCode);
        //For Error response send back appropriate error details
        res.status(ststusCode);
        res.send(ststusCode);
    }
}

/**
 * Responds to a GET request with Weather Information. Forbids a PUT request.
 *
 * @param {Object} req Cloud Function request context.
 * @param {Object} res Cloud Function response context.
 */
exports.weatherService = (req, res) => {
    switch (req.method) {
        case 'GET':
            handleGET(req, res);
            break;
        case 'PUT':
            handlePUT(req, res);
            break;
        default:
            res.status(500).send({
                error: 'Something blew up!'
            });
            break;
    }
};