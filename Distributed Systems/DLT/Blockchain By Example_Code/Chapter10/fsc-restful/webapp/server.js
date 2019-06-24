const express = require('express')
const app = express()
app.set('view engine', 'ejs')

var Fabric_Client = require('fabric-client');
var Fabric_CA_Client = require('fabric-ca-client');
var path = require('path');
var util = require('util');
var os = require('os');

app.get('/', function (req, res) {
   res.render('index');
})
app.get('/queryChaincode',function(req,res){
		// setup the fabric network
		var fabric_client = new Fabric_Client();
		var channel = fabric_client.newChannel('mychannel');
		var peer = fabric_client.newPeer('grpc://localhost:7051');
		channel.addPeer(peer);
		var member_user = null;
		var store_path = path.join(__dirname, 'hfc-key-store');
		var tx_id = null;
		Fabric_Client.newDefaultKeyValueStore({ path: store_path
		}).then((state_store) => {
			fabric_client.setStateStore(state_store);
			var crypto_suite = Fabric_Client.newCryptoSuite();
			var crypto_store = Fabric_Client.newCryptoKeyStore({path: store_path});
			crypto_suite.setCryptoKeyStore(crypto_store);
			fabric_client.setCryptoSuite(crypto_suite);
			return fabric_client.getUserContext('user1', true);
		}).then((user_from_store) => {
			if (user_from_store && user_from_store.isEnrolled()) {
				console.log('Successfully loaded user1 from persistence');
				member_user = user_from_store;
			} else {
				throw new Error('Failed to get user1.... run registerUser.js');
			}
			const request = {
				chaincodeId: 'fsccc',
				fcn: 'query',
				args: ['order_001']
			};
			// send the query proposal to the peer
			return channel.queryByChaincode(request);
		}).then((query_responses) => {
			console.log("Query has completed, checking results");
			// query_responses could have more than one  results if there multiple peers were used as targets
			if (query_responses && query_responses.length == 1) {
				if (query_responses[0] instanceof Error) {
					console.error("error from query = ", query_responses[0]);
				} else {
					console.log("Response is ", query_responses[0].toString());
					//res.json(query_responses[0])
					res.send(JSON.stringify(query_responses[0].toString()));
				}
			} else {
				console.log("No payloads were returned from query");
			}
		}).catch((err) => {
			console.error('Failed to query successfully :: ' + err);
		});
});
app.get('/registerAdmin',function(req,res){
	var fabric_client = new Fabric_Client();
	var fabric_ca_client = null;
	var admin_user = null;
	var member_user = null;
	var store_path = path.join(__dirname, 'hfc-key-store');
	console.log(' Store path:'+store_path);
	Fabric_Client.newDefaultKeyValueStore({ path: store_path
	}).then((state_store) => {
		// assign the store to the fabric client
		fabric_client.setStateStore(state_store);
		var crypto_suite = Fabric_Client.newCryptoSuite();
		var crypto_store = Fabric_Client.newCryptoKeyStore({path: store_path});
		crypto_suite.setCryptoKeyStore(crypto_store);
		fabric_client.setCryptoSuite(crypto_suite);
		var	tlsOptions = {
			trustedRoots: [],
			verify: false
		};
		fabric_ca_client = new Fabric_CA_Client('http://localhost:7054', tlsOptions , 'ca.example.com', crypto_suite);
		return fabric_client.getUserContext('admin', true);
	}).then((user_from_store) => {
		if (user_from_store && user_from_store.isEnrolled()) {
			console.log('Successfully loaded admin from persistence');
			admin_user = user_from_store;
			return null;
		} else {
			// need to enroll it with CA server
			return fabric_ca_client.enroll({
			  enrollmentID: 'admin',
			  enrollmentSecret: 'adminpw'
			}).then((enrollment) => {
			  console.log('Successfully enrolled admin user "admin"');
			  return fabric_client.createUser(
				  {username: 'admin',
					  mspid: 'Org1MSP',
					  cryptoContent: { privateKeyPEM: enrollment.key.toBytes(), signedCertPEM: enrollment.certificate }
				  });
			}).then((user) => {
			  admin_user = user;
			  return fabric_client.setUserContext(admin_user);
			}).catch((err) => {
			  console.error('Failed to enroll and persist admin. Error: ' + err.stack ? err.stack : err);
			  throw new Error('Failed to enroll admin');
			});
		}
	}).then(() => {
		console.log('Assigned the admin user to the fabric client ::' + admin_user.toString());
		res.end("success");
	}).catch((err) => {
		console.error('Failed to enroll admin: ' + err);
	});
});	
app.get('/registerUser',function(req,res){
		var fabric_client = new Fabric_Client();
		var fabric_ca_client = null;
		var admin_user = null;
		var member_user = null;
		var store_path = path.join(__dirname, 'hfc-key-store');
		console.log(' Store path:'+store_path);
		Fabric_Client.newDefaultKeyValueStore({ path: store_path
		}).then((state_store) => {
			// assign the store to the fabric client
			fabric_client.setStateStore(state_store);
			var crypto_suite = Fabric_Client.newCryptoSuite();
			var crypto_store = Fabric_Client.newCryptoKeyStore({path: store_path});
			crypto_suite.setCryptoKeyStore(crypto_store);
			fabric_client.setCryptoSuite(crypto_suite);
			var	tlsOptions = {
				trustedRoots: [],
				verify: false
			};
			// be sure to change the http to https when the CA is running TLS enabled
			fabric_ca_client = new Fabric_CA_Client('http://localhost:7054', null , '', crypto_suite);

			// first check to see if the admin is already enrolled
			return fabric_client.getUserContext('admin', true);
		}).then((user_from_store) => {
			if (user_from_store && user_from_store.isEnrolled()) {
				console.log('Successfully loaded admin from persistence');
				admin_user = user_from_store;
			} else {
				throw new Error('Failed to get admin.... run enrollAdmin.js');
			}

			// at this point we should have the admin user
			// first need to register the user with the CA server
			return fabric_ca_client.register({enrollmentID: 'user1', affiliation: 'org1.department1',role: 'client'}, admin_user);
		}).then((secret) => {
			// next we need to enroll the user with CA server
			console.log('Successfully registered user1 - secret:'+ secret);

			return fabric_ca_client.enroll({enrollmentID: 'user1', enrollmentSecret: secret});
		}).then((enrollment) => {
		  console.log('Successfully enrolled member user "user1" ');
		  return fabric_client.createUser(
			 {username: 'user1',
			 mspid: 'Org1MSP',
			 cryptoContent: { privateKeyPEM: enrollment.key.toBytes(), signedCertPEM: enrollment.certificate }
			 });
		}).then((user) => {
			 member_user = user;

			 return fabric_client.setUserContext(member_user);
		}).then(()=>{
			 console.log('User1 was successfully registered and enrolled and is ready to intreact with the fabric network');
			 res.end("success");
		}).catch((err) => {
			console.error('Failed to register: ' + err);
			if(err.toString().indexOf('Authorization') > -1) {
				console.error('Authorization failures may be caused by having admin credentials from a previous CA instance.\n' +
				'Try again after deleting the contents of the store directory '+store_path);
			}
		});
});	
app.listen(3000, function () {
  console.log('Example app listening on port 3000!')
})
