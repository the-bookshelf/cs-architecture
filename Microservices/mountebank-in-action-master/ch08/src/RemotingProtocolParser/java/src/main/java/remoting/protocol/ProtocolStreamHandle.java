package remoting.protocol;

import java.nio.ByteBuffer;

//Remoting Protocol stream read/write
//ushort/uint...
//http://www.harding.edu/fmccown/java_csharp_comparison.html
public class ProtocolStreamHandle {
	protected int _contentLength = -1;
	protected ByteBuffer _source;
	
	protected ProtocolStreamHandle(ByteBuffer source) {
		this._source = source;
	}
	
	protected int ReadByte() {
		byte b = this._source.get();
		if (b > -1)
			return b;
		else
			return -1;
	}

	protected void WriteByte(byte value) {
		this._source.put(value);
	}

	protected byte[] ReadBytes(int length) {
		byte[] buffer = new byte[length];
		this._source.get(buffer, 0, length);
		return buffer;
	}

	protected void WriteBytes(byte[] value) {
		this._source.put(value, 0, value.length);
	}

	public byte[] ReadContent() {
		return this.ReadBytes(this._contentLength);
	}

	public void WriteContent(byte[] value) {
		this.WriteBytes(value);
	}
}
