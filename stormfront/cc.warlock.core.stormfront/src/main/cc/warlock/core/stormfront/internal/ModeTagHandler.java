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

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.client.internal.StormFrontClient;
import cc.warlock.core.stormfront.network.StormFrontConnection;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;

public class ModeTagHandler extends DefaultTagHandler {
	private String id;

	public ModeTagHandler (IStormFrontProtocolHandler handler)
	{
		super(handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "mode" };
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes, String rawXML) {
		id = attributes.getValue("id");
			
	}
	
	@Override
	public void handleEnd(String rawXML) {
		if(id != null) {
			StormFrontClient client = (StormFrontClient) handler.getClient();

			if (id.equals("GAME"))
			{
				//client.getGameMode().set(IStormFrontClient.GameMode.Game);
			}
			else if (id.equals("CMGR"))
			{
				//client.getGameMode().set(IStormFrontClient.GameMode.CharacterManager);
				((StormFrontConnection)client.getConnection()).passThrough();
				//client.getDefaultStream().send(((StormFrontConnection)client.getConnection()).getBufferContents());
			}
		}
	}
}
