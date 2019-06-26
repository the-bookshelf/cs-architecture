# Chapter 4: Using predicates to give different responses

These examples assume you have access to `curl`, which means you're running Mac, Linux,
or cygwin on Windows. For the Powershell examples, see the example README in
[chapter 2](https://github.com/bbyars/mountebank-in-action/tree/master/ch02).

## Listing 4.1: The basic Abagnale imposter - giving different answers to different questions

````
mb restart --configfile examples/abagnale.json &

# Should respond Miami to Rio
curl -i -d"Which route are you flying?" http://localhost:3000/

# Should respond Harvard Medical School
curl -i -d"Where did you get your degree?" http://localhost:3000/

# Should respond I'll have to get back to you
curl -i -d"What's your favorite color?" http://localhost:3000/

mb stop
````

## Figure 4.4: Using a regular expression to match past and present tense questions

````
mb restart --configfile examples/matchesBody.json &

# Both should respond Miami to Rio
curl -i -d"Which route did you fly?" http://localhost:3000/
curl -i -d"Which route are you flying?" http://localhost:3000/

mb stop
````

## Listing 4.2: Using a regular expression to replace other predicate types

````
mb restart --configfile examples/matchesSuspiciousQuestion.json &

# Should all respond "Catch me if you can!"
curl -i -d"Can I see your driver's license?" http://localhost:3000/
curl -i -d"Can I see your state driver's license?" http://localhost:3000/
curl -i -d"Can I see your current driver's license please?" http://localhost:3000/

mb stop
````

## Listing 4.3: Matching a numeric id on the path

````
mb restart --configfile examples/matchesPath.json &

# Should respond with JSON
curl -i http://localhost:3000/identities/123

# Should respond with default empty response because id is expected to be numeric
curl -i http://localhost:3000/identities/frank-williams

mb stop
````

## Listing 4.4: Matching a query parameter

````
mb restart --configfile examples/matchesQuery.json &

# Should respond with JSON
curl -i http://localhost:3000/identities?q=Frank

mb stop
````

## Example: Matching only if nothing is on the querystring

````
mb restart --configfile examples/emptyQuery.json &

# Should respond with default empty response
curl -i http://localhost:3000/identities?q=Frank

# Should respond with JSON
curl -i http://localhost:3000/identities

mb stop
````

## Listing 4.5: Matching arrays

````
mb restart --configfile examples/arrays.json &

# Should match equals predicate
curl -i "http://localhost:3000/identities?q=Frank&q=Georgia&q=Doctor"

# Should match deepEquals predicate
curl -i "http://localhost:3000/identities?q=Frank&q=Georgia"

mb stop
````

## Example: Using `exists` to test the querystring

````
mb restart --configfile examples/exists.json &

# Should respond with test body
curl -i "http://localhost:3000/identities?q=Frank"

# Should respond with default empty body because the page parameter is passed
curl -i "http://localhost:3000/identities?q=Frank&page=2"

mb stop
````

## Example: Using `exists` to test for a missing Authorization header

````
mb restart --configfile examples/authorization.json &

# The first request should return a default 200
curl -i -H'Authorization: letmein' http://localhost:3000/

# The second request should return a 401
curl -i http://localhost:3000/

mb stop
````

## Example: Using the and predicate

````
mb restart --configfile examples/andPredicate.json &

# Should respond with test body
curl -i -d'Frank Abagnale' http://localhost:3000

# Should respond with default empty body
curl -i -d'Frank Abagnale Jr.' http://localhost:3000

mb stop
````

## Listing 4.6: Combining ands, ors, and nots

````
mb restart --configfile examples/complex.json &

# Should respond with test body
curl -i "http://localhost:3000/identities?q=Frank+Williams"

# Should respond with test body
curl -i http://localhost:3000/identities?q=Frank&q=Williams

# Should respond with test body
curl -i http://localhost:3000/identities/123

# Should respond with empty default body
curl -i "http://localhost:3000/identities?q=Frank+Williams&page=2"

mb stop
````

## Listing 4.7: Case-sensitive predicate

````
mb restart --configfile examples/caseSensitive.json &

# Should respond with test body
curl -i "http://localhost:3000/identities?q=Frank"

# Should respond with default body
curl -i "http://localhost:3000/identities?q=frank"

# Should respond with default body
curl -i "http://localhost:3000/identities?Q=Frank"

mb stop
````

## Listing 4.8: JSON predicate

````
mb restart --configfile examples/jsonPredicate.json &

# Should send test response (career = Doctor)
curl -i -d'{ "name": "Frank Williams", "career": "Doctor", "location": "Georgia"}' http://localhost:3000/identities

# Should send default response (career = Teacher)
curl -i -d'{ "name": "Frank Adams", "career": "Teacher", "location": "Utah"}' http://localhost:3000/identities

mb stop
````

## Listing 4.9: JSONPath predicate

````
mb restart --configfile examples/jsonpath.json &

# Should send 200 status code (last element does not have career = Teacher)
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

# Should send 400 status code (last element has career = Teacher)
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

mb stop
````

## Listing 4.10: Simple XPath predicate

````
mb restart --configfile examples/xpath.json &

# Should send 200 status code (no element has career = Teacher and location = Utah)
curl -i -X PUT http://localhost:3000/identities --data '
<identities>
  <identity career="Doctor">
    <name>Frank Williams</name>
    <location>Georgia</location>
  </identity>
</identities>'

# Should send 400 status code (element has career = Teacher and location = Utah)
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

mb stop
````

## Listing 4.11: XPath with namespaces

````
mb restart --configfile examples/xpath-ns.json &

# Should send 200 status code (sending n:name as attribute)
curl -i -X PUT http://localhost:3000/identities --data '
<identities xmlns:id="https://www.abagnale-spec.com/identity"
            xmlns:n="https://www.abagnale-spec.com/name">
  <id:identity career="Doctor" n:name="Frank Williams">
    <location>Georgia</location>
  </id:identity>
  <id:identity career="Teacher" n:name="Frank Adams">
    <location>Utah</location>
  </id:identity>
</identities>'

# Should send 400 status code (sending n:name as tag)
curl -i -X PUT http://localhost:3000/identities --data '
<identities xmlns:id="https://www.abagnale-spec.com/identity"
            xmlns:n="https://www.abagnale-spec.com/name">
  <id:identity career="Doctor">
    <n:name>Frank Williams</n:name>
    <location>Georgia</location>
  </id:identity>
  <id:identity career="Teacher">
    <n:name>Frank Adams</n:name>
    <location>Utah</location>
  </id:identity>
</identities>'

# Should send 400 status code (sending n:name as attribute and tag)
curl -i -X PUT http://localhost:3000/identities --data '
<identities xmlns:id="https://www.abagnale-spec.com/identity"
            xmlns:n="https://www.abagnale-spec.com/name">
  <id:identity career="Doctor" n:name="Frank Williams">
    <n:name>Frank Williams</n:name>
    <location>Georgia</location>
  </id:identity>
  <id:identity career="Teacher" n:name="Frank Adams">
    <n:name>Frank Adams</n:name>
    <location>Utah</location>
  </id:identity>
</identities>'

mb stop
````
