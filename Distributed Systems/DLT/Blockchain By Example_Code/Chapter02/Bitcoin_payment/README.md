# Bitcoin_payment
Code for the first section of the second Chapter.

## Instructions
 
Detailed instructions are available on the book. To run the code please follow the instruction below :

in ``\root`` folder run:
``npm install``

in ``\view`` directory run:
``bower install``

then in \root folder run: ``npm start``

-http and https ports and IP will be displayed in your terminal.

## Potential issues: 
>>>>>>> 780469a147cf6f668806620192046e7c6c2af947

**issue 1**: More than one instance of bitcore-lib found. Please make sure to require bitcore-lib and check that submodules do not also include their own bitcore-lib dependency.


**Solution** : edit node_modules/bitcore-payment-protocol/node_modules/bitcore-lib/index.js


```bitcore.versionGuard = function(version) {
  /*if (version !== undefined) {
    var message = 'More than one instance of bitcore-lib found. ' + 
      'Please make sure to require bitcore-lib and check that submodules do' +
      ' not also include their own bitcore-lib dependency.';
    throw new Error(message);
  }*/
};```


**issue 2**: Cannot find module '../..' from '/home/user/deletme/Bitcoin_payment/node_modules/asn1.js-rfc5280'

**solution** : edit node_modules/asn1.js-rfc5280/index.js
Change: var asn1 = require('../..'); to  var asn1 = require('../' + '..');

**issue3** There is an issue with Firefox 
Error for ReferenceError: $ is not defined[Learn More]

Cause : Firefox doesn't trust the link provided for Jquery!!


