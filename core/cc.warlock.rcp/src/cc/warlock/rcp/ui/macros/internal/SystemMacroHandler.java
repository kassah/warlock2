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
package cc.warlock.rcp.ui.macros.internal;

import java.util.List;

import org.eclipse.swt.SWT;

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.settings.macro.IMacro;
import cc.warlock.core.client.settings.macro.IMacroHandler;
import cc.warlock.core.script.IScript;
import cc.warlock.core.script.ScriptEngineRegistry;
import cc.warlock.rcp.macro.MacroCommandRegistry;

public class SystemMacroHandler implements IMacroHandler {

	private IMacro sendMacro, stopScript;
	
	public SystemMacroHandler ()
	{
		sendMacro = MacroCommandRegistry.createMacro(SWT.CR, this);
		stopScript = MacroCommandRegistry.createMacro(SWT.ESC, this);
	}
	
	public IMacro[] getMacros ()
	{
		return new IMacro[] { sendMacro, stopScript };
	}
	
	public boolean handleMacro (IMacro macro, IWarlockClientViewer viewer)
	{
		if (macro.equals(sendMacro))
		{
			handleSend(viewer);
			return true;
		}
		else if (macro.equals(stopScript))
		{
			handleStopScript(viewer);
			return true;
		}
		
		return false;
	}
	
	public void handleSend (IWarlockClientViewer viewer)
	{
		viewer.submit();
	}

	private void handleStopScript(IWarlockClientViewer viewer)
	{
		List<IScript> runningScripts = ScriptEngineRegistry.getRunningScripts(viewer.getWarlockClient());
		for(IScript script : runningScripts) {
			script.stop();
		}
	}
	
}
