/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
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
