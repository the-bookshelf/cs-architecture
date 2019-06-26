/*
    (The MIT License)

    Copyright (C) 2012 wsky (wskyhx at gmail.com) and other contributors

    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

using System;
using System.Collections;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Runtime.Remoting;
using System.Runtime.Remoting.Channels;
using System.Runtime.Remoting.Channels.Tcp;
using System.Runtime.Remoting.Messaging;
using System.Runtime.Serialization.Formatters;
using System.Runtime.Serialization.Formatters.Binary;
using System.Text;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace RemotingProtocolParserTest
{
    /// <summary>test something of .net remoting protocol for parser
    /// </summary>
    [TestClass]
    public class SampleParserTest
    {
        [TestMethod]
        public void WriteRequestAndReadResponse()
        {
            //var properties = new Hashtable() { { "port", 8080 } };
            //var channel = new TcpChannel(properties, null, new SoapServerFormatterSinkProvider());
            //if using SOAP via TCP, messageRequestStream must be SOAP format
            var channel = new TcpChannel(8080);
            ChannelServices.RegisterChannel(channel, false);
          
            var service = new ServiceClass();
            ObjRef obj = RemotingServices.Marshal(service, "Remote");

            var uri = "tcp://localhost:8080/Remote";

            using (var client = new TcpClient())
            {
                client.Connect("localhost", 8080);
                using (var stream = client.GetStream())
                {
                    var messageRequest = new MethodCall(new Header[] { 
                        new Header("__Uri", uri),
                        new Header("__MethodName", "Do"),
                        new Header("__MethodSignature", new Type[] { typeof(string) }),
                        new Header("__TypeName", typeof(ServiceClass).AssemblyQualifiedName),
                        new Header("__Args", new object[] { "Hi" })
                    });
                    var messageRequestStream = BinaryFormatterHelper.SerializeObject(messageRequest);

                    var writer = new ProtocolWriter(stream);
                    writer.WritePreamble();
                    writer.WriteMajorVersion();
                    writer.WriteMinorVersion();
                    writer.WriteOperation(TcpOperations.Request);
                    writer.WriteContentDelimiter(TcpContentDelimiter.ContentLength);
                    writer.WriteContentLength(messageRequestStream.Length);
                    writer.WriteTransportHeaders(uri);
                    writer.WriteBytes(messageRequestStream);


                    var reader = new ProtocolReader(stream);
                    Console.WriteLine("Preamble: {0}", reader.ReadPreamble());
                    Console.WriteLine("MajorVersion: {0}", reader.ReadMajorVersion());
                    Console.WriteLine("MinorVersion: {0}", reader.ReadMinorVersion());
                    var op = reader.ReadOperation();
                    Assert.AreEqual(TcpOperations.Reply, op);
                    Console.WriteLine("Operation: {0}", op);
                    Console.WriteLine("ContentDelimiter: {0}", reader.ReadContentDelimiter());
                    var length = reader.ReadContentLength();
                    Console.WriteLine("ContentLength: {0}", length);
                    reader.ReadTransportHeaders();
                    var messageResponse = BinaryFormatterHelper.DeserializeObject(reader.ReadBytes(length)) as MethodResponse;
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

            new TcpListener().Listen(endpoint);

            var url = string.Format("tcp://{0}/remote.rem", endpoint);
            var service = RemotingServices.Connect(typeof(ServiceClass), url) as ServiceClass;
            Assert.AreEqual("Hi", service.Do("Hi"));
        }

        public class ServiceClass : MarshalByRefObject
        {
            public string Do(string input)
            {
                Console.WriteLine("ServiceClass Received: {0}", input);
                return input;
            }
        }

        //reference:http://www.cnblogs.com/wsky/archive/2011/04/06/2007201.html
        //just a demo server
        public class TcpListener
        {
            private SocketAsyncEventArgs Args;
            private Socket ListenerSocket;
            private StringBuilder buffers;
            private byte[] totalBuffer = new byte[0];
            public TcpListener() { }
            public void Listen(EndPoint e)
            {
                //buffer
                buffers = new StringBuilder();
                //socket
                ListenerSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
                ListenerSocket.Bind(e);
                ListenerSocket.Listen(10);

                Args = new SocketAsyncEventArgs();
                Args.Completed += new EventHandler<SocketAsyncEventArgs>(ProcessAccept);
                BeginAccept(Args);
                Console.WriteLine("server run at {0}", e.ToString());
            }

            void BeginAccept(SocketAsyncEventArgs e)
            {
                e.AcceptSocket = null;
                if (!ListenerSocket.AcceptAsync(e))
                    ProcessAccept(ListenerSocket, e);
            }
            void ProcessAccept(object sender, SocketAsyncEventArgs e)
            {
                Socket s = e.AcceptSocket;
                e.AcceptSocket = null;

                int bufferSize = 10;
                var args = new SocketAsyncEventArgs();
                args.Completed += new EventHandler<SocketAsyncEventArgs>(OnIOCompleted);
                args.SetBuffer(new byte[bufferSize], 0, bufferSize);
                args.AcceptSocket = s;
                if (!s.ReceiveAsync(args))
                    this.ProcessReceive(args);

                BeginAccept(e);
            }

            void OnIOCompleted(object sender, SocketAsyncEventArgs e)
            {
                switch (e.LastOperation)
                {
                    case SocketAsyncOperation.Receive:
                        this.ProcessReceive(e);
                        break;
                    case SocketAsyncOperation.Send:
                        this.ProcessSend(e);
                        break;
                    default:
                        throw new ArgumentException("The last operation completed on the socket was not a receive or send");
                }
            }

            void ProcessReceive(SocketAsyncEventArgs e)
            {
                if (e.BytesTransferred > 0)
                {
                    if (e.SocketError == SocketError.Success)
                    {
                        totalBuffer = Combine(totalBuffer, e.Buffer);

                        if (e.AcceptSocket.Available == 0)
                        {
                            //.Net Remoting Protocol Parser
                            #region Read Request
                            Console.WriteLine("==== .Net Remoting Protocol Parser ====");
                            //1. Preamble, will be ".NET"
                            Console.WriteLine("Preamble: {0}", Encoding.ASCII.GetString(new byte[] { 
                            totalBuffer[0], 
                            totalBuffer[1], 
                            totalBuffer[2], 
                            totalBuffer[3] }));
                            //2. MajorVersion, will be 1
                            Console.WriteLine("MajorVersion: {0}", totalBuffer[4]);
                            //3. MinorVersion, will be 0
                            Console.WriteLine("MinorVersion: {0}", totalBuffer[5]);
                            //4. Operation, will be 5 (request,onewayrequest...)
                            Console.WriteLine("Operation: {0}", (UInt16)(totalBuffer[6] & 0xFF | totalBuffer[7] << 8));
                            //5. TcpContentDelimiter and ContentLength
                            var header = (UInt16)(totalBuffer[8] & 0xFF | totalBuffer[9] << 8);
                            if (header == 1)
                                Console.WriteLine("Chunked: {0}", true);
                            else
                                Console.WriteLine("ContentLength: {0}"
                                    , (int)((totalBuffer[10] & 0xFF)
                                    | totalBuffer[11] << 8
                                    | totalBuffer[12] << 16
                                    | totalBuffer[13] << 24));

                            #region 6. ReadHeaders ITransportHeaders
                            Console.WriteLine("---- ITransportHeaders ----");
                            var index = header == 1 ? 9 : 13;
                            var headerType = ReadUInt16(ref index);
                            while (headerType != TcpHeaders.EndOfHeaders)
                            {
                                if (headerType == TcpHeaders.Custom)
                                {
                                    Console.WriteLine("{0}: {1}", ReadCountedString(ref index), ReadCountedString(ref index));
                                }
                                else if (headerType == TcpHeaders.RequestUri)
                                {
                                    Console.WriteLine("RequestUri-Format: {0}", ReadByte(ref index));
                                    Console.WriteLine("RequestUri: {0}", ReadCountedString(ref index));
                                }
                                else if (headerType == TcpHeaders.StatusCode)
                                {
                                    Console.WriteLine("StatusCode-Format: {0}", ReadByte(ref index));
                                    var code = ReadUInt16(ref index);
                                    Console.WriteLine("StatusCode: {0}", code);
                                    //if (code != 0) error = true;
                                }
                                else if (headerType == TcpHeaders.StatusPhrase)
                                {
                                    Console.WriteLine("StatusPhrase-Format: {0}", ReadByte(ref index));
                                    Console.WriteLine("StatusPhrase: {0}", ReadCountedString(ref index));
                                }
                                else if (headerType == TcpHeaders.ContentType)
                                {
                                    Console.WriteLine("ContentType-Format: {0}", ReadByte(ref index));
                                    Console.WriteLine("ContentType: {0}", ReadCountedString(ref index));
                                }
                                else
                                {
                                    var headerFormat = (byte)ReadByte(ref index);

                                    switch (headerFormat)
                                    {
                                        case TcpHeaderFormat.Void: break;
                                        case TcpHeaderFormat.CountedString: ReadCountedString(ref index); break;
                                        case TcpHeaderFormat.Byte: ReadByte(ref index); break;
                                        case TcpHeaderFormat.UInt16: ReadUInt16(ref index); break;
                                        case TcpHeaderFormat.Int32: ReadInt32(ref index); break;
                                        default:
                                            throw new RemotingException("Remoting_Tcp_UnknownHeaderType");
                                    }
                                }

                                headerType = ReadUInt16(ref index);
                            }
                            #endregion

                            //7. RequestStream/Message
                            var requestStream = new byte[totalBuffer.Length - index - 1];
                            Buffer.BlockCopy(totalBuffer, index + 1, requestStream, 0, totalBuffer.Length - index - 1);
                            //using BinaryFormatterSink default
                            var requestMessage = BinaryFormatterHelper.DeserializeObject(requestStream) as MethodCall;
                            DumpHelper.DumpMessage(requestMessage);
                            #endregion

                            buffers = new StringBuilder();
                            totalBuffer = new byte[0];

                            #region Write Response

                            //http://labs.developerfusion.co.uk/SourceViewer/browse.aspx?assembly=SSCLI&namespace=System.Runtime.Remoting
                            //else if (name.Equals("__Return"))
                            var responeMessage = new MethodResponse(
                                //just return args[0]
                                new Header[] { new Header("__Return", requestMessage.Args[0]) }
                                , requestMessage);

                            //responeMessage.ReturnValue//can not set
                            var responseStream = BinaryFormatterHelper.SerializeObject(responeMessage);
                            //1.Preamble
                            var preamble = Encoding.ASCII.GetBytes(".NET");
                            foreach (var b in preamble)
                                WriteByte(b);
                            //2.MajorVersion
                            WriteByte((byte)1);
                            //3.MinorVersion
                            WriteByte((byte)0);
                            //4.Operation
                            WriteUInt16(TcpOperations.Reply);
                            //5.TcpContentDelimiter and ContentLength
                            WriteUInt16(0);
                            WriteInt32(responseStream.Length);
                            //6.Headers
                            WriteUInt16(TcpHeaders.EndOfHeaders);
                            //7.ResponseStream/Message
                            foreach (var b in responseStream)
                                WriteByte(b);
                            #endregion

                            e.SetBuffer(totalBuffer, 0, totalBuffer.Length);
                            if (!e.AcceptSocket.SendAsync(e))
                            {
                                this.ProcessSend(e);
                            }
                        }
                        else if (!e.AcceptSocket.ReceiveAsync(e))
                        {
                            this.ProcessReceive(e);
                        }
                    }
                    else
                    {
                        //this.ProcessError(e);
                    }
                }
                else
                {
                    //this.CloseClientSocket(e);
                }
            }
            void ProcessSend(SocketAsyncEventArgs e)
            {
                if (e.SocketError == SocketError.Success)
                {
                    if (!e.AcceptSocket.ReceiveAsync(e))
                    {
                        this.ProcessReceive(e);
                    }
                }
                else
                {

                }
            }

            byte[] Combine(byte[] first, byte[] second)
            {
                byte[] ret = new byte[first.Length + second.Length];
                Buffer.BlockCopy(first, 0, ret, 0, first.Length);
                Buffer.BlockCopy(second, 0, ret, first.Length, second.Length);
                return ret;
            }
            int ReadByte(ref int index)
            {
                return totalBuffer[++index];
            }
            UInt16 ReadUInt16(ref int index)
            {
                return (UInt16)(totalBuffer[++index] & 0xFF | totalBuffer[++index] << 8);
            }
            int ReadInt32(ref int index)
            {
                return (int)((totalBuffer[++index] & 0xFF)
                    | totalBuffer[++index] << 8
                    | totalBuffer[++index] << 16
                    | totalBuffer[++index] << 24);
            }
            string ReadCountedString(ref int index)
            {
                var format = (byte)ReadByte(ref index);
                int size = ReadInt32(ref index);

                if (size > 0)
                {
                    byte[] data = new byte[size];
                    Buffer.BlockCopy(totalBuffer, index + 1, data, 0, size);
                    index += size;

                    switch (format)
                    {
                        case TcpStringFormat.Unicode:
                            return Encoding.Unicode.GetString(data);

                        case TcpStringFormat.UTF8:
                            return Encoding.UTF8.GetString(data);

                        default:
                            throw new RemotingException("Remoting_Tcp_UnrecognizedStringFormat");
                    }
                }
                else
                {
                    return null;
                }
            }

            void WriteByte(byte data)
            {
                totalBuffer = Combine(totalBuffer, new byte[] { data });
            }
            void WriteUInt16(UInt16 data)
            {
                WriteByte((byte)data);
                WriteByte((byte)(data >> 8));
            }
            void WriteInt32(int data)
            {
                WriteByte((byte)data);
                WriteByte((byte)(data >> 8));
                WriteByte((byte)(data >> 16));
                WriteByte((byte)(data >> 24));
            }
        }
        public class BinaryFormatterHelper
        {
            private static readonly BinaryFormatter FormatterInstance = new BinaryFormatter
            {
                AssemblyFormat = FormatterAssemblyStyle.Simple,
                TypeFormat = FormatterTypeStyle.TypesWhenNeeded,
                FilterLevel = TypeFilterLevel.Full,
            };

            public static byte[] SerializeObject(object value)
            {
                if (value == null)
                    return null;

                using (var stream = new MemoryStream())
                {
                    FormatterInstance.Serialize(stream, value);
                    return stream.ToArray();
                }
            }

            public static object DeserializeObject(byte[] data)
            {
                if (data == null)
                    return null;

                using (var stream = new MemoryStream(data))
                {
                    return FormatterInstance.Deserialize(stream);
                }
            }
        }
        public class DumpHelper
        {
            public static void DumpMessage(IMessage msg)
            {
                Console.WriteLine("");
                Console.WriteLine("==== Message Dump ====");
                Console.WriteLine("Type: {0}", msg);
                if (msg is MethodCall)
                {
                    var call = msg as MethodCall;
                    Console.WriteLine("Uri: {0}", call.Uri);
                    Console.WriteLine("---- MethodCall.Args ----");
                    DumpArray(call.Args);
                }
                Console.WriteLine("---- Properties ----");
                var enm = msg.Properties.GetEnumerator();
                while (enm.MoveNext())
                {
                    Console.WriteLine("{0}: {1}", enm.Key, enm.Value);
                    var data = enm.Value as object[];
                    if (data != null)
                        DumpArray(data);
                }

                Console.WriteLine("\n\n");
            }
            public static void DumpArray(object[] data)
            {
                Console.WriteLine("\t---- Array ----");
                for (var i = 0; i < data.Length; i++)
                    Console.WriteLine("\t{0}: {1}", i, data[i]);
            }
        }

        #region MS Remoting Sourcecode
        public class TcpHeaders
        {
            internal const ushort CloseConnection = 5;
            internal const ushort Custom = 1;
            internal const ushort ContentType = 6;
            internal const ushort EndOfHeaders = 0;
            internal const ushort RequestUri = 4;
            internal const ushort StatusCode = 2;
            internal const ushort StatusPhrase = 3;
        }
        public class TcpStringFormat
        {
            internal const byte Unicode = 0;
            internal const byte UTF8 = 1;
        }
        public class TcpHeaderFormat
        {
            internal const byte Byte = 2;
            internal const byte CountedString = 1;
            internal const byte Int32 = 4;
            internal const byte UInt16 = 3;
            internal const byte Void = 0;
        }
        public class TcpOperations
        {
            internal const ushort OneWayRequest = 1;
            internal const ushort Reply = 2;
            internal const ushort Request = 0;
        }
        public class TcpContentDelimiter
        {
            internal const ushort ContentLength = 0;
            internal const ushort Chunked = 1;
        }
        #endregion

        public class ProtocolReader
        {
            private Stream _stream;
            public ProtocolReader(Stream stream)
            {
                this._stream = stream;
            }
            public string ReadPreamble()
            {
                return Encoding.ASCII.GetString(new byte[] { 
                    this.ReadByte(), 
                    this.ReadByte(), 
                    this.ReadByte(), 
                    this.ReadByte() });
            }
            public int ReadMajorVersion()
            {
                return this.ReadByte();
            }
            public int ReadMinorVersion()
            {
                return this.ReadByte();
            }
            public ushort ReadOperation()
            {
                return this.ReadUInt16();
            }
            public ushort ReadContentDelimiter()
            {
                return this.ReadUInt16();
            }
            public int ReadContentLength()
            {
                return this.ReadInt32();
            }
            public void ReadTransportHeaders()
            {
                ushort headerType;
                Console.WriteLine("HeaderType: {0}", headerType = this.ReadUInt16());
                while (headerType != TcpHeaders.EndOfHeaders)
                {
                    if (headerType == TcpHeaders.Custom)
                    {
                        Console.WriteLine("{0}: {1}", ReadCountedString(), ReadCountedString());
                    }
                    else if (headerType == TcpHeaders.RequestUri)
                    {
                        Console.WriteLine("RequestUri-Format: {0}", ReadByte());
                        Console.WriteLine("RequestUri: {0}", ReadCountedString());
                    }
                    else if (headerType == TcpHeaders.StatusCode)
                    {
                        Console.WriteLine("StatusCode-Format: {0}", ReadByte());
                        var code = ReadUInt16();
                        Console.WriteLine("StatusCode: {0}", code);
                        //if (code != 0) error = true;
                    }
                    else if (headerType == TcpHeaders.StatusPhrase)
                    {
                        Console.WriteLine("StatusPhrase-Format: {0}", ReadByte());
                        Console.WriteLine("StatusPhrase: {0}", ReadCountedString());
                    }
                    else if (headerType == TcpHeaders.ContentType)
                    {
                        Console.WriteLine("ContentType-Format: {0}", ReadByte());
                        Console.WriteLine("ContentType: {0}", ReadCountedString());
                    }
                    else
                    {
                        var headerFormat = (byte)ReadByte();

                        switch (headerFormat)
                        {
                            case TcpHeaderFormat.Void: break;
                            case TcpHeaderFormat.CountedString: ReadCountedString(); break;
                            case TcpHeaderFormat.Byte: ReadByte(); break;
                            case TcpHeaderFormat.UInt16: ReadUInt16(); break;
                            case TcpHeaderFormat.Int32: ReadInt32(); break;
                            default:
                                throw new RemotingException("Remoting_Tcp_UnknownHeaderType");
                        }
                    }
                }

            }
            public byte[] ReadBytes(int length)
            {
                var buffer = new byte[length];
                this._stream.Read(buffer, 0, length);
                return buffer;
            }

            private byte ReadByte()
            {
                var b = this._stream.ReadByte();
                return (byte)b;
            }
            private UInt16 ReadUInt16()
            {
                return (UInt16)(this.ReadByte() & 0xFF | this.ReadByte() << 8);
            }
            private int ReadInt32()
            {
                return (int)((this.ReadByte() & 0xFF)
                    | this.ReadByte() << 8
                    | this.ReadByte() << 16
                    | this.ReadByte() << 24);
            }
            
            private string ReadCountedString()
            {
                var format = this.ReadByte();
                int size = ReadInt32();

                if (size > 0)
                {
                    byte[] data = this.ReadBytes(size);

                    switch (format)
                    {
                        case TcpStringFormat.Unicode:
                            return Encoding.Unicode.GetString(data);

                        case TcpStringFormat.UTF8:
                            return Encoding.UTF8.GetString(data);

                        default:
                            throw new RemotingException("Remoting_Tcp_UnrecognizedStringFormat");
                    }
                }
                else
                {
                    return null;
                }
            }
        }
        public class ProtocolWriter
        {
            private static readonly byte[] PREAMBLE = Encoding.ASCII.GetBytes(".NET");

            private Stream _buffer;
            public ProtocolWriter() : this(null) { }
            public ProtocolWriter(Stream stream)
            {
                this._buffer = stream ?? new MemoryStream();
            }

            public void WritePreamble()
            {
                this.WriteBytes(PREAMBLE);
            }
            public void WriteMajorVersion()
            {
                this.WriteByte((byte)1);
            }
            public void WriteMinorVersion()
            {
                this.WriteByte((byte)0);
            }
            public void WriteOperation(ushort value)
            {
                this.WriteUInt16(value);
            }
            public void WriteContentDelimiter(ushort value)
            {
                this.WriteUInt16(value);
            }
            public void WriteContentLength(int value)
            {
                this.WriteInt32(value);
            }
            public void WriteTransportHeaders(string uri)
            {
                //this.WriteContentTypeHeader("application/octet-stream");
                //must write request-uri
                this.WriteRequestUriHeader(uri);
                this.WriteUInt16(TcpHeaders.EndOfHeaders);
            }
            public void WriteBytes(byte[] value)
            {
                this._buffer.Write(value, 0, value.Length);
            }

            private void WriteByte(byte value)
            {
                this._buffer.WriteByte(value);
            }
            private void WriteUInt16(UInt16 value)
            {
                this.WriteByte((byte)value);
                this.WriteByte((byte)(value >> 8));
            }
            private void WriteInt32(int value)
            {
                this.WriteByte((byte)value);
                this.WriteByte((byte)(value >> 8));
                this.WriteByte((byte)(value >> 16));
                this.WriteByte((byte)(value >> 24));
            }
            private void WriteRequestUriHeader(String value)
            {
                this.WriteUInt16(TcpHeaders.RequestUri);
                this.WriteByte(TcpHeaderFormat.CountedString);
                this.WriteCountedString(value);
            }
            private void WriteContentTypeHeader(String value)
            {
                this.WriteUInt16(TcpHeaders.ContentType);
                this.WriteByte(TcpHeaderFormat.CountedString);
                this.WriteCountedString(value);
            }
            private void WriteCustomHeader(String name, String value)
            {
                this.WriteUInt16(TcpHeaders.Custom);
                this.WriteCountedString(name);
                this.WriteCountedString(value);
            }
            private void WriteCountedString(String str)
            {
                // strings are formatted as follows [string length (int32)][string value (unicode)]
                int strLength = 0;
                if (str != null)
                    strLength = str.Length;

                if (strLength > 0)
                {
                    byte[] strBytes = Encoding.UTF8.GetBytes(str);

                    // write string format
                    WriteByte(TcpStringFormat.UTF8);

                    // write string data size 
                    WriteInt32(strBytes.Length);

                    // write string data 
                    WriteBytes(strBytes);
                }
                else
                {
                    // write string format
                    //   (just call it Unicode (doesn't matter since there is no data)) 
                    WriteByte(TcpStringFormat.Unicode);

                    // stream data size is 0. 
                    WriteInt32(0);
                }
            }
        }
    }
}
