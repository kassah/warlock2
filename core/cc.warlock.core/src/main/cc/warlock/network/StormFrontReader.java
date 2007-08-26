package cc.warlock.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

public class StormFrontReader extends BufferedReader {

	protected StringBuffer currentLine = new StringBuffer();
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
			currentLine.append((char)c);
			if (((char)c) == '\n')
			{
				connection.dataReady(currentLine.toString());
				currentLine.setLength(0);
			}
		}
		return c;
	}

	private void appendChars (char[] cbuf, int start, int bytesRead)
	{
		if (bytesRead > 0)
		{
			currentLine.append(cbuf, start, bytesRead);
			int newLineIndex = currentLine.indexOf("\n");
			while (newLineIndex != -1)
			{
				String line = currentLine.substring(0, newLineIndex);
				connection.dataReady(line);
				currentLine.replace(0, newLineIndex+1, "");
				newLineIndex = currentLine.indexOf("\n");
			}
		}
	}
	
	@Override
	public int read(char[] cbuf) throws IOException {
		int bytesRead = super.read(cbuf);
		appendChars(cbuf, 0, bytesRead);
		
		return bytesRead;
	}
	
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		int bytesRead = super.read(cbuf, off, len);
		appendChars(cbuf, off, bytesRead);
		
		return bytesRead;
	}
	
	@Override
	public int read(CharBuffer target) throws IOException {
		int bytesRead = super.read(target);
		appendChars(target.array(), 0, bytesRead);
		
		return bytesRead;
	}
}
