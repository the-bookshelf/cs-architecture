/*
    (The MIT License)

    Copyright (C) 2012 wsky (wskyhx at gmail.com) and other contributors

    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Runtime.Remoting;
using System.Runtime.Remoting.Channels;
using System.Runtime.Remoting.Channels.Tcp;
using System.Runtime.Remoting.Messaging;
using System.Text;
using System.Threading;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using RemotingProtocolParser;
using RemotingProtocolParser.TCP;

namespace RemotingProtocolParserTest
{
    [TestClass]
    public class TcpProtocolTest
    {
        [TestMethod]
        public void WriteRequestAndReadResponse()
        {
            var channel = new TcpChannel(8080);
            ChannelServices.RegisterChannel(channel, false);
            RemotingServices.Marshal(new ServiceClass(), "Remote");

            var uri = "tcp://localhost:8080/Remote";

            using (var client = new TcpClient())
            {
                client.Connect("localhost", 8080);
                using (var stream = client.GetStream())
                {
                    var messageRequest = new MethodCall(new Header[] { 
                        new Header(MessageHeader.Uri, uri),
                        new Header(MessageHeader.MethodName, "Do"),
                        new Header(MessageHeader.MethodSignature, new Type[] { typeof(string) }),
                        new Header(MessageHeader.TypeName, typeof(ServiceClass).AssemblyQualifiedName),
                        new Header(MessageHeader.Args, new object[] { "Hi" })
                    });
                    var messageRequestStream = BinaryFormatterHelper.SerializeObject(messageRequest);

                    var handle = new TcpProtocolHandle(stream);

                    //send remoting request
                    handle.WritePreamble();
                    handle.WriteMajorVersion();
                    handle.WriteMinorVersion();
                    handle.WriteOperation(TcpOperations.Request);
                    handle.WriteContentDelimiter(TcpContentDelimiter.ContentLength);
                    handle.WriteContentLength(messageRequestStream.Length);
                    handle.WriteTransportHeaders(new Dictionary<string, object>() { { TcpTransportHeader.RequestUri, uri } });
                    handle.WriteContent(messageRequestStream);

                    //read remoting response
                    Console.WriteLine("Preamble: {0}", handle.ReadPreamble());
                    Console.WriteLine("MajorVersion: {0}", handle.ReadMajorVersion());
                    Console.WriteLine("MinorVersion: {0}", handle.ReadMinorVersion());
                    var op = handle.ReadOperation();
                    Assert.AreEqual(TcpOperations.Reply, op);
                    Console.WriteLine("Operation: {0}", op);
                    Console.WriteLine("ContentDelimiter: {0}", handle.ReadContentDelimiter());
                    Console.WriteLine("ContentLength: {0}", handle.ReadContentLength());
                    handle.ReadTransportHeaders();
                    var messageResponse = BinaryFormatterHelper.DeserializeObject(handle.ReadContent()) as MethodResponse;
                    Assert.IsNotNull(messageResponse);
                    DumpHelper.DumpMessage(messageResponse);
                    if (messageResponse.Exception != null)
                        throw messageResponse.Exception;
                    Assert.AreEqual("Hi", messageResponse.ReturnValue);
                }
            }
        }
        [TestMethod]
        public void ReadRequestAndWriteResponse()
        {
            IPAddress[] addressList = Dns.GetHostEntry(Environment.MachineName).AddressList;
            var endpoint = new IPEndPoint(addressList[addressList.Length - 1], 9900);
            var listener = new TcpListener(endpoint);
            listener.Start();

            var t = new Thread(new ThreadStart(() =>
            {
                var c = listener.AcceptTcpClient();
                var handle = new TcpProtocolHandle(c.GetStream());

                //read remoting request
                Console.WriteLine("Preamble: {0}", handle.ReadPreamble());
                Console.WriteLine("MajorVersion: {0}", handle.ReadMajorVersion());
                Console.WriteLine("MinorVersion: {0}", handle.ReadMinorVersion());
                var op = handle.ReadOperation();
                Assert.AreEqual(TcpOperations.Request, op);
                Console.WriteLine("Operation: {0}", op);
                Console.WriteLine("ContentDelimiter: {0}", handle.ReadContentDelimiter());
                Console.WriteLine("ContentLength: {0}", handle.ReadContentLength());
                DumpHelper.DumpDictionary(handle.ReadTransportHeaders());
                var messageRequest = BinaryFormatterHelper.DeserializeObject(handle.ReadContent()) as MethodCall;
                Assert.IsNotNull(messageRequest);
                DumpHelper.DumpMessage(messageRequest);

                //write remoting response
                var responeMessage = new MethodResponse(new Header[] { new Header(MessageHeader.Return, messageRequest.Args[0]) }, messageRequest);
                var responseStream = BinaryFormatterHelper.SerializeObject(responeMessage);
                handle.WritePreamble();
                handle.WriteMajorVersion();
                handle.WriteMinorVersion();
                handle.WriteOperation(TcpOperations.Reply);
                handle.WriteContentDelimiter(TcpContentDelimiter.ContentLength);
                handle.WriteContentLength(responseStream.Length);
                handle.WriteTransportHeaders(null);
                handle.WriteContent(responseStream);
            }));
            t.Start();

            var url = string.Format("tcp://{0}/remote.rem", endpoint);
            var service = RemotingServices.Connect(typeof(ServiceClass), url) as ServiceClass;
            Assert.AreEqual("Hi", service.Do("Hi"));

            t.Abort();
        }

        public class ServiceClass : MarshalByRefObject
        {
            public string Do(string input)
            {
                return input;
            }
        }
    }
}