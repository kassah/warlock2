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
package cc.warlock.core.client.settings.internal;

import java.util.regex.Pattern;

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.client.settings.IHighlightProvider;
import cc.warlock.core.client.settings.IHighlightString;

public class HighlightString extends PatternSetting implements IHighlightString {

	protected IWarlockStyle style;
	
	public HighlightString (IHighlightProvider provider, String pattern, boolean literal, boolean caseSensitive, boolean fullWordMatch, IWarlockStyle style) {	
		super(provider, pattern, literal, caseSensitive, fullWordMatch);
		this.style = style;
	}
	
	public HighlightString (IHighlightProvider provider, Pattern pattern, IWarlockStyle style)
	{
		super(provider, pattern);
		this.style = style;
	}

	public HighlightString (HighlightString other)
	{
		super(other);
		this.style = new WarlockStyle(other.style);
	}
	
	public IWarlockStyle getStyle() {
		return style;
	}

	public void setStyle(IWarlockStyle style) {
		if (!this.style.equals(style))
			needsUpdate = true;
		
		this.style = style;
	}

	public HighlightString getOriginalHighlightString() {
		return (HighlightString)originalSetting;
	}
}

