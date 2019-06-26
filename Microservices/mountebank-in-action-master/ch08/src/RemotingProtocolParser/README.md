Remoting Protocol Parser
======================

Help communicating with .NET Remoting via MS build-in Binary or SOAP.

You can refer it to build your CrossPlatform-ServiceFramework/RPC written by other language.

Serialization is the main question finally, you need implement Other FormatterSink using JSON/Protobuf/... :)


## Protocal parser logic:

- TCP Channel

```c#
//Read
//1. Read Preamble, will be ".Net"
//2. MajorVersion, will be 1
//3. MinorVersion, will be 0
//4. Operation, will be 5 (request,onewayrequest...)
//5. TcpContentDelimiter and ContentLength
//6. Read Headers(ITransportHeaders)
//7. RequestStream/Message

//Write
//same as read

```

- HTTP Channel

```c#
//Read
//1. Read FirstLine of HTTP Request, like "POST /remote.rem HTTP/1.1"
//2. Read Http Headers, find out "Content-Length" or Chunked
//3. RequestStream/Message

//Write
//you can just using webclient sending common httprequest
```

## Upcoming

- [X] HTTP Parser
- [] Buffer Improvement (Pooling/Management)
- [] NSF supporing
- [] .Net Remoting MockServer for testing
- [x] Parser of NodeJS (need json/soap or other formatterSink)

## Later

- [-] Parser of PHP
- [X] Parser of Java

[x] : done.

## Not Support

- Chunked (I think it useless for RPC)
- SOAP via TCP/HTTP (you can use other standard lib or impl yourself)

## Reference

[[MS-NRTP]: .NET Remoting: Core Protocol](http://msdn.microsoft.com/en-us/library/cc237297(v=prot.20).aspx)

[.NET Remoting](https://github.com/wsky/System.Runtime.Remoting)

[MONO Remoting MessageIO](https://github.com/mono/mono/blob/master/mcs/class/System.Runtime.Remoting/System.Runtime.Remoting.Channels.Tcp/TcpMessageIO.cs)

[A Note Of Remoting Protocol](https://github.com/ali-ent/apploader/issues/4)

The .NET remoting framework was designed to be extensible.

http://books.google.com.hk/books?id=rbW3qBrWiAcC&printsec=frontcover&hl=zh-CN#v=onepage&q&f=false

Custom RealProxy that you can dynamic invoke remoting:

https://github.com/wsky/RemotingProtocolParser/blob/master/csharp/RemotingProtocolParserTest/CustomRemotingProxyTest.cs

[Async Sockets and Buffer Management](http://codebetter.com/gregyoung/2007/06/18/async-sockets-and-buffer-management/)

https://gist.github.com/4167477

## License

(The MIT License)

Copyright (C) 2012 wsky (wskyhx at gmail.com) and other contributors

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.