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
				if (currentScript.isSuspended()) {
					currentScript.resume();
				}
				else {
					currentScript.suspend();
				}
			}
		}
	}
}
