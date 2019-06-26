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
using System.Linq;
using System.Runtime.Remoting;
using System.Runtime.Remoting.Channels;
using System.Runtime.Remoting.Channels.Tcp;
using System.Runtime.Remoting.Messaging;
using System.Runtime.Remoting.Proxies;
using System.Runtime.Remoting.Services;
using System.Text;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace CodeSharp.Core.Castles.Test
{
    [TestClass]
    public class CustomRemotingProxyTest
    {
        public void TrackingAndCustomProxy()
        {
            TrackingServices.RegisterTrackingHandler(new TrackingHandler());

            var channel = new TcpChannel(8080);
            ChannelServices.RegisterChannel(channel, false);
            var service = new ServiceClass();
            ObjRef obj = RemotingServices.Marshal(service, "TcpService");

            var url = "tcp://localhost:8080/TcpService";
            service = RemotingServices.Connect(typeof(ServiceClass), url) as ServiceClass;
            Console.WriteLine("client received: {0}", service.Echo("hello"));

            //use custom RealProxy
            service = new CustomProxy(typeof(ServiceClass), url).GetTransparentProxy() as ServiceClass;
            Console.WriteLine("client received: {0}", service.Echo("hello"));

            RemotingServices.Unmarshal(obj);
            RemotingServices.Disconnect(service);
        }
        public class ServiceClass : MarshalByRefObject
        {
            public string Echo(string msg) { Console.WriteLine("server received: " + msg); return msg; }
        }
        public class TrackingHandler : ITrackingHandler
        {
            public void DisconnectedObject(object obj)
            {
                Console.WriteLine("DisconnectedObject: " + obj);
            }
            public void MarshaledObject(object obj, System.Runtime.Remoting.ObjRef or)
            {
                Console.WriteLine("MarshaledObject: " + obj);
            }
            public void UnmarshaledObject(object obj, System.Runtime.Remoting.ObjRef or)
            {
                Console.WriteLine("UnmarshaledObject: " + obj);
            }
        }
        public class CustomProxy : RealProxy
        {
            private string _url, _uri;
            private IMessageSink _sinkChain;

            public CustomProxy(Type type, string url)
                : base(type)
            {
                this._url = url;

                IChannel[] registeredChannels = ChannelServices.RegisteredChannels;
                foreach (IChannel chnl in registeredChannels)
                    if (chnl is IChannelSender)
                        if ((this._sinkChain = ((IChannelSender)chnl).CreateMessageSink(this._url, null, out this._uri)) != null)
                            break;

                if (_sinkChain == null)
                    throw new Exception("No channel has been found for " + this._url);
            }
            public override IMessage Invoke(IMessage msg)
            {
                IDictionary d = msg.Properties;
                d["__Uri"] = this._url;

                this.DumpMessage(msg);

                IMessage retMsg = _sinkChain.SyncProcessMessage(msg);

                this.DumpMessage(retMsg);

                return retMsg;
            }
            private void DumpMessage(IMessage msg)
            {
                Console.WriteLine("");
                Console.WriteLine("==== Message Dump ====");
                Console.WriteLine("Type: {0}", msg);
                Console.WriteLine("---- Properties ----");
                var enm = msg.Properties.GetEnumerator();
                while (enm.MoveNext())
                {
                    Console.WriteLine("{0}: {1}", enm.Key, enm.Value);
                    var data = enm.Value as object[];
                    if (data != null)
                        this.DumpArray(data);
                }

                Console.WriteLine("\n\n");
            }
            private void DumpArray(object[] data)
            {
                Console.WriteLine("\t---- Array ----");
                for (var i = 0; i < data.Length; i++)
                    Console.WriteLine("\t{0}: {1}", i, data[i]);
            }
        }
    }
}
