# Chapter 2: Your first test using mountebank

## Listing 2.1: Creating a products imposter

The imposter JSON is saved in examples/products.json. The `curl` command allows you to
POST data inside a file using the `-d@filename` flag. We'll show an example that does the
following:

* Starts mountebank, waiting for it to fully initialize
* Creates the imposter
* Sends a sample request to the imposter
* Stops mountebank

The bash script looks like this:

````bash
# Start mountebank in the background
mb &

# Wait for mb to fully initialize by waiting for it to create the pidfile
while [ ! -f mb.pid ]; do
  sleep 1
done

# Create imposter
curl -d@examples/products.json http://localhost:2525/imposters

# Call the imposter
curl -i http://localhost:3000/products

# Stop mountebank
mb stop
````

The Windows shells aren't as friendly towards streaming text as the \*nix shells, so the
output may not look as nice, but PowerShell is nonetheless a very capable alternative.
Starting process in the background is possible but a bit clumsy, so I suggest you use
a separate terminal to start mountebank:

````
mb
````

Creating the imposter, sending it a sample request, and stopping mountebank can occur
in the second PowerShell terminal:

````
# Create the imposters
Invoke-RestMethod -Method POST -Uri http://localhost:2525/imposters -InFile examples/products.json

# Call the imposter
Invoke-RestMethod -Method GET -Uri http://localhost:3000/

# Stop mountebank
mb stop
````

Run the website with `npm run start`. Run the tests with `npm run test`. The
tests assume mountebank is already running.

## Listing 2.2: Creating a products imposter with predicates

The imposter JSON is saved in examples/productsWithPredicates.json. Due to the added complexity
of this example, we've added a step to the script. It now:

* Starts mountebank, waiting for it to fully initialize
* Creates the imposter
* Sends a sample request to the imposter for page 2 (should get an empty product list)
* Sends a sample request to the imposter without a page query parameter
* Stops mountebank

The bash script looks like this:

````bash
# Start mountebank in the background
mb &

# Wait for mb to fully initialize by waiting for it to create the pidfile
while [ ! -f mb.pid ]; do
  sleep 1
done

# Create imposter
curl -d@examples/productsWithPredicates.json http://localhost:2525/imposters

# Call the imposter with page=2, should get an empty product list
curl -i http://localhost:3000/products?page=2

# Call the imposter, should get a full body
curl -i http://localhost:3000/products

# Stop mountebank
mb stop
````

The Windows example, assuming mountebank is running in a separate terminal, is similar:

````
# Create the imposters
Invoke-RestMethod -Method POST -Uri http://localhost:2525/imposters -InFile examples/productsWithPredicates.json

# Call the imposter for page 2
Invoke-RestMethod -Method GET -Uri http://localhost:3000/?page=2

# Call the imposter
Invoke-RestMethod -Method GET -Uri http://localhost:3000/
````

# Listings 2.4 - 2.7, Using mountebank in an automated JavaScript test

The code to run the automated JavaScript test is in the `test` directory. It tests a very simple
Web Facade service that represents the system under test in our example. That code resides in
`src/index.js`.

The first time you download the source, you need to install the dependencies:

````
npm install
````

Then you need to start the Web Facade service that we're testing:

````
npm start
````

Finally, you can run the tests:

````
npm test
````
