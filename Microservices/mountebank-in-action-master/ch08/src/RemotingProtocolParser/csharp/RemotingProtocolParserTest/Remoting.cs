/*
    (The MIT License)

    Copyright (C) 2012 wsky (wskyhx at gmail.com) and other contributors

    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

using System;
using System.Linq;
using System.Collections.Generic;
using System.IO;
using System.Runtime.Remoting;
using System.Runtime.Remoting.Channels;
using System.Runtime.Remoting.Channels.Tcp;
using System.Runtime.Remoting.Messaging;
using System.Runtime.Serialization.Json;
using System.Reflection;
using System.Collections;

public class RemotingServer
{
    public static void Main(string[] args)
    {
        var channel = new TcpChannel(new Hashtable() { { "port", 8080 } }, null, new JsonServerFormatterSinkProvider());
        //var channel = new TcpChannel(new Hashtable() { { "port", 8080 } }, null, null);
        ChannelServices.RegisterChannel(channel, false);
        RemotingServices.Marshal(new ServiceClass(), "remote");

        Console.WriteLine("Remoting server running at tcp://localhost:8080/remote");
        Console.ReadKey();
        //System.Threading.Thread.Sleep(10000000);
    }

    public class ServiceClass : MarshalByRefObject
    {
        public string Do(string input)
        {
            return input;
        }
    }

    public class JsonServerFormatterSinkProvider : IServerFormatterSinkProvider
    {
        public IServerChannelSink CreateSink(IChannelReceiver channel)
        {
            if (this.Next != null)
            {
                var nextChannelSink = Next.CreateSink(channel);
                if (nextChannelSink != null)
                    return new JsonServerFormatterSink(nextChannelSink);
            }
            return null;
        }
        public void GetChannelData(IChannelDataStore channelData) { }
        public IServerChannelSinkProvider Next { get; set; }
    }
    public class JsonServerFormatterSink : IServerChannelSink
    {
        private readonly IServerChannelSink _nextChannelSink;

        public IServerChannelSink NextChannelSink { get { return this._nextChannelSink; } }
        public System.Collections.IDictionary Properties { get { return null; } }

        public JsonServerFormatterSink(IServerChannelSink nextChannelSink)
        {
            this._nextChannelSink = nextChannelSink;
        }
        public System.IO.Stream GetResponseStream(IServerResponseChannelSinkStack sinkStack
            , object state
            , IMessage msg
            , ITransportHeaders headers)
        {
            throw new NotImplementedException();
        }
        public void AsyncProcessResponse(IServerResponseChannelSinkStack sinkStack
            , object state
            , IMessage msg
            , ITransportHeaders headers
            , System.IO.Stream stream)
        {
            SerializeResponseMessage(sinkStack, msg, ref headers, ref stream);
            sinkStack.AsyncProcessResponse(msg, headers, stream);
        }
        public ServerProcessing ProcessMessage(IServerChannelSinkStack sinkStack
            , IMessage requestMsg
            , ITransportHeaders requestHeaders
            , Stream requestStream
            , out IMessage responseMsg
            , out ITransportHeaders responseHeaders
            , out System.IO.Stream responseStream)
        {
            if (requestMsg != null)
                return this.NextChannelSink.ProcessMessage(sinkStack
                    , requestMsg
                    , requestHeaders
                    , requestStream
                    , out responseMsg
                    , out responseHeaders
                    , out responseStream);

            requestMsg = DeserializeRequestMessage(requestHeaders, requestStream);

            sinkStack.Push(this, null);

            var result = this.NextChannelSink.ProcessMessage(sinkStack
                , requestMsg
                , requestHeaders
                , null
                , out responseMsg
                , out responseHeaders
                , out responseStream);

            switch (result)
            {
                case ServerProcessing.Complete:
                    sinkStack.Pop(this);
                    SerializeResponseMessage(sinkStack, responseMsg, ref responseHeaders, ref responseStream);
                    break;
                case ServerProcessing.OneWay:
                    sinkStack.Pop(this);
                    break;
                case ServerProcessing.Async:
                    sinkStack.Store(this, null);
                    break;
            }

            return result;
        }

        private static IMessage DeserializeRequestMessage(ITransportHeaders requestHeaders, Stream requestStream)
        {
            if (requestHeaders == null)
                throw new ArgumentNullException("requestHeaders");
            if (requestStream == null)
                throw new ArgumentNullException("requestStream");

            var requestUri = (string)requestHeaders["__RequestUri"];
            if (requestUri == null)
                throw new RemotingException("Request URI not specified.");

            var serverType = RemotingServices.GetServerTypeForUri(requestUri);
            if (serverType == null)
                throw new RemotingException(string.Format(
                    "Request URI '{0}' not published for remoting.", requestUri));

            using (requestStream)
            {
                var request = (JSONSerializer.Deserialize(typeof(JsonMessage), requestStream) as JsonMessage) ?? new JsonMessage();
                var serverMethod = GetServerMethod(serverType, request.MethodName);

                if (serverMethod == null)
                    throw new RemotingException(string.Format(
                        "Invalid request method '{0}' specified.", request.MethodName));

                var headers = new List<Header>()
				{
					new Header("__Uri", requestUri),
					new Header("__TypeName", serverMethod.DeclaringType.AssemblyQualifiedName),
					new Header("__MethodName", request.MethodName),
					//new Header("__MethodSignature", GetMethodSignature(serverMethod)),
					new Header("__Args", request.Args),
					//new Header("__CallContext", request.CallContext)
				};
                headers.AddRange(request.GetMessageHeaders());

                return new MethodCall(headers.ToArray());
            }
        }
        private static void SerializeResponseMessage(IServerResponseChannelSinkStack sinkStack
            , IMessage responseMsg
            , ref ITransportHeaders responseHeaders
            , ref Stream responseStream)
        {
            if (sinkStack == null)
                throw new ArgumentNullException("sinkStack");
            if (responseMsg == null)
                throw new ArgumentNullException("responseMsg");

            var methodReturnMessage = responseMsg as IMethodReturnMessage;
            if (methodReturnMessage == null)
                throw new ArgumentException(string.Format(
                    "Invalid response message type: '{0}'.", responseMsg.GetType()), "responseMsg");

            if (responseHeaders == null)
                responseHeaders = new TransportHeaders();

            bool shouldRewindStream = false;

            if (responseStream == null)
            {
                responseStream = sinkStack.GetResponseStream(responseMsg, responseHeaders);

                if (responseStream == null)
                {
                    responseStream = new MemoryStream();
                    shouldRewindStream = true;
                }
            }

            var m = new JsonMessage();
            m.Return = methodReturnMessage.ReturnValue;
            m.Exception = methodReturnMessage.Exception;
            JSONSerializer.Serialize(responseStream, m, typeof(JsonMessage));

            if (shouldRewindStream)
            {
                responseStream.Position = 0;
            }
        }
        private static MethodInfo GetServerMethod(Type serverType, string requestMethod)
        {
            return serverType.GetMethod(requestMethod, BindingFlags.Instance | BindingFlags.Public | BindingFlags.FlattenHierarchy);
        }
    }
    public class JsonMessage
    {
        public string TypeName { get; set; }
        public string MethodName { get; set; }
        public IDictionary<string, object> Headers { get; set; }
        public object[] Args { get; set; }
        public object Return { get; set; }
        public object Exception { get; set; }
        public IEnumerable<Header> GetMessageHeaders()
        {
            return this.Headers != null 
                ? this.Headers.Select(o => new Header(o.Key, o.Value)) 
                : new List<Header>();
        }

        public JsonMessage()
        {
            this.Args = new object[] { };
        }
    }
    public class JSONSerializer
    {
        public static void Serialize(Stream stream, object obj, Type type)
        {
            new DataContractJsonSerializer(type).WriteObject(stream, obj);
        }
        public static object Deserialize(Type target, Stream stream)
        {
            try
            {
                return new DataContractJsonSerializer(target).ReadObject(stream);
            }
            catch (Exception e)
            {
                throw e;
                //return null;
            }
        }
    }
}