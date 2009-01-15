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
import java.util.regex.PatternSyntaxException;

import cc.warlock.core.client.settings.IClientSettingProvider;
import cc.warlock.core.client.settings.IPatternSetting;

public class PatternSetting extends ClientSetting implements IPatternSetting {

	protected Pattern pattern;
	
	protected String text;
	protected boolean literal = false;
	protected boolean fullWord = true;
	protected boolean caseSensitive = false;
	protected boolean updateDeferred = true;
	
	public PatternSetting (PatternSetting other)
	{
		super(other);	
		this.pattern = other.pattern;
		this.text = other.text;
		this.literal = other.literal;
		this.fullWord = other.fullWord;
		this.caseSensitive = other.caseSensitive;
	}
	
	public PatternSetting (IClientSettingProvider provider, String pattern) throws PatternSyntaxException
	{
		this(provider, pattern, false);
	}
	
	public PatternSetting (IClientSettingProvider provider, String pattern, boolean literal) throws PatternSyntaxException
	{
		this(provider, pattern, literal, false);
	}
	
	public PatternSetting (IClientSettingProvider provider, String pattern, boolean literal, boolean caseSensitive) throws PatternSyntaxException {
		this(provider, pattern, literal, caseSensitive, true);
	}
	
	public PatternSetting (IClientSettingProvider provider, String pattern, boolean literal, boolean caseSensitive, boolean fullWordMatch) throws PatternSyntaxException {
		super(provider);
		
		this.text = pattern;
		this.literal = literal;
		this.caseSensitive = caseSensitive;
		this.fullWord = fullWordMatch;
		update();
	}
	
	protected void update() throws PatternSyntaxException {
		String s = this.text;
		if (s != null) {
			int flags = 0;
			if (literal) {
				s = Pattern.quote(s);
			}
			if (!caseSensitive) {
				flags |= Pattern.CASE_INSENSITIVE;
			}
			if (fullWord) {
				s = "(^|\\s)\\p{Punct}?" + s + "\\p{Punct}?(\\s|$)";
			}
			
			pattern = Pattern.compile(s, flags);
		} else {
			pattern = null;
		}
		updateDeferred = false;
	}
	
	public Pattern getPattern() throws PatternSyntaxException{
		if (updateDeferred)
			update();
		return pattern;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) throws PatternSyntaxException {
		if (!text.equals(this.text)) {
			updateDeferred = true;
			needsUpdate = true;
		}
		this.text = text;
	}
	
	public void setLiteral (boolean literal) throws PatternSyntaxException
	{
		if (literal != this.literal) {
			updateDeferred = true;
			needsUpdate = true;
			this.literal = literal;
		}
	}
	
	public void setCaseSensitive (boolean caseSensitive)
	{
		if (caseSensitive != this.caseSensitive) {
			updateDeferred = true;
			needsUpdate = true;
			this.caseSensitive = caseSensitive;
		}
	}
	
	public void setFullWordMatch (boolean fullWordMatch)
	{
		if (fullWordMatch != this.fullWord) {
			updateDeferred = true;
			needsUpdate = true;
			this.fullWord = fullWordMatch;
		}
	}
	
	public boolean isLiteral() {
		return literal;
	}
	
	public boolean isCaseSensitive() {
		return caseSensitive;
	}
	
	public boolean isFullWordMatch () {
		return fullWord;
	}
}
