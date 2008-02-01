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
package cc.warlock.core.stormfront.script.wsl;

import java.io.IOException;
import java.io.Reader;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CharStream;

public class ANTLRNoCaseReaderStream  extends ANTLRReaderStream
{
	public ANTLRNoCaseReaderStream(Reader reader) throws IOException
	{
		super(reader);
	}

	public int LA(int i) {
		if ( i==0 ) {
			return 0; // undefined
		}
		if ( i<0 ) {
			i++; // e.g., translate LA(-1) to use offset 0 
		}

		if ( (p+i-1) >= n ) {
			return CharStream.EOF;
		}
		return Character.toLowerCase(data[p+i-1]);
	}
}