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
package cc.warlock.core.stormfront.internal;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringEscapeUtils;

import cc.warlock.core.configuration.ConfigurationUtil;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;
import cc.warlock.core.stormfront.xml.StormFrontDocument;


public class CmdlistTagHandler extends DefaultTagHandler {

	private StringBuffer buffer = new StringBuffer();
	private int count = 0;
	
	public CmdlistTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}

	@Override
	public String[] getTagNames() {
		return new String[] { "cmdlist" };
	}

	@Override
	public boolean handleStartChild(String name, StormFrontAttributeList attributes,
			String rawXML, boolean newLine) {
		buffer.append(rawXML);
		count++;
		
		return true;
	}
	
	
	@Override
	public boolean handleEndChild(String name, String rawXML, boolean newLine) {
		
		buffer.append(rawXML);
		
		return true;
	}
	
	public void writeOut(String timestamp) {
		
		File cmdList = ConfigurationUtil.getConfigurationFile("cmdlist1.xml");
		try {
			FileWriter writer = new FileWriter(cmdList);

			buffer.insert(0, "<cmdlist timestamp=\"" + timestamp + "\" count=\"" + count + "\">");
			buffer.append("</cmdlist>");
			
			InputStream inStream = new ByteArrayInputStream(buffer.toString().getBytes());
			StormFrontDocument document = new StormFrontDocument(inStream);
			document.saveTo(writer, true);
			inStream.close();
			
			writer.close();
			buffer.setLength(0);
			count = 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean handleCharacters(String characters) {
		System.out.print(characters);
		buffer.append(StringEscapeUtils.escapeXml(characters));
		return true;
	}
}
