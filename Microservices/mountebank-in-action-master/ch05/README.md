# Chapter 5: Adding record/replay behavior

The examples below require _two_ imposters: the imposter with the proxy response
(which is what the chapter is about), and the origin server that we're proxying to.
If you look at the JSON files, the origin server is listed first on port 8080 and
the proxy imposter second on port 3000.

## Listing 5.1 and 5.2: Setting up a basic proxy to query inventory

````
mb restart --configfile examples/basic-inventory.json &

# Should respond with 54
curl -i http://localhost:3000/inventory/2599b7f4

# Should still respond 54, even though the downstream service would return 21
curl -i http://localhost:3000/inventory/2599b7f4

# Shows the changed state of the imposter
curl -i http://localhost:2525/imposters/3000

# Returns 21, showing difference between proxy and downstream service
curl -i http://localhost:8080/inventory/2599b7f4

mb stop
````

## Listing 5.3: Using a path predicate generator

````
mb restart --configfile examples/path-predicate.json &

# Should respond with 54
curl -i http://localhost:3000/inventory/2599b7f4

# Should respond with 100
curl -i http://localhost:3000/inventory/e1977c9e

# Shows the changed state of the imposter
curl -i http://localhost:2525/imposters/3000

mb stop
````

## Example: Using a path and page query predicate generator

````
mb restart --configfile examples/path-and-query-predicate.json &

# Should respond with "page 1"
curl -i http://localhost:3000/inventory/2599b7f4?page=1

# Should respond with "not page 1"
curl -i http://localhost:3000/inventory/2599b7f4?page=2

# Shows the changed state of the imposter
curl -i http://localhost:2525/imposters/3000

mb stop
````

## Listing 5.5: Combining a case-sensitive and case-insensitive parameter

````
mb restart --configfile examples/caseSensitive-predicateGenerators.json &

# We're sending "Hello, world!" in the body to path /hello
# The proxy is set to echo back the results, so the body should be:
# "You called /hello with a body of Hello, world!"
curl -i -d'Hello, world!' http://localhost:3000/hello

# Call it again with a different case of the body. The response will be the same:
# "You called /hello with a body of Hello, world!"
curl -i -d'HELLO, WORLD!' http://localhost:3000/hello

# Now call it with a different case of the path. The response will reflect the
# change, because the predicate on path is case-sensitive
# "You called /HELLO with a body of Hello, world!"
curl -i -d'Hello, world!' http://localhost:3000/HELLO

# Shows the changed state of the imposter
curl -i http://localhost:2525/imposters/3000

mb stop
````

## Listing 5.6: Generating a JSONPath predicate
````
mb restart --configfile examples/jsonpath.json &

# Send a request with last career = "Doctor"
# Should show "First response" in body
curl -i -X PUT http://localhost:3000/identities --data '{
  "identities": [
    {
      "name": "Frank Adams",
      "career": "Teacher",
      "location": "Utah"
    },
    {
      "name": "Frank Williams",
      "career": "Doctor",
      "location": "Georgia"
    }
  ]
}'

# Send the same request
# Should show "First response" in body, proving it saved the results
curl -i -X PUT http://localhost:3000/identities --data '{
  "identities": [
    {
      "name": "Frank Adams",
      "career": "Teacher",
      "location": "Utah"
    },
    {
      "name": "Frank Williams",
      "career": "Doctor",
      "location": "Georgia"
    }
  ]
}'

# Send a request with last career = "Teacher"
# Should show "Second response" in body
curl -i -X PUT http://localhost:3000/identities --data '{
  "identities": [
    {
      "name": "Frank Williams",
      "career": "Doctor",
      "location": "Georgia"
    },
    {
      "name": "Frank Adams",
      "career": "Teacher",
      "location": "Utah"
    }
  ]
}'

# Shows the changed state of the imposter
curl -i http://localhost:2525/imposters/3000

mb stop
````

## Listing 5.7: Generating an XPath predicate
````
mb restart --configfile examples/xpath.json &

# Send a request to generate the predicate (element has career = Teacher and location = Utah)
curl -i -X PUT http://localhost:3000/identities --data '
<identities>
  <identity career="Doctor">
    <name>Frank Williams</name>
    <location>Georgia</location>
  </identity>
  <identity career="Teacher">
    <name>Frank Adams</name>
    <location>Utah</location>
  </identity>
</identities>'

# Shows the changed state of the imposter
curl -i http://localhost:2525/imposters/3000

mb stop
````

## Listing 5.8-5.9: Using a proxyAlways proxy mode

````
mb restart --configfile examples/proxyAlways.json &

# Should respond with 54
curl -i http://localhost:3000/inventory/2599b7f4

# Different product, should respond with 100
curl -i http://localhost:3000/inventory/e1977c9e

# Should respond with 21
curl -i http://localhost:3000/inventory/2599b7f4

# Should respond with 0
curl -i http://localhost:3000/inventory/2599b7f4

# Shows the changed state of the imposter
curl -i http://localhost:2525/imposters/3000

mb stop
````

## Listing 5.10: Simple replay

````
mb restart --configfile examples/proxyAlways.json &

# Should respond with 54
curl -i http://localhost:3000/inventory/2599b7f4

# Different product, should respond with 100
curl -i http://localhost:3000/inventory/e1977c9e

# Should respond with 21
curl -i http://localhost:3000/inventory/2599b7f4

# Should respond with 0
curl -i http://localhost:3000/inventory/2599b7f4

# Restart mountebank without the proxies
mb replay

# Shows the changed state of the imposter
curl -i http://localhost:2525/imposters/3000

mb stop
````

## Listing 5.15: Using a partial proxy
````
mb restart --configfile examples/partialProxy.json &

# Should respond with 400 error code
curl -i -d'cc=5555555555555555' http://localhost:3000/400-code-path

# Should repond with a 500 error code
curl -i -d'cc=4444444444444444' http://localhost:3000/500-code-path

# Should respond proxied message
curl -i -d'cc=3333333333333333' http://localhost:3000/something-else

# Should respond with another proxied message
curl -i -d'cc=2222222222222222' http://localhost:3000/something-else

# In this scenario, you'd never use mb replay

mb stop
````

## Listing 5.16: Bridging HTTPS to HTTP

````
mb restart --configfile examples/https-to-http.json &

# The HTTPS imposter uses a self-signed certificate
# Calling the HTTP imposter doesn't require us to use
# the -k flag for curl, which disables certificate validation
curl -i http://localhost:3000/

mb stop
````
