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
		this(provider, pattern, literal, caseSensitive, true);
	}
	
	public PatternSetting (IClientSettingProvider provider, String pattern, boolean literal, boolean caseSensitive, boolean fullWordMatch) {
		super(provider);
		
		int flags = 0;
		if (literal) {
			flags |= Pattern.LITERAL;
			pattern = Pattern.quote(pattern);
		}
		if (!caseSensitive) {
			flags |= Pattern.CASE_INSENSITIVE;
		}
		if (fullWordMatch) {
			pattern = "\\b" + pattern + "\\b";
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
	
	protected void setFlag (boolean set, int flag)
	{
		int flags = pattern.flags();
		if (set) {
			flags |= flag;
		} else {
			flags &= ~flag;
		}
		
		pattern = Pattern.compile(pattern.pattern(), flags);
	}
	
	public void setLiteral (boolean literal)
	{
		if (literal != isLiteral())
			needsUpdate = true;
		
		setFlag(literal, Pattern.LITERAL);
	}
	
	public void setCaseSensitive (boolean caseSensitive)
	{
		if (caseSensitive != isCaseSensitive())
			needsUpdate = true;
		
		setFlag(!caseSensitive, Pattern.CASE_INSENSITIVE);
	}
	
	public void setFullWordMatch (boolean fullWordMatch)
	{
		boolean isFullWordMatch = isFullWordMatch();
		
		if (fullWordMatch != isFullWordMatch)
			needsUpdate = true;
		
		if (!fullWordMatch && isFullWordMatch)
		{
			this.pattern = Pattern.compile(getFullWordPattern(), this.pattern.flags());
		}
		else if (fullWordMatch && !isFullWordMatch)
		{
			String pattern = this.pattern.pattern();
			pattern = "\\b" + pattern + "\\b";
			
			this.pattern = Pattern.compile(pattern, this.pattern.flags());
		}
	}
	
	public boolean isLiteral() {
		return (pattern.flags() & Pattern.LITERAL) > 0;
	}
	
	public boolean isCaseSensitive() {
		return (pattern.flags() & Pattern.CASE_INSENSITIVE) == 0;
	}
	
	public boolean isFullWordMatch () {
		return pattern.pattern().startsWith("\\b") && pattern.pattern().endsWith("\\b");
	}
	
	public String getFullWordPattern() {
		if (isFullWordMatch()) {
			String pattern = this.pattern.pattern();
			pattern = pattern.substring(2, pattern.length()-2);
			return pattern;
		} else return pattern.pattern();
	}
}
