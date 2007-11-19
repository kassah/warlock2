package cc.warlock.core.stormfront.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

public class StormFrontReader extends BufferedReader {

	protected StormFrontConnection connection;
	
	public StormFrontReader (StormFrontConnection connection, Reader reader)
	{
		super(reader);
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
		connection.dataReady(String.valueOf(cbuf, off, bytesRead));
		
		return bytesRead;
	}
	
	@Override
	public int read(CharBuffer target) throws IOException {
		int bytesRead = super.read(target);
		connection.dataReady(target.toString());
		
		return bytesRead;
	}
}
