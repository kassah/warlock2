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

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.configuration.ConfigurationUtil;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.IStormFrontTagHandler;
import cc.warlock.core.stormfront.client.IStormFrontClientViewer;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;
import cc.warlock.core.stormfront.xml.StormFrontDocument;


public class SettingsTagHandler extends DefaultTagHandler {

	private StringBuffer buffer = new StringBuffer();
	private IStormFrontTagHandler subElements;
	
	public SettingsTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
		
		subElements = new SettingsElementsTagHandler(handler, this);
	}

	protected static interface ViewerVisitor {
		public void visit (IStormFrontClientViewer viewer);
	}
	
	protected void visitViewers (ViewerVisitor visitor)
	{
		for (IWarlockClientViewer viewer : handler.getClient().getViewers())
		{
			if (viewer instanceof IStormFrontClientViewer)
			{
				IStormFrontClientViewer sfViewer = (IStormFrontClientViewer) viewer;
				visitor.visit(sfViewer);
			}
		}
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "settings" };
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes, String rawXML) {
		buffer.setLength(0);
		
		buffer.append(rawXML);
		/*buffer.append("<settings");
		String client = attributes.getValue("client");
		if(client != null) buffer.append(" client=\"" + client + "\"");
		String major = attributes.getValue("major");
		if(major != null) buffer.append(" major=\"" + (Integer.parseInt(major) + 1) + "\"");
		buffer.append(">\n");*/
		
		visitViewers(new ViewerVisitor() {
			public void visit(IStormFrontClientViewer viewer) {
				viewer.startDownloadingServerSettings();
			}
		});
	}
	
	@Override
	public void handleEnd(String rawXML) {
		buffer.append(rawXML);
		
		String playerId = handler.getClient().getPlayerId().get();
		File serverSettings = ConfigurationUtil.getConfigurationFile("serverSettings_" + playerId + ".xml");
		try {
			FileWriter writer = new FileWriter(serverSettings);

			InputStream inStream = new ByteArrayInputStream(buffer.toString().getBytes());
			StormFrontDocument document = new StormFrontDocument(inStream);
			document.saveTo(writer, false);
			
			inStream.close();
			writer.close();
			buffer.setLength(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		handler.getClient().getServerSettings().load(handler.getClient().getPlayerId().get());
		visitViewers(new ViewerVisitor() {
			public void visit(IStormFrontClientViewer viewer) {
				viewer.finishedDownloadingServerSettings();
			}
		});

	}
	
	@Override
	public boolean handleCharacters(String characters) {
		System.out.print(characters);
		buffer.append(StringEscapeUtils.escapeXml(characters));
		return true;
	}
	
	public void append(String text) {
		buffer.append(text);
	}
	
	@Override
	public IStormFrontTagHandler getTagHandler(String tagName) {
		return subElements;
	}
}
