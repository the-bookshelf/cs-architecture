/*
    (The MIT License)

    Copyright (C) 2012 wsky (wskyhx at gmail.com) and other contributors

    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


//Remoting server do extend ServerFormatterSink 
//using json serializer 
//in this demo

var parser 				= require('./lib/remotingProtocolParser'),
	tcpWriter 			= parser.tcpWriter,
	tcpReader 			= parser.tcpReader,
	flags 				= parser.Flags(),
	TcpOperations 		= flags.TcpOperations,
	TcpTransportHeader 	= flags.TcpTransportHeader,

	client 				= new require('net').Socket(),

	HOST 				= 'localhost',
	PORT 				= 8080,
	URI 				= 'tcp://localhost:8080/remote',
	ENCODING 			= 'ascii';

console.log('---- NodeJS Remoting Parser ----');

client.connect(PORT, HOST, function() {
	//json message formatter
	var msg = {
		//typeName is not necessary.
		//must be AssemblyQualifiedName.
		TypeName 	: 'RemotingServer.ServiceClass,RemotingServer',
		MethodName	: 'Do',
		Args 		: ['hi']
	};
	var msgBuffer = new Buffer(JSON.stringify(msg), ENCODING);

	//send remoting request
	var w = tcpWriter(client);
	//Preamble
	w.writePreamble();
	//MajorVersion
	w.writeMajorVersion();
	//MinorVersion
	w.writeMinorVersion();
	//Operation
	w.writeOperation(TcpOperations.Request);
	//ContentDelimiter
	w.writeContentDelimiter();
	//ContentLength
	w.writeContentLength(msgBuffer.length);
	//Headers
	var headers = {};
	headers[TcpTransportHeader.RequestUri] = URI;
	w.writeHeaders(headers);
	//Content
	w.writeContent(msgBuffer);
});

client.on('data', function(data) {
	//console.log(data.toString(ENCODING));

	//read remoting response
	var r = tcpReader(data);
	console.log('---- received ----');
	//Preamble
	console.log('Preamble: %s', r.readPreamble());
	//MajorVersion
	console.log('MajorVersion: %s', r.readMajorVersion());
	//MinorVersion
	console.log('MinorVersion: %s', r.readMinorVersion());
	//Operation
	console.log('Operation: %s', r.readOperation());
	//ContentDelimiter
	console.log('ContentDelimiter: %s', r.readContentDelimiter());
	//ContentLength
	console.log('ContentLength: %s', r.readContentLength());
	//Headers
	console.log('---- Headers ----');
	console.log(r.readHeaders());
	//Content
	console.log('---- Content ----');
	var msgBuffer = r.readContent();
	console.log(msgBuffer);
	var msg = JSON.parse(msgBuffer.toString());
	console.log(msg);
	console.log('return: %s', msg.Return);

	client.end();
});
