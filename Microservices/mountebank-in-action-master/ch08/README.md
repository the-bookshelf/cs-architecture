# Chapter 8: Protocols

The examples below assume you have netcat (`nc`) installed on your machine.
Netcat is like telnet, but easier to script.

## Listing 8.1-8.2: Virtualizing a TCP updateInventory call

````
mb restart --configfile examples/updateInventory.json &

# Should respond with "0\n1343"
echo "updateInventory\na32fbd\n-5" | nc localhost 3000

mb stop
````

## Listing 8.3: Using a TCP proxy

````
mb restart --configfile examples/textProxy.json &

# Should respond with "0\n1343"
# Note the -q 1 flag, which unfortunately doesn't work on my Mac but does on Linux
# It prevents netcat from prematurely closing the connection while mountebank negotiates the proxy
# You can leave it off and the first call will fail (by the time mb tries to respond, the
# nc client will have closed the connection), but the subsequent calls will work because
# mountebank saved the response
echo "updateInventory\n5131\n-5" | nc -q 1 localhost 3000

# Should respond with "0\n1343" (proxy serving saved response)
echo "updateInventory\n5131\n-5" | nc localhost 3000

# Should respond with "0\n0" if you go to the origin server
echo "updateInventory\n5131\n-5" | nc localhost 3333

mb stop
````

## Listing 8.4: Using a TCP proxy with predicate generators

````
mb restart --configfile examples/proxyWithPredicateGenerators.json &

# Should respond with "0\n1343"
echo "updateInventory\n5131\n-5" | nc -q 1 localhost 3000

# Should respond with "0\n1343" (saved response, no -q needed)
echo "updateInventory\n5131\n-5" | nc localhost 3000

# Should respond with "0\0"
echo "updateInventory\n5040\n-5" | nc -q 1 localhost 3000

# Should respond with "0\0" (saved response, no -q needed)
echo "updateInventory\n5040\n-5" | nc localhost 3000

# See the generated stubs
curl http://localhost:2525/imposters/3000

mb stop
````

## Listing 8.5: Using a TCP proxy with predicate generators with an XML payload

````
mb restart --configfile examples/proxyWithPredicateGeneratorsXML.json &

# Write first test data
cat <<EOF > mbtemp
<functionCall>
  <functionName>updateInventory</functionName>
  <parameters>
    <parameter name="productId" value="5131" />
    <parameter name="amount" value="-5" />
  </parameters>
</functionCall>
EOF

# Should respond with 1343 inventory
cat mbtemp | nc -q 1 localhost 3000

# Should respond with 1343 (saved response, no -q needed)
cat mbtemp | nc localhost 3000

# Write second test data
cat <<EOF > mbtemp
<functionCall>
  <functionName>getInventory</functionName>
  <parameters>
    <parameter name="productId" value="5040" />
  </parameters>
</functionCall>
EOF

# Should respond with 0 inventory
cat mbtemp | nc -q 1 localhost 3000

# Should respond with 0 (saved response, no -q needed)
cat mbtemp | nc localhost 3000

rm mbtemp

# Show new stubs
curl http://localhost:2525/imposters/3000

mb stop
````

## Listing 8.6 - 8.7: Converting to Base64 and back

````
# Encodes in base64
node -e 'console.log(new Buffer("Hello, world!").toString("base64"));'

# Decodes from base64
node -e 'console.log(new Buffer("SGVsbG8sIHdvcmxkIQ==", "base64").toString("utf8"));'
````

## Listing 8.8: Configuring mountebank for a binary response

````
mb --configfile examples/binaryResponse.json &

# Returns "Hello, world!" to the console
echo "Testing" | nc localhost 3000

mb stop
````

## Listing 8.9: Using a Base64-encoded contains predicate

````
mb restart --configfile examples/binaryContains.json &

# Should return 'Did not match' since it doesn't contain 0x49 0x11
node -e 'console.log(new Buffer([0x10, 0x49, 0x10]).toString("base64"));' | base64 --decode | nc localhost 3000

# Should return 'Matched' since it doesn't start with 0x49 0x11
node -e 'console.log(new Buffer([0x10, 0x49, 0x11]).toString("base64"));' | base64 --decode | nc localhost 3000

mb stop
````

## Virtualizing a .NET Remoting server

Running this example currently requires Visual Studio on a Windows machine. The source code for both the client
and the server is in src/TownCrier-DotNetRemoting. I've checked in the built
executables in src/TownCrier-DotNetRemoting/artifacts.

The tests use code in src/RemotingProtocolParser, which is a copy of the amazing work
[Xu Huang](https://github.com/wsky/RemotingProtocolParser) has done.

The example test we walk through in the book is src/TownCrier-DotNetRemoting/ClientTest/TownCrierGatewayTest.cs.
To run it, you'll first have to start mountebank in a separate console. I run NUnit by right-clicking inside the
file and selecting 'Run unit tests'


## Listing 8.14-8.16: Proxying to a .NET remoting server

This works on Windows. All three comamnds should be run in separate consoles. Note that, to generate a large
enough payload to require the `endOfRequestResolver`, I generated a bunch of random text, so don't be
surprised if a successful response looks weird.

````
src\TownCrier-DotNetRemoting\artifacts\Server.exe 3333
````

````
mb --configfile examples/proxyWithEndOfRequestResolver.json --allowInjection
````

````
REM Client.exe assumes you're running in the right directory
cd src\TownCrier-DotNetRemoting\artifacts
Client.exe 3000
````
