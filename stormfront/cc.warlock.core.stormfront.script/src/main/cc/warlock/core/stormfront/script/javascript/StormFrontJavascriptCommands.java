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
/**
 * 
 */
package cc.warlock.core.stormfront.script.javascript;

import java.util.Collection;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;

import cc.warlock.core.script.IMatch;
import cc.warlock.core.script.internal.RegexMatch;
import cc.warlock.core.script.javascript.JavascriptCommands;
import cc.warlock.core.script.javascript.JavascriptScript;
import cc.warlock.core.stormfront.script.IStormFrontScriptCommands;

public class StormFrontJavascriptCommands extends JavascriptCommands
{
	protected IStormFrontScriptCommands sfCommands;
	
	public StormFrontJavascriptCommands (IStormFrontScriptCommands commands, JavascriptScript script)
	{
		super(commands, script);
		this.sfCommands = commands;
	}

	protected class JSActionHandler implements Runnable 
	{
		private Function function;
		private RegexMatch match;
		
		public JSActionHandler (Function function, RegexMatch match)
		{
			this.function = function;
			this.match = match;
		}
		
		public void run() {
			Context.enter();
			try {
				Object[] arguments = new Object[0];
				Collection<String> matchGroups = match.groups();
				arguments = matchGroups.toArray(new String[matchGroups.size()]);
				
				function.call(script.getContext(), script.getScope(), null, arguments);
			} finally {
				Context.exit();
			}
		}
	}
	
	// IStormFrontScriptCommands delegated methods
	public void addAction(Function action, String text) {
		checkStop();
		
		RegexMatch match = new RegexMatch(text);
		JSActionHandler command = new JSActionHandler(action, match);
		sfCommands.addAction(command, match);
	}

	public void removeAction(String text) {
		checkStop();
		
		sfCommands.removeAction(text);
	}
	
	public void removeAction(IMatch action)
	{
		checkStop();
		
		sfCommands.removeAction(action);
	}

	public void clearActions() {
		checkStop();
		
		sfCommands.clearActions();
	}

	public void waitForRoundtime() {
		checkStop();
		
		sfCommands.waitForRoundtime();
	}

	public void waitNextRoom() {
		checkStop();
		
		sfCommands.waitNextRoom();
	}

}