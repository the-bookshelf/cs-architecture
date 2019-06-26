function (request, state) {
  function csvToObjects (csvData) {
    var lines = csvData.split('\n'),
      headers = lines[0].split(','),
      result = [];

    // Remove the headers
    lines.shift();

    lines.forEach(function (line) {
      var fields = line.split(','),
        row = {};

      for (var i = 0; i < headers.length; i++) {
        var header = headers[i],
          data = fields[i];
        row[header] = data;
      }

      result.push(row);
    });

    return result;
  }

  function hasThreeDaysOutOfRange (humidities) {
    var daysOutOfRange = 0,
      result = false;

    humidities.forEach(function (humidity) {
      if (humidity < 60) {
        daysOutOfRange = 0;
        return;
      }

      daysOutOfRange += 1;
      if (daysOutOfRange >= 3) {
        result = true;
      }
    });

    return result;
  }

  // Initialize state arrays
  if (!state.humidities) {
    state.days = [];
    state.humidities = [];
  }

  var rows = csvToObjects(request.body);

  rows.forEach(function (row) {
    if (state.days.indexOf(row.Day) < 0) {
      state.days.push(row.Day);
      state.humidities.push(row.Humidity.replace('%', ''));
    }
  });

  var hasDayTenPercentOutOfRange = state.humidities.some(function (humidity) {
    return humidity >= 70;
  });

  if (hasDayTenPercentOutOfRange || hasThreeDaysOutOfRange(state.humidities)) {
    return {
      statusCode: 400,
      body: 'Humidity levels dangerous, action required'
    };
  }
  else {
    return {
      body: 'Humidity levels OK'
    };
  }
}
