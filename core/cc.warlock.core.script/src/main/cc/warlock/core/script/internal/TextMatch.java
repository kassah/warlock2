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
package cc.warlock.core.script.internal;

import cc.warlock.core.script.IMatch;

public class TextMatch implements IMatch {
	
	private String originalText;
	private String matchText;
	private String matchLine;//BFisher - For getting line we matched against.
	private boolean ignoreCase;
	
	public TextMatch(String text) {
		this(text, true);
	}

	//BFisher - Return the line matched against for JS scripting.
	//See matches() below and matchLine above.
	public String getMatchedLine() {
		return this.matchLine;
	}
	
	public TextMatch(String text, boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
		this.originalText = text;
		
		if(ignoreCase) {
			matchText = text.toLowerCase();
		} else {
			matchText = text;
		}
	}

	public boolean matches(String text) {
		boolean rv = false;

		if(ignoreCase) {
			if(text.toLowerCase().contains(matchText)) {
				rv = true;
				//BFisher - Set text line matched.
				this.matchLine = text;
			}
		} else {
			if(text.matches(matchText)) {
				rv = true;	
				//BFisher - Set text line matched.
				this.matchLine = text;
			}
		}

		return rv;
	}
	
	public String getText() {
		return originalText;
	}
}
