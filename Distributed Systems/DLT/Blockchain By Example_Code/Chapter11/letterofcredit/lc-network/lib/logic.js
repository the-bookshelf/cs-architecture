/*
 * Blockchain by example - LC
 * Author; Brian Wu
 */


'use strict';
/**
 * Write your transction processor functions here
 */

 /**
  * Create the LC asset
  * @param {org.example.lc.InitialApplication} initalAppliation - the InitialApplication transaction
  * @transaction
  */
 async function initialApplication(application) { // eslint-disable-line no-unused-vars
     const factory = getFactory();
     const namespace = 'org.example.lc';

     const letter = factory.newResource(namespace, 'LetterOfCredit', application.letterId);
     letter.buyer = factory.newRelationship(namespace, 'User', application.buyer.getIdentifier());
     letter.seller = factory.newRelationship(namespace, 'User', application.seller.getIdentifier());
     letter.issuingBank = factory.newRelationship(namespace, 'Bank', application.buyer.bank.getIdentifier());
     letter.confirmingBank = factory.newRelationship(namespace, 'Bank', application.seller.bank.getIdentifier());
     letter.rules = application.rules;
     letter.productDetails = application.productDetails;
     letter.evidence = [];
     letter.status = 'CONTRACT';
     letter.step=0;
     //save the application
     const assetRegistry = await getAssetRegistry(letter.getFullyQualifiedType());
     await assetRegistry.add(letter);

     // emit event
     const applicationEvent = factory.newEvent(namespace, 'InitialApplicationEvent');
     applicationEvent.lc = letter;
     emit(applicationEvent);
 }

 /**
  * Buyer submit LC requst to issuing bank
  * @param {org.example.lc.BuyerRequestLC} buyerLCRequest - the Buyer request LC transaction
  * @transaction
  */
 async function buyerLCRequest(request) { // eslint-disable-line no-unused-vars
     const factory = getFactory();
     const namespace = 'org.example.lc';

     let letter = request.lc;

     if (letter.status === 'CLOSED') {
         throw new Error ('This letter of credit has already been closed');
     } else if (letter.step!== 0) {
         throw new Error ('This letter of credit should be in step 0 - CONTRACT');
     }
     letter.status = 'REQUEST_LC';
     letter.step=1;

     const assetRegistry = await getAssetRegistry(request.lc.getFullyQualifiedType());
     await assetRegistry.update(letter);

     // emit event
     const buyerRequestLCEvent = factory.newEvent(namespace, 'BuyerRequestLCEvent');
     buyerRequestLCEvent.lc = letter;
     emit(buyerRequestLCEvent);
 }

 /**
  * issuing bank approval buyer LC
  * @param {org.example.lc.IssuingBankApproveLC} issuingBankApproveLC - Issuing Bank approval LC transaction
  * @transaction
  */
 async function issuingBankApproveLC(request) { // eslint-disable-line no-unused-vars
     const factory = getFactory();
     const namespace = 'org.example.lc';

     let letter = request.lc;

     if (letter.status === 'CLOSED') {
         throw new Error ('This letter of credit has already been closed');
     } else if (letter.step!== 1) {
         throw new Error ('This letter of credit should be in step 1 - REQUEST_LC');
     }
     letter.status = 'ISSUE_LC';
     letter.step=2;

     const assetRegistry = await getAssetRegistry(request.lc.getFullyQualifiedType());
     await assetRegistry.update(letter);

     // emit event
     const issuingBankApproveLCEvent = factory.newEvent(namespace, 'IssuingBankApproveLCEvent');
     issuingBankApproveLCEvent.lc = letter;
     emit(issuingBankApproveLCEvent);
 }
 /**
  * confirming bank approval LC
  * @param {org.example.lc.ConfirmingBankAdviceLC} confirmingBankAdviceLC - confirming bank advice LC transaction
  * @transaction
  */
 async function confirmingBankAdviceLC(request) { // eslint-disable-line no-unused-vars
     const factory = getFactory();
     const namespace = 'org.example.lc';

     let letter = request.lc;

     if (letter.status === 'CLOSED') {
         throw new Error ('This letter of credit has already been closed');
     } else if (letter.step!== 2) {
         throw new Error ('This letter of credit should be in step 2 - ISSUE_LC');
     }
     letter.status = 'ADVICE_LC';
     letter.step=3;

     const assetRegistry = await getAssetRegistry(request.lc.getFullyQualifiedType());
     await assetRegistry.update(letter);

     // emit event
     const confirmingBankAdviceLCEvent = factory.newEvent(namespace, 'ConfirmingBankAdviceLCEvent');
     confirmingBankAdviceLCEvent.lc = letter;
     emit(confirmingBankAdviceLCEvent);
 }
 /**
  * seller deliver product
  * @param {org.example.lc.SellerDeliverGoods} sellerDeliverGoods - seller deliver product
  * @transaction
  */
 async function sellerDeliverGoods(request) { // eslint-disable-line no-unused-vars
     const factory = getFactory();
     const namespace = 'org.example.lc';

     let letter = request.lc;

     if (letter.status === 'CLOSED') {
         throw new Error ('This letter of credit has already been closed');
     } else if (letter.step!== 3) {
         throw new Error ('This letter of credit should be in step 3 - ADVICE_LC');
     }
     letter.status = 'DELIVER_PRODUCT';
     letter.step=4;
     letter.evidence.push(request.evidence);
     const assetRegistry = await getAssetRegistry(request.lc.getFullyQualifiedType());
     await assetRegistry.update(letter);

     // emit event
     const sellerDeliverGoodsEvent = factory.newEvent(namespace, 'SellerDeliverGoodsEvent');
     sellerDeliverGoodsEvent.lc = letter;
     emit(sellerDeliverGoodsEvent);
 }
 /**
  * seller Presentation the Document
  * @param {org.example.lc.SellerPresentDocument} sellerPresentDocument - seller present document
  * @transaction
  */
 async function sellerPresentDocument(request) { // eslint-disable-line no-unused-vars
     const factory = getFactory();
     const namespace = 'org.example.lc';

     let letter = request.lc;

     if (letter.status === 'CLOSED') {
         throw new Error ('This letter of credit has already been closed');
     } else if (letter.step!== 4) {
         throw new Error ('This letter of credit should be in step 4 - ADVICE_LC');
     }
     letter.status = 'PRESENT_DOCUMENT';
     letter.step=5;
     letter.evidence.push(request.evidence);
     const assetRegistry = await getAssetRegistry(request.lc.getFullyQualifiedType());
     await assetRegistry.update(letter);

     // emit event
     const sellerPresentDocumentEvent = factory.newEvent(namespace, 'SellerPresentDocumentEvent');
     sellerPresentDocumentEvent.lc = letter;
     emit(sellerPresentDocumentEvent);
 }
 /**
  * seller deliver product
  * @param {org.example.lc.ConfirmingBankDeliverDocument} confirmingBankDeliverDocument - seller deliver product
  * @transaction
  */
 async function confirmingBankDeliverDocument(request) { // eslint-disable-line no-unused-vars
     const factory = getFactory();
     const namespace = 'org.example.lc';

     let letter = request.lc;

     if (letter.status === 'CLOSED') {
         throw new Error ('This letter of credit has already been closed');
     } else if (letter.step!== 5) {
         throw new Error ('This letter of credit should be in step 5 - PRESENT_DOCUMENT');
     }
     letter.status = 'DELIVERY_DOCUMENT';
     letter.step=6;
     letter.evidence.push(request.evidence);
     const assetRegistry = await getAssetRegistry(request.lc.getFullyQualifiedType());
     await assetRegistry.update(letter);

     // emit event
     const confirmingBankDeliverDocumentEvent = factory.newEvent(namespace, 'ConfirmingBankDeliverDocumentEvent');
     confirmingBankDeliverDocumentEvent.lc = letter;
     emit(confirmingBankDeliverDocumentEvent);
 }
 /**
  * buyer Deposit Payment
  * @param {org.example.lc.BuyerDepositPayment} buyerDepositPayment - buyer Deposit Payment
  * @transaction
  */
 async function buyerDepositPayment(request) { // eslint-disable-line no-unused-vars
     const factory = getFactory();
     const namespace = 'org.example.lc';

     let letter = request.lc;

     if (letter.status === 'CLOSED') {
         throw new Error ('This letter of credit has already been closed');
     } else if (letter.step!== 6) {
         throw new Error ('This letter of credit should be in step 6 - DELIVERY_DOCUMENT');
     }
     letter.status = 'BUYER_DEBIT_PAYMENT';
     letter.step=7;

     const assetRegistry = await getAssetRegistry(request.lc.getFullyQualifiedType());
     await assetRegistry.update(letter);

     // emit event
     const buyerDepositPaymentEvent = factory.newEvent(namespace, 'BuyerDepositPaymentEvent');
     buyerDepositPaymentEvent.lc = letter;
     emit(buyerDepositPaymentEvent);
 }
 /**
  * banks Transfer Payment
  * @param {org.example.lc.BanksTransferPayment} banksTransferPayment - banks Transfer Payment
  * @transaction
  */
 async function banksTransferPayment(request) { // eslint-disable-line no-unused-vars
     const factory = getFactory();
     const namespace = 'org.example.lc';

     let letter = request.lc;

     if (letter.status === 'CLOSED') {
         throw new Error ('This letter of credit has already been closed');
     } else if (letter.step!== 7) {
         throw new Error ('This letter of credit should be in step 6 - BUYER_DEBIT_PAYMENT');
     }
     letter.status = 'BANKS_PAYMENT_TRANSFER';
     letter.step=8;

     const assetRegistry = await getAssetRegistry(request.lc.getFullyQualifiedType());
     await assetRegistry.update(letter);

     // emit event
     const banksTransferPaymentEvent = factory.newEvent(namespace, 'BanksTransferPaymentEvent');
     banksTransferPaymentEvent.lc = letter;
     emit(banksTransferPaymentEvent);
 }
 /**
  * seller Received Payment
  * @param {org.example.lc.SellerReceivedPayment} sellerReceivedPayment - seller Received Payment
  * @transaction
  */
 async function sellerReceivedPayment(request) { // eslint-disable-line no-unused-vars
     const factory = getFactory();
     const namespace = 'org.example.lc';

     let letter = request.lc;

     if (letter.status === 'CLOSED') {
         throw new Error ('This letter of credit has already been closed');
     } else if (letter.step!== 8) {
         throw new Error ('This letter of credit should be in step 6 - BANKS_PAYMENT_TRANSFER');
     }
     letter.status = 'SELL_RECEIVED_PAYMENT';
     letter.step=9;

     const assetRegistry = await getAssetRegistry(request.lc.getFullyQualifiedType());
     await assetRegistry.update(letter);

     // emit event
     const sellerReceivedPaymentEvent = factory.newEvent(namespace, 'SellerReceivedPaymentEvent');
     sellerReceivedPaymentEvent.lc = letter;
     emit(sellerReceivedPaymentEvent);
 }
 /**
  * Close the LOC
  * @param {org.example.lc.Close} close - the Close transaction
  * @transaction
  */
 async function close(closeRequest) { // eslint-disable-line no-unused-vars
     const factory = getFactory();
     const namespace = 'org.example.lc';

     let letter = closeRequest.lc;

     if (letter.status === 'SELL_RECEIVED_PAYMENT') {
         letter.status = 'CLOSED';
         letter.closeReason = closeRequest.closeReason;

         // update the status of the lc
         const assetRegistry = await getAssetRegistry(closeRequest.lc.getFullyQualifiedType());
         await assetRegistry.update(letter);

         // emit event
         const closeEvent = factory.newEvent(namespace, 'CloseEvent');
         closeEvent.lc = closeRequest.lc;
         closeEvent.closeReason = closeRequest.closeReason;
         emit(closeEvent);
     } else if (letter.status === 'CLOSED') {
         throw new Error('This letter of credit has already been closed');
     } else {
         throw new Error('Cannot close this letter of credit');
     }
 }
 /**
  * Create the participants needed for the demo
  * @param {org.example.lc.CreateDemoParticipants} createDemoParticipants - the CreateDemoParticipants transaction
  * @transaction
  */
 async function createDemoParticipants() { // eslint-disable-line no-unused-vars
     const factory = getFactory();
     const namespace = 'org.example.lc';

     // create the banks
     const bankRegistry = await getParticipantRegistry(namespace + '.Bank');
     const issuingbank = factory.newResource(namespace, 'Bank', 'BI');
     issuingbank.name = 'First Consumer Bank';
     issuingbank.type = 'ISSUING_BANK';
     await bankRegistry.add(issuingbank);
     const confirmingbank = factory.newResource(namespace, 'Bank', 'BE');
     confirmingbank.name = 'Bank of Eastern Export';
     confirmingbank.type = 'CONFIRMING_BANK';
     await bankRegistry.add(confirmingbank);


     // create users
     const userRegistry = await getParticipantRegistry(namespace + '.User');
     const buyer = factory.newResource(namespace, 'User', 'david');
     buyer.name = 'David';
     buyer.lastName= 'Wilson';
     buyer.bank = factory.newRelationship(namespace, 'Bank', 'BI');
     buyer.companyName = 'Toy Mart Inc';
     buyer.type = 'BUYER';
     await userRegistry.add(buyer);
     const seller = factory.newResource(namespace, 'User', 'jason');
     seller.name = 'Jason';
     seller.lastName= 'Jones';
     seller.bank = factory.newRelationship(namespace, 'Bank', 'EB');
     seller.companyName = 'Valley Toys Manufacturing';
     seller.type = 'SELLER';
     await userRegistry.add(seller);

 }
