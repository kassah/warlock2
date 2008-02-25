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
import java.io.FileInputStream;
import java.io.IOException;

import cc.warlock.core.configuration.ConfigurationUtil;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.settings.StormFrontServerSettings;
import cc.warlock.core.stormfront.settings.internal.StormFrontClientSettings;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


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
		if(clientVersion != null) {
//			handler.getClient().getServerSettings().setClientVersion(clientVersion);
		}
	}
	
	@Override
	public void handleEnd(String rawXML) {
		if (newSettings) {
			
			// This is a character that has no server settings, we need to immediately send our own
			StormFrontServerSettings.sendInitialServerSettings(handler.getClient());
			
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
			try {
				handler.getClient().getConnection().sendLine("");
				
				if (StormFrontServerSettings.instance().getClientVersion() == null)
				{
					FileInputStream stream = new FileInputStream(serverSettings);
					StormFrontClientSettings settings =
						(StormFrontClientSettings)handler.getClient().getStormFrontClientSettings();
					
					StormFrontServerSettings.instance().importServerSettings(
							stream, settings);					
					stream.close();
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
			
			// TODO we will need to un-comment this later when we add code to support merging of stormfront's settings
			// check against crc to see if we're up to date
//			StormFrontDocument document = ServerSettings.getDocument(playerId);
//			String currentCRC = ServerSettings.getCRC(document);
//			Integer currentMajorVersion = ServerSettings.getMajorVersion(document);
//			if (currentMajorVersion == null) currentMajorVersion = 0;
//			
//			if (currentCRC != null && crc.equals(currentCRC))
//			{
//				boolean sendBlankLine = true;
//				
//				handler.getClient().getServerSettings().load(playerId);
//				// crcs match, if we have the bigger major version, override with our settings
//				if (currentMajorVersion > majorVersion)
//				{
//					handler.getClient().getServerSettings().sendAllSettings();
//					sendBlankLine = false;
//				}
//				
//				if (sendBlankLine)
//				{
//					try {
//						handler.getClient().getConnection().sendLine("");
//					} catch(IOException e) {
//						e.printStackTrace();
//					}
//				}
//			} else {
//				System.out.println("our crc is: " + currentCRC + ", their crc is: " + crc);
//				try {
//					handler.getClient().getConnection().send("<sendSettings/>\n");
//				} catch(IOException e) {
//					e.printStackTrace();
//				}
//			}
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
