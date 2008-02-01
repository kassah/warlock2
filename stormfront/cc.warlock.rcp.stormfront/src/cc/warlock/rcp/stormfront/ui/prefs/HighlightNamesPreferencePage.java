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
package cc.warlock.rcp.stormfront.ui.prefs;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import cc.warlock.core.stormfront.serversettings.server.HighlightPreset;

public class HighlightNamesPreferencePage extends
		HighlightStringsPreferencePage {

	public static final String PAGE_ID = "cc.warlock.rcp.prefs.highlightNames";
	
	@Override
	protected String getDisplayName() {
		return "Highlight Names";
	}
	
	protected ViewerFilter getViewerFilter ()
	{
		return new ViewerFilter ()
		{
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				return ((HighlightPreset)element).isName();
			}
		};
	}
	
	@Override
	protected HighlightPreset createHighlightString ()
	{
		 HighlightPreset newString = client.getServerSettings().createHighlightString(true);
		 newString.setText("<Highlight Name>");
		 return newString;
	}
	
	@Override
	protected void saveSettings() {
		client.getServerSettings().saveHighlightNames();
	}
}
