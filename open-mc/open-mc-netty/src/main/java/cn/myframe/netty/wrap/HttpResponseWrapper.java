/**
 * 
 */
package cn.myframe.netty.wrap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 响应对象
 * 
 * @author  ynz
 * @email   ynz@myframe.cn
 * @version 创建时间：2018年6月25日 上午9:03:43
 */
public class HttpResponseWrapper {
	private Map<String, String> headers = new HashMap<String, String>();
	private int statusCode = 200;
	private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	public Map<String, String> headers() {
		return headers;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public void write(byte[] bytes) throws IOException {
		outputStream.write(bytes);
	}

	public void write(InputStream is) throws IOException {
		//IoKit.copy(is, outputStream);
	}

	/**
	 * @return the outputStream
	 */
	public final OutputStream getOutputStream() {
		return outputStream;
	}

	public byte[] content() {
		return outputStream.toByteArray();
	}
}
