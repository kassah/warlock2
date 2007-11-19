package cc.warlock.core.stormfront.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;

public class StormFrontReader extends InputStreamReader {

	protected StormFrontConnection connection;
	
	public StormFrontReader (StormFrontConnection connection, InputStream stream)
	{
		super(stream);
		this.connection = connection;
	}

	public int read() throws IOException {
		int c = super.read();
		if (c != -1)
		{
			connection.dataReady(String.valueOf(c));
		}
		return c;
	}
	
	@Override
	public int read(char[] cbuf) throws IOException {
		int bytesRead = super.read(cbuf);
		connection.dataReady(String.valueOf(cbuf));
		
		return bytesRead;
	}
	
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		int bytesRead = super.read(cbuf, off, len);
		if(bytesRead != -1)
			connection.dataReady(String.valueOf(cbuf, off, bytesRead));
		
		return bytesRead;
	}
	
	@Override
	public int read(CharBuffer target) throws IOException {
		int bytesRead = super.read(target);
		if(bytesRead != -1)
			connection.dataReady(target.toString());
		
		return bytesRead;
	}
}
