# Chapter 6: Programming mountebank

The injection examples require starting mountebank with the `--allowInjection` flag.
To keep it secure, we'll also test with the `--localOnly` flag.

In order to maintain readable JavaScript functions, the example JSON config files use
the `stringify` function mountebank adds to EJS to grab an external JavaScript file and
JSON-escape it.

## Listing 6.3-6.5: Testing the Roadie service predicate

````
mb restart --allowInjection --localOnly --configfile examples/roadie-predicate.json &

# More than 10 degrees over (2nd row)
# Should respond with "Humidity levels dangerous, action required"
curl -i http://localhost:3000/ --data 'Day,Description,High,Low,Precip,Wind,Humidity
4-Jun,PM Thunderstorms,83,71,50%,E 5 mph,65%
5-Jun,PM Thunderstorms,82,69,60%,NNE 8mph,73%
6-Jun,Sunny,90,67,10%,NNE 11mph,55%
7-Jun,Mostly Sunny,86,65,10%,NE 7 mph,53%
8-Jun,Partly Cloudy,84,68,10%,ESE 4 mph,53%
9-Jun,Partly Cloudy,88,68,0%,SSE 11mph,56%
10-Jun,Partly Cloudy,89,70,10%,S 15 mph,57%
11-Jun,Sunny,90,73,10%,S 16 mph,61%
12-Jun,Partly Cloudy,91,74,10%,S 13 mph,63%
13-Jun,Partly Cloudy,90,74,10%,S 17 mph,59%'

# More than 3 days over (last 3 rows)
# Should respond with "Humidity levels dangerous, action required"
curl -i http://localhost:3000/ --data 'Day,Description,High,Low,Precip,Wind,Humidity
4-Jun,PM Thunderstorms,83,71,50%,E 5 mph,65%
5-Jun,PM Thunderstorms,82,69,60%,NNE 8mph,69%
6-Jun,Sunny,90,67,10%,NNE 11mph,55%
7-Jun,Mostly Sunny,86,65,10%,NE 7 mph,53%
8-Jun,Partly Cloudy,84,68,10%,ESE 4 mph,53%
9-Jun,Partly Cloudy,88,68,0%,SSE 11mph,56%
10-Jun,Partly Cloudy,89,70,10%,S 15 mph,57%
11-Jun,Sunny,90,73,10%,S 16 mph,61%
12-Jun,Partly Cloudy,91,74,10%,S 13 mph,63%
13-Jun,Partly Cloudy,90,74,10%,S 17 mph,64%'

# Should respond with "Humidity levels OK for the next 10 days"
curl -i http://localhost:3000/ --data 'Day,Description,High,Low,Precip,Wind,Humidity
4-Jun,PM Thunderstorms,83,71,50%,E 5 mph,65%
5-Jun,PM Thunderstorms,82,69,60%,NNE 8mph,63%
6-Jun,Sunny,90,67,10%,NNE 11mph,55%
7-Jun,Mostly Sunny,86,65,10%,NE 7 mph,53%
8-Jun,Partly Cloudy,84,68,10%,ESE 4 mph,53%
9-Jun,Partly Cloudy,88,68,0%,SSE 11mph,56%
10-Jun,Partly Cloudy,89,70,10%,S 15 mph,57%
11-Jun,Sunny,90,73,10%,S 16 mph,61%
12-Jun,Partly Cloudy,91,74,10%,S 13 mph,63%
13-Jun,Partly Cloudy,90,74,10%,S 17 mph,59%'

mb stop
````

## Listing 6.7: Testing the Roadie service response

````
# No predicates in imposter, all logic performed in response
mb restart --allowInjection --localOnly --configfile examples/roadie-response.json &

# More than 10 degrees over (2nd row)
# Should respond with "Humidity levels dangerous, action required"
curl -i http://localhost:3000/ --data 'Day,Description,High,Low,Precip,Wind,Humidity
4-Jun,PM Thunderstorms,83,71,50%,E 5 mph,65%
5-Jun,PM Thunderstorms,82,69,60%,NNE 8mph,73%
6-Jun,Sunny,90,67,10%,NNE 11mph,55%
7-Jun,Mostly Sunny,86,65,10%,NE 7 mph,53%
8-Jun,Partly Cloudy,84,68,10%,ESE 4 mph,53%
9-Jun,Partly Cloudy,88,68,0%,SSE 11mph,56%
10-Jun,Partly Cloudy,89,70,10%,S 15 mph,57%
11-Jun,Sunny,90,73,10%,S 16 mph,61%
12-Jun,Partly Cloudy,91,74,10%,S 13 mph,63%
13-Jun,Partly Cloudy,90,74,10%,S 17 mph,59%'

# More than 3 days over (last 3 rows)
# Should respond with "Humidity levels dangerous, action required"
curl -i http://localhost:3000/ --data 'Day,Description,High,Low,Precip,Wind,Humidity
4-Jun,PM Thunderstorms,83,71,50%,E 5 mph,65%
5-Jun,PM Thunderstorms,82,69,60%,NNE 8mph,69%
6-Jun,Sunny,90,67,10%,NNE 11mph,55%
7-Jun,Mostly Sunny,86,65,10%,NE 7 mph,53%
8-Jun,Partly Cloudy,84,68,10%,ESE 4 mph,53%
9-Jun,Partly Cloudy,88,68,0%,SSE 11mph,56%
10-Jun,Partly Cloudy,89,70,10%,S 15 mph,57%
11-Jun,Sunny,90,73,10%,S 16 mph,61%
12-Jun,Partly Cloudy,91,74,10%,S 13 mph,63%
13-Jun,Partly Cloudy,90,74,10%,S 17 mph,64%'

# Should respond with "Humidity levels OK for the next 10 days"
curl -i http://localhost:3000/ --data 'Day,Description,High,Low,Precip,Wind,Humidity
4-Jun,PM Thunderstorms,83,71,50%,E 5 mph,65%
5-Jun,PM Thunderstorms,82,69,60%,NNE 8mph,63%
6-Jun,Sunny,90,67,10%,NNE 11mph,55%
7-Jun,Mostly Sunny,86,65,10%,NE 7 mph,53%
8-Jun,Partly Cloudy,84,68,10%,ESE 4 mph,53%
9-Jun,Partly Cloudy,88,68,0%,SSE 11mph,56%
10-Jun,Partly Cloudy,89,70,10%,S 15 mph,57%
11-Jun,Sunny,90,73,10%,S 16 mph,61%
12-Jun,Partly Cloudy,91,74,10%,S 13 mph,63%
13-Jun,Partly Cloudy,90,74,10%,S 17 mph,59%'

mb stop
````

## Listing 6.8: Using state to detect dangerous humidity across requests

````
# No predicates in imposter, all logic performed in response
mb restart --allowInjection --localOnly --configfile examples/roadie-state.json &

# Should respond with "Humidity levels OK"
# But note that Jun-11 is over 60%
curl -i http://localhost:3000/ --data 'Day,Description,High,Low,Precip,Wind,Humidity
9-Jun,Partly Cloudy,88,68,0%,SSE 11mph,56%
10-Jun,Partly Cloudy,89,70,10%,S 15 mph,57%
11-Jun,Sunny,90,73,10%,S 16 mph,61%'

# By 13-Jun, we've reached the 3 consecutive day marker
# Should respond with "Humidity levels dangerous, action required"
curl -i http://localhost:3000/ --data 'Day,Description,High,Low,Precip,Wind,Humidity
12-Jun,Partly Cloudy,91,74,10%,S 13 mph,63%
13-Jun,Partly Cloudy,90,74,10%,S 17 mph,61%
14-Jun,Partly Cloudy,90,74,10%,S 15 mph,59%'

mb stop
````

## Listing 6.11-6.14: Virtualizing Github's API to test OAuth flow

````
#!/usr/bin/env bash

# Install dependencies
npm install

# Point app to mountebank instead of github
export MBSTAR_BASE_AUTH_URL=http://localhost:3001
export MBSTAR_BASE_API_URL=http://localhost:3001

# Configure secrets
# Normally you should never check in the secret to source control
# The id and secret below are no longer active, so you should register your
# own github app if you want to test integrated to the actual github API
export GH_BASIC_CLIENT_ID=79553235751bfcb87654
export GH_BASIC_CLIENT_SECRET=a1284814800451b97f7288d9c9cd762bf176eb87

# Start the web app
npm start &
# Captures the PID of the last backgrounded process
WEB_PID=$!

# Start mountebank
mb restart --allowInjection --localOnly --configfile examples/auth.json &

# Wait for mb to fully initialize by waiting for it to create the pidfile
while [ ! -f mb.pid ]; do
  sleep 1
done

# Run the test against a virtualized github API
npm test

# Stop the web app and mountebank
kill $WEB_PID
mb stop
````
