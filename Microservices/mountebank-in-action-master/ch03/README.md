# Chapter 3: Testing using canned responses

Most of the examples assume mountebank is running on port 2525. You can start it in a
separate terminal by running

````
mb &
````

The examples also assume you have access to `curl`, which means you're running Mac, Linux,
or cygwin on Windows. For the Powershell examples, see the example README in
[chapter 2](https://github.com/bbyars/mountebank-in-action/tree/master/ch02).

## Listing 3.3: Hello, world!

The introductory hello world imposter configuration is stored in examples/helloWorld.json.
Test it out with the following:

````
# Create the imposter
curl -d@examples/helloWorld.json http://localhost:2525/imposters

# Verify the response
curl -i http://localhost:3000/any/path?query=does-not-matter

# Delete the imposter
curl -X DELETE http://localhost:2525/imposters
````

Change examples/helloWorld.json to examples/helloWorld2.json to only set the body in the
response, allowing the default response to merge in the other fields

## Listing 3.5-3.6: Changing the default response

The configuration is in examples/errorDefaults.json.

````
# Create the imposter
curl -d@examples/errorDefaults.json http://localhost:2525/imposters

# Verify the response
curl -i http://localhost:3000/

# Delete the imposter
curl -X DELETE http://localhost:2525/imposters
````

## Listing 3.7: Cycling through multiple responses

The configuration for the inventory example is in examples/inventory.json.

````
# Create the imposter
curl -d@examples/inventory.json http://localhost:2525/imposters

# Call the imposter 7 times to see it cycle through responses
for (( i = 0; i < 7; i++ )); do
  curl http://localhost:3000/
  echo
done

# Delete the imposter
curl -X DELETE http://localhost:2525/imposters
````

## Listing 3.8: Creating an HTTPS imposter

We'll create one with a self-signed certificate, which means we have to use the `-k`
switch in `curl`. The `-k` switch bypasses certificate validation. As indicated in the
book, this is not recommended.

````
# Create the imposter
curl -d@examples/httpsImposter.json http://localhost:2525/imposters

# Call the imposter; should be denied because the certificate is invalid
curl -i https://localhost:3000/

# Call the imposter bypassing certificate validation
curl -i -k https://localhost:3000/

# Delete the imposter
curl -X DELETE http://localhost:2525/imposters
````

## Listing 3.10: Saving the inventory service in config files

The config file is in the configfiles directory, with EJS references to the configfiles/ssl
directory. You'll need to kill mountebank (either by typing Ctrl-C in the terminal running it
or by typing `mb stop` in a separate terminal that's in the same directory.

````
# Restart mountebank using the config file
mb restart --configfile configfiles/inventory.ejs

# Call the imposter 7 times to see it cycle through responses
for (( i = 0; i < 7; i++ )); do
  curl -k https://localhost:3000/
  echo
done
````

## Listing 3.12: Saving multiple imposters in config files

The config file is in the configfiles directory, with EJS references to the configfiles/ssl
directory. Kill mountebank before running:

````
# Restart mountebank using the config file
mb restart --configfile configfiles/imposters.ejs

# Verify inventory imposter
curl -k -i https://localhost:3000/

# Verify product imposter
curl -k -i https://localhost:3001/products

# Verify content imposter
curl -k -i https://localhost:3002/content?ids=2599b7f4,e1977c9e
````
