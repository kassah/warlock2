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

import java.io.File;
import java.io.IOException;

import cc.warlock.core.configuration.ConfigurationUtil;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.serversettings.server.ServerSettings;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;
import cc.warlock.core.stormfront.xml.StormFrontDocument;


public class SettingsInfoTagHandler extends DefaultTagHandler {

	protected String crc, clientVersion;
	protected int majorVersion;
	protected boolean newSettings = false;
	
	public SettingsInfoTagHandler(IStormFrontProtocolHandler handler) {
		super (handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "settingsInfo" };
	}
	
	public void setNewSettings(boolean newSettings) {
		this.newSettings = newSettings;
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes, String rawXML) {
		
		if (attributes.getAttribute("space") != null
			&& attributes.getAttribute("not") != null
			&& attributes.getAttribute("found") != null)
		{
			newSettings = true;
			return;
		}
		
		crc = attributes.getValue("crc");
		
		String major = attributes.getValue("major");
		if(major != null) {
			majorVersion = Integer.parseInt(attributes.getValue("major"));
		}
		
		clientVersion = attributes.getValue("client");
		if(clientVersion != null)
			handler.getClient().getServerSettings().setClientVersion(clientVersion);
	}
	
	@Override
	public void handleEnd(String rawXML) {
		if (newSettings) {
			
			// This is a character that has no server settings, we need to immediately send our own
			handler.getClient().getServerSettings().sendInitialServerSettings();
			
			PromptTagHandler promptHandler = (PromptTagHandler) handler.getTagHandler(PromptTagHandler.class);
			promptHandler.setWaitingForInitialStreams(true);
			newSettings = false;
			return;
		}
		
		String playerId = handler.getClient().getPlayerId().get();
		
		File serverSettings = ConfigurationUtil.getConfigurationFile("serverSettings_" + playerId + ".xml", false);
		if (!serverSettings.exists())
		{
			try {
				handler.getClient().getConnection().send("<sendSettings/>\n");
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else {
			// check against crc to see if we're up to date
			StormFrontDocument document = ServerSettings.getDocument(playerId);
			//String currentCRC = ServerSettings.getCRC(document);
			Integer currentMajorVersion = ServerSettings.getMajorVersion(document);
			if (currentMajorVersion == null) currentMajorVersion = 0;
			
				
			handler.getClient().getServerSettings().load(playerId);
			// 
			if (currentMajorVersion > majorVersion)
			{
				handler.getClient().getServerSettings().sendAllSettings();
			} else if(currentMajorVersion < majorVersion) {
				try {
					handler.getClient().getConnection().send("<sendSettings/>\n");
				} catch(IOException e) {
					e.printStackTrace();
				}
			} else { // major versions are equal
				try {
					// increment our version
					handler.getClient().getServerSettings().incrementMajorVersion();
					handler.getClient().getConnection().sendLine("");
				} catch(IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
	
	public String getCRC () {
		return crc;
	}
	
	public Integer getMajorVersion () {
		return majorVersion;
	}
	
	public String getClientVersion () {
		return clientVersion;
	}

}
