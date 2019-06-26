# Chapter 9: Mountebank and Continuous Delivery

## Running the example services

First we need to install all dependencies:

````
(cd productService && npm install)
(cd contentService && npm install)
(cd webFacadeService && npm install)
````

Then to run them all at once:

````
export PRODUCT_SERVICE_URL='http://localhost:3000'
export CONTENT_SERVICE_URL='http://localhost:4000'
(cd productService && npm start &)
(cd contentService && npm start &)
(cd webFacadeService && npm start &)
````

I use a blunt instrument to kill them all:

````
killall node
````

## Running the unit tests

We have an npm task set up for the purpose. The parenthesis around the script
runs in a subshell so that we can `cd` for the script execution without changing
your directory once the script finishes.

````
(
cd webFacadeService
npm install
npm test
)
````

## Running the service tests

First you need the webFacadeService running:

````
cd webFacadeService
export PRODUCT_SERVICE_URL='http://localhost:3000'
export CONTENT_SERVICE_URL='http://localhost:4000'
npm start
````

In a different console, you can run the service tests as shown. The task will automatically
start and stop mountebank:

````
(
cd webFacadeService
npm test test:service
)
````

## Running the contract tests

Here we need the Product and Content services running, but not the webFacade:

````
export PRODUCT_SERVICE_URL='http://localhost:3000'
export CONTENT_SERVICE_URL='http://localhost:4000'
(cd productService && npm start &)
(cd contentService && npm start &)
````

In a different console:

````
(
cd webFacadeService
npm test test:contract
)
````
