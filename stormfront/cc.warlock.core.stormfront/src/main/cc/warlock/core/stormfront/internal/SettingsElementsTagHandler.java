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
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;

public class SettingsElementsTagHandler extends DefaultTagHandler {

	public SettingsTagHandler settings;
	
	public SettingsElementsTagHandler(IStormFrontProtocolHandler handler, SettingsTagHandler settings) {
		super(handler);
		this.settings = settings;
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { };
		/*return new String[] {
				"presets", "strings", "stream", "scripts",
				"names", "macros", "palette", "vars", "dialog",
				"panels", "toggles", "misc", "options", "cmdline",
				"keys", "p", "k", "i", "w", "h", "k", "v", "s", "m", "o",
				"font", "columnFont", "detach", "ignores", "builtin",
				"group", "toggles", "app", "display"
				};*/
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes, String rawXML) {
		settings.append(rawXML);
	}
	
	@Override
	public boolean handleCharacters(String characters) {
		settings.append(characters);
		return true;
	}
	
	@Override
	public void handleEnd(String rawXML) {
		handler.getClient().receivedServerSetting(getCurrentTag());
		settings.append(rawXML);
	}

}
