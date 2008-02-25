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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.warlock.core.client.settings.ITrigger;
import cc.warlock.core.client.settings.ITriggerProvider;

public class Trigger extends PatternSetting implements ITrigger {

	protected Pattern triggerPattern;
	
	public Trigger (ITriggerProvider provider, String pattern, boolean literal, boolean caseSensitive)
	{
		super(provider, pattern, literal, caseSensitive);
	}
	
	public Trigger (ITriggerProvider provider, Pattern triggerPattern)
	{
		super(provider, triggerPattern);
	}
	
	public Trigger (Trigger other)
	{
		super(other);
	}
	
	public boolean isTriggered(String text) {
		Matcher matcher = triggerPattern.matcher(text);
		return matcher.find();
	}

	public Trigger getOriginalTrigger ()
	{
		return (Trigger) originalSetting;
	}
}
