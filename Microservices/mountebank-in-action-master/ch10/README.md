# Chapter 10: Performance testing with mountebank

## Running the Adoption Service

First we need to install all dependencies:

````
(cd adoptionService && npm install)
````

Then we can run it. Without any environment variables, the service connects to the
real RescueOrgs service:

````
(cd adoptionService && npm start)
````

You can run the API calls with `curl` in another console window:

````
curl http://localhost:5000/nearbyAnimals?postalCode=75228&maxDistance=50
curl http://localhost:5000/animals/10677691
````

## Listing 10.1: Capturing basic responses from RescueGroups.org

We have to add an environment variable to tell the Adoption service to use the URL of our proxy,
which we'll run on port 3000. Start it like this:

````
(cd adoptionService && RESCUE_URL=http://localhost:3000/ npm start)
````

Then, in a separate terminal window:

````
mb restart --configfile examples/basicProxy.json &

# Capture the two searches
curl http://localhost:5000/nearbyAnimals?postalCode=75228&maxDistance=20
curl http://localhost:5000/nearbyAnimals?postalCode=75228&maxDistance=50

# Capture the detailed results. Change the ids to ones returned in the last search
curl http://localhost:5000/animals/10677691
curl http://localhost:5000/animals/10837552
curl http://localhost:5000/animals/11618347

mb save --removeProxies --savefile mb.json
mb stop
````

The test data will be stored in mb.json.

## Listing 10.2: Capturing the latency of the downstream system

This example is nearly identical to the previous one, with the addition of the
`addWaitBehavior` flag. Once again run the Adoption service with the proxy URL:

````
(cd adoptionService && RESCUE_URL=http://localhost:3000/ npm start)
````

Then, in a separate terminal window:

````
mb restart --configfile examples/proxyWithLatency.json &

# Capture the two searches
curl http://localhost:5000/nearbyAnimals?postalCode=75228&maxDistance=20
curl http://localhost:5000/nearbyAnimals?postalCode=75228&maxDistance=50

# Capture the detailed results. Change the ids to ones returned in the last search
curl http://localhost:5000/animals/10677691
curl http://localhost:5000/animals/10837552
curl http://localhost:5000/animals/11618347

mb save --removeProxies --savefile mb.json
mb stop
````

The test data will be stored in mb.json, and will contain the actual latencies.

## Listing 10.3: Assigning random latency swings

Our imposter always returns a body of "Hello, world!" with random latency, but once in
about every 10 calls, it takes an order of magnitude longer to respond.

````
mb restart --allowInjection --configfile examples/respondWithRandomLatency.json &

# Call 20 times. This might be painful
for i in {1..20}
do
  time curl http://localhost:3000/
done

mb stop
````

## Listing 10.5: Running the performance test

Once again run the Adoption service connected to our imposter, now no longer a proxy:

````
(cd adoptionService && RESCUE_URL=http://localhost:3000/ npm start)
````

Run the imposter using the saved test data:

````
mb restart --configfile mb.json --loglevel warn
````

Now we can run Gatling, pointing to our simulation file

````
$GATLING_HOME/bin/gatling.sh -sf gatling/simulations -s adoptionservice.SearchForPetSimulation -rf gatling/reports
````


