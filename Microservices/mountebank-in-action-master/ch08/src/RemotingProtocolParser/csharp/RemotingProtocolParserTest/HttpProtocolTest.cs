/*
    (The MIT License)

    Copyright (C) 2012 wsky (wskyhx at gmail.com) and other contributors

    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

using System;
using System.Collections;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Runtime.Remoting;
using System.Runtime.Remoting.Channels;
using System.Runtime.Remoting.Channels.Http;
using System.Runtime.Remoting.Channels.Tcp;
using System.Runtime.Remoting.Messaging;
using System.Text;
using System.Threading;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using RemotingProtocolParser;
using RemotingProtocolParser.HTTP;

namespace RemotingProtocolParserTest
{
    [TestClass]
    public class HttpProtocolTest
    {
        [TestMethod]
        public void WriteRequestAndReadResponse()
        {
            var properties = new Hashtable() { { "port", 8080 } };
            var channel = new HttpChannel(properties, null, new BinaryServerFormatterSinkProvider());
            ChannelServices.RegisterChannel(channel, false);
            RemotingServices.Marshal(new ServiceClass(), "Remote");

            var uri = "http://localhost:8080/Remote";

            var messageRequest = new MethodCall(new Header[] { 
                new Header(MessageHeader.Uri, uri),
                new Header(MessageHeader.MethodName, "Do"),
                new Header(MessageHeader.MethodSignature, new Type[] { typeof(string) }),
                new Header(MessageHeader.TypeName, typeof(ServiceClass).AssemblyQualifiedName),
                new Header(MessageHeader.Args, new object[] { "Hi" })
            });
            var messageRequestStream = BinaryFormatterHelper.SerializeObject(messageRequest);

            var wc = new WebClient();
            var messageResponse = BinaryFormatterHelper.DeserializeObject(wc.UploadData(uri, messageRequestStream)) as MethodResponse;
            Assert.IsNotNull(messageResponse);
            DumpHelper.DumpMessage(messageResponse);
            if (messageResponse.Exception != null)
                throw messageResponse.Exception;
            Assert.AreEqual("Hi", messageResponse.ReturnValue);
            
            //handle.WriteRequestFirstLine("POST", "/Remote");
            //handle.WriteHeaders(new Dictionary<string, object>() { { HttpHeader.ContentLength, messageRequestStream.Length } });
            //HACK:http will send 100 continue after read http header
            //handle.WriteContent(messageRequestStream);

            ChannelServices.UnregisterChannel(channel);
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
                var handle = new HttpProtocolHandle(c.GetStream());

                //read remoting request
                Trace.WriteLine(handle.ReadFirstLine());
                DumpHelper.DumpDictionary(handle.ReadHeaders());
                var messageRequest = BinaryFormatterHelper.DeserializeObject(handle.ReadContent()) as MethodCall;

                //HACK:SoapFormatter not work well
                //http://labs.developerfusion.co.uk/SourceViewer/browse.aspx?assembly=SSCLI&namespace=System.Runtime.Remoting
                //var f = new System.Runtime.Serialization.Formatters.Soap.SoapFormatter();
                //f.Context = new System.Runtime.Serialization.StreamingContext(System.Runtime.Serialization.StreamingContextStates.Other);
                //var mc = new MethodCall(this.Parse(headers));
                //var messageRequest = f.Deserialize(new System.IO.MemoryStream(content), new HeaderHandler(mc.HeaderHandler));

                Assert.IsNotNull(messageRequest);
                DumpHelper.DumpMessage(messageRequest);

                //write remoting response
                var responeMessage = new MethodResponse(new Header[] { new Header(MessageHeader.Return, messageRequest.Args[0]) }, messageRequest);
                var responseStream = BinaryFormatterHelper.SerializeObject(responeMessage);
                handle.WriteResponseFirstLine("200", "ok");
                handle.WriteHeaders(new Dictionary<string, object>() { { HttpHeader.ContentLength, responseStream.Length } });
                handle.WriteContent(responseStream);

                //end httprequest
                c.Close();
            }));
            t.Start();

            //use BinaryFormatter via HTTP
            var channel = new HttpChannel(new Hashtable(), new BinaryClientFormatterSinkProvider(), null);
            ChannelServices.RegisterChannel(channel, false);
            var url = string.Format("http://{0}/remote.rem", endpoint);
            var service = RemotingServices.Connect(typeof(ServiceClass), url) as ServiceClass;
            Assert.AreEqual("Hi", service.Do("Hi"));

            t.Abort();

            ChannelServices.UnregisterChannel(channel);
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