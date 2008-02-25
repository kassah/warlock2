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

import cc.warlock.core.client.settings.IClientSettingProvider;
import cc.warlock.core.client.settings.IPatternSetting;

public class PatternSetting extends ClientSetting implements IPatternSetting {

	protected Pattern pattern;
	
	public PatternSetting (PatternSetting other)
	{
		super(other);	
		this.pattern = other.pattern;
	}
	
	public PatternSetting (IClientSettingProvider provider, String pattern)
	{
		this(provider, pattern, false);
	}
	
	public PatternSetting (IClientSettingProvider provider, String pattern, boolean literal)
	{
		this(provider, pattern, literal, false);
	}
	
	public PatternSetting (IClientSettingProvider provider, String pattern, boolean literal, boolean caseSensitive) {
		super(provider);
		
		int flags = 0;
		if (literal) {
			flags |= Pattern.LITERAL;
		}
		if (!caseSensitive) {
			flags |= Pattern.CASE_INSENSITIVE;
		}
		
		this.pattern = Pattern.compile(pattern, flags);
	}
	
	public PatternSetting (IClientSettingProvider provider, Pattern pattern) {
		super(provider);
		
		this.pattern = pattern;
	}
	
	public Pattern getPattern() {
		return pattern;
	}
	
	public void setPattern(Pattern pattern) {
		if (!pattern.equals(this.pattern))
			needsUpdate = true;
		
		this.pattern = pattern;
	}
	
	public boolean isLiteral() {
		return (pattern.flags() & Pattern.LITERAL) > 0;
	}
	
	public boolean isCaseSensitive() {
		return (pattern.flags() & Pattern.CASE_INSENSITIVE) == 0;
	}
}
