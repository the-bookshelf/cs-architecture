/*
    (The MIT License)

    Copyright (C) 2012 wsky (wskyhx at gmail.com) and other contributors

    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

var ENCODING_ASCII 		= 'ascii',
	ENCODING_UTF8 		= 'utf-8',
	ENCODING_UNICODE 	= 'ucs2';

module.exports.tcpWriter = function(socket) {
	var w = {};

	w.writePreamble = function() {
		writeString('.NET', ENCODING_ASCII)
	};
	w.writeMajorVersion = function() {
		writeByte(1);
	};
	w.writeMinorVersion = function() { 
		writeByte(0);
	};
	w.writeOperation = function(opr) {
		writeUInt16(opr);
	};
	w.writeContentDelimiter = function() { 
		writeUInt16(0);
	};
	w.writeContentLength = function(length) {
		writeInt32(length);
	};
	w.writeHeaders = function(headers) {
		if(headers != null)
			for(var k in headers) {
				if(k == TcpTransportHeader.StatusCode)
					writeStatusCodeHeader(headers[k]);
				else if(k == TcpTransportHeader.StatusPhrase)
					writeStatusPhraseHeader(headers[k]);
				else if(k == TcpTransportHeader.ContentType)
					writeContentTypeHeader(headers[k]);
				else if(k == TcpTransportHeader.RequestUri)
					writeRequestUriHeader(headers[k]);
				else
					writeCustomHeader(k, headers[k]);
			}
		//end of header
		writeUInt16(0);
	};
	w.writeContent = function(v) {
		socket.write(v);
	};

	//TODO:need buffer pooling
	function writeByte(v) {
		socket.write(new Buffer([v]));
	}
	function  writeBytes(v) {
		socket.write(v);
	}
	function writeString(v, e) {
		socket.write(new Buffer(v, e));
	}
	function writeUInt16(v) {
		var buffer = new Buffer(2);
		buffer.writeUInt16LE(v, 0);
		socket.write(buffer);
	}
	function writeInt32(v) {
		var buffer = new Buffer(4);
		buffer.writeInt32LE(v, 0);
		socket.write(buffer);
	}
	function writeCountedString(v) {
		var strLength = 0;
        if (v != null)
            strLength = v.length;

        if (strLength > 0)
        {
            var strBytes = new Buffer(v, ENCODING_UTF8);
            writeByte(TcpStringFormat.UTF8);
            writeInt32(strBytes.length);
            writeBytes(strBytes);
        }
        else
        {
            //just call it Unicode (doesn't matter since there is no data)
            writeByte(TcpStringFormat.Unicode);
            writeInt32(0);
        }
	}
	function writeRequestUriHeader(v) {
		writeUInt16(TcpHeaders.RequestUri);
		writeByte(TcpHeaderFormat.CountedString);
		writeCountedString(v);
	}
	function writeContentTypeHeader(v) {
        writeUInt16(TcpHeaders.ContentType);
		writeByte(TcpHeaderFormat.CountedString);
		writeCountedString(v);
    }
    function writeStatusCodeHeader(v) {
        writeUInt16(TcpHeaders.StatusCode);
		writeByte(TcpHeaderFormat.UInt16);
		writeUInt16(v);
    }
	function writeStatusPhraseHeader(v) {
        writeUInt16(TcpHeaders.StatusPhrase);
		writeByte(TcpHeaderFormat.CountedString);
		writeCountedString(v);
    }
    function writeCustomHeader(k, v) {
    	writeUInt16(TcpHeaders.Custom);
		writeCountedString(k);
		writeCountedString(v);
    }

	return w;
}
module.exports.tcpReader = function(buffer) {
	var r = {};
	r.offset = -1;
	r.contentLength = -1;
	r.readPreamble = function() {
		return readString(4, ENCODING_ASCII);
	};
	r.readMajorVersion = function() { 
		return readByte();//4
	};
	r.readMinorVersion = function() { 
		return readByte();//5
	};
	r.readOperation = function() {
		return readUInt16();
	};
	r.readContentDelimiter = function() { 
		return readUInt16();
	};
	r.readContentLength = function() {
		return r.contentLength = readInt32();
	};
	r.readHeaders = function() {
		var headers = {};
		var headerType = readUInt16();
		while(headerType != TcpHeaders.EndOfHeaders) {
			if(headerType == TcpHeaders.Custom) {
				headers[readCountedString()] = readCountedString();
			} else if(headerType == TcpHeaders.RequestUri) {
				readByte();
				headers[TcpTransportHeader.RequestUri] = readCountedString();
			} else if(headerType == TcpHeaders.StatusCode) {
				readByte();
				headers[TcpTransportHeader.StatusCode] = readUInt16();
			} else if(headerType == TcpHeaders.StatusPhrase) {
				readByte();
				headers[TcpTransportHeader.StatusPhrase] = readCountedString();
			} else if(headerType == TcpHeaders.ContentType) {
				readByte();
				headers[TcpTransportHeader.ContentType] = readCountedString();
			} else {
				var format = readByte();
				switch(format) {
					case TcpHeaderFormat.Void: break;
                    case TcpHeaderFormat.CountedString: readCountedString(); break;
                    case TcpHeaderFormat.Byte: readByte(); break;
                    case TcpHeaderFormat.UInt16: readUInt16(); break;
                    case TcpHeaderFormat.Int32: readInt32(); break;
                    default: throw new Error('HeaderFormat Not Support: ' + format);
				}
			}
			headerType = readUInt16();
		}
		return headers;
	};
	r.readContent = function() {
		var ret = new Buffer(r.contentLength);
		buffer.copy(ret, 0, ++r.offset, r.offset+ r.contentLength);
		r.offset= r.offset + r.contentLength - 1;
		return ret;
	};

	function readCountedString() {
		var format = readByte();
		var size = readInt32();

		if(size > 0){
			if(format == TcpStringFormat.Unicode)
				return readString(size, ENCODING_UNICODE);
			else if(format == TcpStringFormat.UTF8)
				return readString(size, ENCODING_UTF8);
			else
				throw new Error('StringFormat Not Support: ' + format);
		}else{
			return null;
		}
	}
	function readString(size, e) {
		var ret = buffer.toString(e, ++r.offset, r.offset + size);
		 r.offset= r.offset + size - 1;
		 return ret;
	}
	function readUInt16() {
		var ret = buffer.readUInt16LE(++r.offset);
		r.offset += 1;
		return ret;
	}
	function readInt32() {
		var ret = buffer.readInt32LE(++r.offset);
		r.offset += 3;
		return ret;
	}
	function readByte() {
		return buffer[++r.offset];
	}
	return r;
}

var TcpHeaders = {
	EndOfHeaders 	: 0,
	Custom 			: 1,
	StatusCode 		: 2,
	StatusPhrase 	: 3,
	RequestUri 		: 4,
	CloseConnection : 5,
	ContentType 	: 6
};
var TcpHeaderFormat = {
	Void 			: 0,
    CountedString 	: 1,
    Byte 			: 2,
    UInt16 			: 3,
    Int32 			: 4
}
var TcpStringFormat = {
	Unicode 		: 0,
    UTF8 			: 1
}
var TcpOperations = {
	Request 		: 0,
	OneWayRequest 	: 1,
	Reply 			: 2
}
var TcpContentDelimiter = {
	ContentLength 	: 0,
	Chunked 		: 1
}
var TcpTransportHeader = {
    RequestUri 		: 'RequestUri',
    ContentType 	: 'ContentType',
    StatusCode 		: 'StatusCode',
    StatusPhrase 	: 'StatusPhrase'
}

module.exports.Flags = function(){
	return {
 		TcpOperations		: TcpOperations,
	 	TcpTransportHeader 	: TcpTransportHeader
	}
}