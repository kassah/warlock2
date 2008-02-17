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
import cc.warlock.core.stormfront.client.IStormFrontClientViewer;
import cc.warlock.core.stormfront.internal.SettingsTagHandler.ViewerVisitor;

public class SettingsElementsTagHandler extends DefaultTagHandler {

	public SettingsTagHandler settings;
	
	public SettingsElementsTagHandler(IStormFrontProtocolHandler handler, SettingsTagHandler settings) {
		super(handler);
		this.settings = settings;
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] {
				"presets", "strings", "stream", "scripts",
				"names", "macros", "palette", "vars", "dialog",
				"panels", "toggles", "misc", "options", "cmdline",
				"keys", "p", "k", "i", "w", "h", "k", "v", "s", "m", "o",
				"font", "columnFont", "detach", "ignores", "builtin",
				"group", "toggles", "app", "display"
				};
	}
	
	@Override
	public void handleEnd(String rawXML) {
		IStormFrontClientViewer.SettingType setting = null;
		if ("presets".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Presets;
		else if ("strings".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Strings;
		else if ("stream".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Streams;
		else if ("scripts".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Scripts;
		else if ("names".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Names;
		else if ("macros".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Macros;
		else if ("palette".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Palette;
		else if ("vars".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Vars;
		else if ("dialog".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Dialogs;
		else if ("panels".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Panels;
		else if ("toggles".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Toggles;
		else if ("misc".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Misc;
		else if ("options".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Options;
		
		if (setting != null)
		{
			final IStormFrontClientViewer.SettingType finalSetting = setting;
			settings.visitViewers(new ViewerVisitor() {
				public void visit(IStormFrontClientViewer viewer) {
					viewer.receivedServerSetting(finalSetting);
				}
			});
		}
	}

}
