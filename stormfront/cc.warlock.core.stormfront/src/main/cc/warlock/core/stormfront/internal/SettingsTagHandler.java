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
import cc.warlock.core.stormfront.client.IStormFrontClientViewer;
import cc.warlock.core.stormfront.xml.StormFrontAttribute;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;
import cc.warlock.core.stormfront.xml.StormFrontDocument;


public class SettingsTagHandler extends DefaultTagHandler {

	private StringBuffer buffer = new StringBuffer();
	private SettingsInfoTagHandler infoTagHandler;
	
	public SettingsTagHandler(IStormFrontProtocolHandler handler, SettingsInfoTagHandler infoTagHandler) {
		super(handler);
		this.infoTagHandler = infoTagHandler;
		
		//addTagHandler(new SettingsElementsTagHandler(handler, this));
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
	public void handleStart(StormFrontAttributeList attributes) {
		buffer.setLength(0);
		
		buffer.append("<settings crc=\"" + infoTagHandler.getCRC() +
				"\" major=\"" + infoTagHandler.getMajorVersion() +
				"\" client=\"" + infoTagHandler.getClientVersion() + "\">\n");
		
		visitViewers(new ViewerVisitor() {
			public void visit(IStormFrontClientViewer viewer) {
				viewer.startDownloadingServerSettings();
			}
		});
	}

	@Override
	public boolean handleChild(String name, StormFrontAttributeList attributes) {
		String startTag = "<" + name;
		if (attributes != null) {
            for (StormFrontAttribute attribute : attributes.getList())
            {
                startTag += " " + attribute.getName() + "=" +
                	attribute.getQuoteType() + 
                	StringEscapeUtils.escapeXml(attribute.getValue()) +
                	attribute.getQuoteType();
            }
        }
		startTag += ">";
		buffer.append(startTag);
		
		return true;
	}
	
	
	@Override
	public boolean handleEndChild(String name) {
		
		buffer.append("</" + name + ">");
		
		return true;
	}
	
	@Override
	public void handleEnd() {
		buffer.append("</settings>");
		
		String playerId = handler.getClient().getPlayerId().get();
		File serverSettings = ConfigurationUtil.getConfigurationFile("serverSettings_" + playerId + ".xml");
		try {
			FileWriter writer = new FileWriter(serverSettings);

			InputStream inStream = new ByteArrayInputStream(buffer.toString().getBytes());
			StormFrontDocument document = new StormFrontDocument(inStream);
			document.saveTo(writer, true);
			
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
		buffer.append(StringEscapeUtils.escapeXml(characters));
		return true;
	}
}
