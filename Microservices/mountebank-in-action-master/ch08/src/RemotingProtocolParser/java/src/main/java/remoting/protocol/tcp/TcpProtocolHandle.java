/*
    (The MIT License)

    Copyright (C) 2012 wsky (wskyhx at gmail.com) and other contributors

    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package remoting.protocol.tcp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map.Entry;

import remoting.protocol.CompatibleUtil;
import remoting.protocol.NotSupportedException;
import remoting.protocol.ProtocolStreamHandle;

//.Net Remoting Protocol (via TCP) Parser
public class TcpProtocolHandle extends ProtocolStreamHandle {
	private final static byte[] PREAMBLE = ".NET".getBytes();
	
	public TcpProtocolHandle(ByteBuffer source) {
		super(source);
	}
	
	// / <summary>remoting protocol premable, expected value is ".NET".
	// / </summary>
	// / <returns></returns>
	public String ReadPreamble()
	{
		return new String(new byte[] {
				(byte) this.ReadByte(),
				(byte) this.ReadByte(),
				(byte) this.ReadByte(),
				(byte) this.ReadByte() });
	}
	
	public void WritePreamble()
	{
		this.WriteBytes(PREAMBLE);
	}
	
	// / <summary>remoting protocol majorVersion, expected value is "1".
	// / </summary>
	// / <returns></returns>
	public int ReadMajorVersion()
	{
		return this.ReadByte();
	}
	
	public void WriteMajorVersion()
	{
		this.WriteByte((byte) 1);
	}
	
	// / <summary>remoting protocol minorVersion, expected value is "0".
	// / </summary>
	// / <returns></returns>
	public int ReadMinorVersion()
	{
		return this.ReadByte();
	}
	
	public void WriteMinorVersion()
	{
		this.WriteByte((byte) 0);
	}
	
	// / <summary>remoting operation code, eg Request/OneWayRequest/Reply
	// / </summary>
	// / <returns></returns>
	public short ReadOperation()
	{
		return this.ReadUInt16();
	}
	
	// / <summary>write remoting operation code
	// / </summary>
	// / <param name="value">Request/OneWayRequest/Reply</param>
	public void WriteOperation(short value)
	{
		this.WriteUInt16(value);
	}
	
	// / <summary>Chunked or Fixed ContentLength. Only http channel support
	// currently.
	// / </summary>
	// / <returns></returns>
	public short ReadContentDelimiter()
	{
		return this.ReadUInt16();
	}
	
	// / <summary>ContentLength=0, Chunked=1
	// / </summary>
	// / <param name="value"></param>
	public void WriteContentDelimiter(short value)
	{
		this.WriteUInt16(value);
	}
	
	// / <summary>get message content length
	// / </summary>
	// / <returns></returns>
	public int ReadContentLength()
	{
		return this._contentLength = this.ReadInt32();
	}
	
	public void WriteContentLength(int value)
	{
		this.WriteInt32(this._contentLength = value);
	}
	
	public HashMap<String, Object> ReadTransportHeaders() throws NotSupportedException
	{
		HashMap<String, Object> dict = new HashMap<String, Object>();
		short headerType = this.ReadUInt16();
		
		while (headerType != TcpHeaders.EndOfHeaders)
		{
			if (headerType == TcpHeaders.Custom)
			{
				dict.put(this.ReadCountedString(), this.ReadCountedString());
			}
			else if (headerType == TcpHeaders.RequestUri)
			{
				this.ReadByte();// RequestUri-Format
				dict.put(TcpTransportHeader.RequestUri, this.ReadCountedString());
			}
			else if (headerType == TcpHeaders.StatusCode)
			{
				this.ReadByte();// StatusCode-Format
				dict.put(TcpTransportHeader.StatusCode, this.ReadUInt16());
				// HACK:error curse when StatusCode!=0
				// if (code != 0) error = true;
			}
			else if (headerType == TcpHeaders.StatusPhrase)
			{
				this.ReadByte();// StatusPhrase-Format
				dict.put(TcpTransportHeader.StatusPhrase, this.ReadCountedString());
			}
			else if (headerType == TcpHeaders.ContentType)
			{
				this.ReadByte();// ContentType-Format
				dict.put(TcpTransportHeader.ContentType, this.ReadCountedString());
			}
			else if (this.readExtendedHeader(headerType, dict))
			{
				
			}
			else
			{
				byte headerFormat = (byte) ReadByte();
				
				switch (headerFormat)
				{
				case TcpHeaderFormat.Void:
					break;
				case TcpHeaderFormat.CountedString:
					this.ReadCountedString();
					break;
				case TcpHeaderFormat.Byte:
					this.ReadByte();
					break;
				case TcpHeaderFormat.UInt16:
					this.ReadUInt16();
					break;
				case TcpHeaderFormat.Int32:
					this.ReadInt32();
					break;
				default:
					throw new NotSupportedException();
				}
			}
			
			headerType = this.ReadUInt16();
		}
		return dict;
	}
	
	// / <summary>write transport header. PS: "RequestUri" must be transport
	// while request call
	// / </summary>
	// / <param name="headers"></param>
	public void WriteTransportHeaders(HashMap<String, Object> headers)
	{
		if (headers != null)
			for (Entry<String, Object> i : headers.entrySet())
			{
				if (i.getKey().equalsIgnoreCase(TcpTransportHeader.StatusCode))
					this.WriteStatusCodeHeader(Short.parseShort(i.getValue().toString()));
				else if (i.getKey().equalsIgnoreCase(TcpTransportHeader.StatusPhrase))
					this.WriteStatusPhraseHeader(i.getValue().toString());
				else if (i.getKey().equalsIgnoreCase(TcpTransportHeader.ContentType))
					this.WriteContentTypeHeader(i.getValue().toString());
				else if (i.getKey().equalsIgnoreCase(TcpTransportHeader.RequestUri))
					// Request-Uri must be transport while request call
					this.WriteRequestUriHeader(i.getValue().toString());
				else if (this.writeExtendedHeader(i))
					;
				else
					this.WriteCustomHeader(i.getKey(), i.getValue().toString());
			}
		this.WriteUInt16(TcpHeaders.EndOfHeaders);
	}
	
	protected boolean readExtendedHeader(short headerType, HashMap<String, Object> dict) throws NotSupportedException {
		return false;
	}
	
	protected boolean writeExtendedHeader(Entry<String, Object> entry) {
		return false;
	}
	
	protected final short ReadUInt16()
	{
		return (short) (this.ReadByte() & 0xFF | this.ReadByte() << 8);
	}
	
	protected final void WriteUInt16(short value)
	{
		this.WriteByte((byte) value);
		this.WriteByte((byte) (value >> 8));
	}
	
	protected int ReadInt32()
	{
		this._source.order(ByteOrder.LITTLE_ENDIAN);
		int value = this._source.getInt();
		this._source.order(ByteOrder.BIG_ENDIAN);
		return value;
	}
	
	protected void WriteInt32(int value)
	{
		this._source.order(ByteOrder.LITTLE_ENDIAN);
		this._source.putInt(value);
		this._source.order(ByteOrder.BIG_ENDIAN);
	}
	
	protected final String ReadCountedString() throws NotSupportedException
	{
		byte format = (byte) this.ReadByte();
		int size = ReadInt32();
		
		if (size > 0)
		{
			byte[] data = this.ReadBytes(size);
			
			switch (format)
			{
			case TcpStringFormat.Unicode:
				return CompatibleUtil.newString(data, "unicode");
				
			case TcpStringFormat.UTF8:
				return CompatibleUtil.newString(data, "UTF-8");
				
			default:
				throw new NotSupportedException();
			}
		}
		else
		{
			return null;
		}
	}
	
	protected final void WriteCountedString(String value)
	{
		int strLength = 0;
		if (value != null)
			strLength = value.length();
		
		if (strLength > 0)
		{
			byte[] strBytes = CompatibleUtil.getBytes(value, "UTF-8");
			this.WriteByte(TcpStringFormat.UTF8);
			this.WriteInt32(strBytes.length);
			this.WriteBytes(strBytes);
		}
		else
		{
			// just call it Unicode (doesn't matter since there is no data)
			this.WriteByte(TcpStringFormat.Unicode);
			this.WriteInt32(0);
		}
	}
	
	private void WriteRequestUriHeader(String value)
	{
		// value maybe "application/octet-stream"
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
	
	private void WriteStatusCodeHeader(short value)
	{
		this.WriteUInt16(TcpHeaders.StatusCode);
		this.WriteByte(TcpHeaderFormat.UInt16);
		this.WriteUInt16(value);
	}
	
	private void WriteStatusPhraseHeader(String value)
	{
		this.WriteUInt16(TcpHeaders.StatusPhrase);
		this.WriteByte(TcpHeaderFormat.CountedString);
		this.WriteCountedString(value);
	}
	
	private void WriteCustomHeader(String name, String value)
	{
		this.WriteUInt16(TcpHeaders.Custom);
		this.WriteCountedString(name);
		this.WriteCountedString(value);
	}
}
