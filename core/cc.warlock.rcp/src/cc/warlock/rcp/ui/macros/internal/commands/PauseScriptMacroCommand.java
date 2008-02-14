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
package cc.warlock.rcp.ui.macros.internal.commands;

import java.util.List;

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.script.IScript;
import cc.warlock.core.script.ScriptEngineRegistry;
import cc.warlock.rcp.ui.macros.IMacroCommand;

public class PauseScriptMacroCommand implements IMacroCommand {
	
	public String getIdentifier() {
		return "PauseScript";
	}
	
	public void execute(IWarlockClientViewer viewer) {
		List<IScript> runningScripts = ScriptEngineRegistry.getRunningScripts();
		if (runningScripts.size() > 0)
		{
			IScript currentScript = runningScripts.get(runningScripts.size() - 1);
			if (currentScript != null)
			{
				currentScript.suspendOrResume();
			}
		}
	}
}
