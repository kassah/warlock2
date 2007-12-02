package cc.warlock.core.stormfront.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;

import cc.warlock.core.stormfront.client.IStormFrontClient;

public class StormFrontReader extends InputStreamReader {

	private StormFrontConnection connection;
	private StringBuffer recentText = new StringBuffer();
	
	public StormFrontReader (StormFrontConnection connection, InputStream stream)
	{
		super(stream);
		this.connection = connection;
	}

	@Override
	public int read() throws IOException {
		if(!this.ready())
			flush();
		
		int c = super.read();
		if (c != -1)
		{
			recentText.append((char)c);
		}
		return c;
	}

	private void appendChars (char[] cbuf, int start, int bytesRead) throws IOException
	{
		if (bytesRead > 0)
		{	
			recentText.append(cbuf, start, bytesRead);
		}
	}
	
	@Override
	public int read(char[] cbuf) throws IOException {
		if(!this.ready())
			flush();
		
		int bytesRead = super.read(cbuf);
		appendChars(cbuf, 0, bytesRead);
		
		return bytesRead;
	}
	
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		if(!this.ready())
			flush();
		
		int bytesRead = super.read(cbuf, off, len);
		appendChars(cbuf, off, bytesRead);
		
		return bytesRead;
	}
	
	@Override
	public int read(CharBuffer target) throws IOException {
		if(!this.ready())
			flush();
		
		int bytesRead = super.read(target);
		appendChars(target.array(), 0, bytesRead);
		
		return bytesRead;
	}
	
	private void flush() {
		if(recentText.length() > 0) {
			((IStormFrontClient) connection.getClient()).flushStreams();
			connection.dataReady(recentText.toString());
			recentText.setLength(0);
		}
	}
}
