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
package cc.warlock.rcp.stormfront.ui.util;

import cc.warlock.core.stormfront.client.internal.StormFrontClient;
import cc.warlock.rcp.stormfront.settings.StormFrontSWTSettings;

/**
 * This is a helper factory so we can hook in our own implementations, listeners, etc
 *  into each client instance we need to create.
 * @author marshall
 *
 */
public class StormFrontClientFactory {

	public static StormFrontClient createStormFrontClient ()
	{
		StormFrontClient client = new StormFrontClient ();
		client.getServerSettings().setMacroImporter(new StormFrontSWTSettings());
		
		return client;
	}
}
