package remoting.protocol;

import java.io.UnsupportedEncodingException;

public class CompatibleUtil {
	public static String newString(byte[] data, String charset) {
		try {
			return new String(data, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static byte[] getBytes(String input, String charset) {
		try {
			return input.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
