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
package cc.warlock.core.client.settings;

import java.util.Collection;
import java.util.List;

import cc.warlock.core.client.IWarlockStyle;

public interface IHighlightProvider extends IClientSettingProvider {
	public List<? extends IHighlightString> getHighlightStrings();
	
	public void addHighlightString (IHighlightString string);
	public void insertHighlightString (int index, IHighlightString string);
	public void removeHighlightString (IHighlightString string);
	public void replaceHighlightString (IHighlightString originalString, IHighlightString newString);
	
	public IWarlockStyle getNamedStyle (String name);
	public Collection<? extends IWarlockStyle> getNamedStyles();
	
	public void addNamedStyle(String name, IWarlockStyle style);
	public void removeNamedStyle(String name);
	
}
