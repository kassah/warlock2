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
import cc.warlock.core.stormfront.client.internal.StormFrontClient;
import cc.warlock.core.stormfront.settings.StormFrontServerSettings;
import cc.warlock.core.stormfront.settings.internal.StormFrontClientSettings;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


public class SettingsInfoTagHandler extends DefaultTagHandler {
	
	public SettingsInfoTagHandler(IStormFrontProtocolHandler handler) {
		super (handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "settingsInfo" };
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes, String rawXML) {
		
		if (attributes.getAttribute("space") != null
			&& attributes.getAttribute("not") != null
			&& attributes.getAttribute("found") != null)
		{
			// This is a character that has no server settings, we need to immediately send our own
			StormFrontServerSettings.sendInitialServerSettings(handler.getClient());
			
			PromptTagHandler promptHandler = (PromptTagHandler) handler.getTagHandler(PromptTagHandler.class);
			promptHandler.setWaitingForInitialStreams(true);
			return;
		}
		
		/* At some point we should check these values, and if the server sent
		 * us a version newer than our archived version of their settings
		 * we should prompt the user asking if they want to add the difference.
		String crc = attributes.getValue("crc");
		
		String major = attributes.getValue("major");
		if(major != null) {
			majorVersion = Integer.parseInt(attributes.getValue("major"));
		}
		
		String clientVersion = attributes.getValue("client");
		if(clientVersion != null) {
			handler.getClient().getServerSettings().setClientVersion(clientVersion);
		}
		*/
		
		String playerId = handler.getClient().getPlayerId();
		
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
				
				StormFrontClient client = (StormFrontClient)handler.getClient();
				if (client.getServerSettings().getClientVersion() == null)
				{
					FileInputStream stream = new FileInputStream(serverSettings);
					StormFrontClientSettings settings =
						(StormFrontClientSettings)handler.getClient().getStormFrontClientSettings();
					
					client.getServerSettings().importServerSettings(stream, settings);					
					stream.close();
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
