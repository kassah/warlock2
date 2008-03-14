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

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import cc.warlock.core.script.IMatch;

public class RegexMatch implements IMatch {
	
	Pattern regex;
	ArrayList<String> groups = new ArrayList<String>();
	
	public RegexMatch(String text) throws PatternSyntaxException {
		this(text, false);
	}
	
	public RegexMatch(String text, boolean ignoreCase) throws PatternSyntaxException {
		
		int flags = 0;
		if(ignoreCase) flags |= Pattern.CASE_INSENSITIVE;
		
		regex = Pattern.compile(text, flags);
	}
	
	public boolean matches(String text) {
		groups.clear();
		Matcher m = regex.matcher(text);
		if(m.find()) {
			groups.add(m.group());
			for(int i = 1; i <= m.groupCount(); i++) {
				groups.add(m.group(i));
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	public String getText() {
		return regex.pattern();
	}
	public Collection<String> groups() {
		return groups;
	}
}
