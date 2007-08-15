package cc.warlock.rcp.ui.macros.internal;

import java.util.List;

import org.eclipse.swt.SWT;

import cc.warlock.client.IWarlockClientViewer;
import cc.warlock.client.stormfront.IStormFrontClient;
import cc.warlock.rcp.ui.macros.IMacro;
import cc.warlock.rcp.ui.macros.IMacroHandler;
import cc.warlock.script.IScript;

public class ScriptMacroHandler implements IMacroHandler {

	protected IMacro stopScript, pauseOrResumeScript;
	
	public ScriptMacroHandler ()
	{
		stopScript = new Macro(SWT.ESC);
		pauseOrResumeScript = new Macro(SWT.ESC, SWT.SHIFT);
		
		stopScript.addHandler(this);
		pauseOrResumeScript.addHandler(this);
	}
	
	public IMacro[] getMacros()
	{
		return new IMacro[] { stopScript, pauseOrResumeScript };
	}
	
	public boolean handleMacro(IMacro macro, IWarlockClientViewer viewer)
	{
		if (macro.equals(stopScript))
		{
			handleStopScript(viewer);
		}
		else if (macro.equals(pauseOrResumeScript))
		{
			handlePauseOrResumeScript(viewer);
		}
		
		return true;
	}

	private void handlePauseOrResumeScript(IWarlockClientViewer viewer)
	{
		List<IScript> runningScripts = ((IStormFrontClient)viewer.getWarlockClient()).getRunningScripts();
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

	private void handleStopScript(IWarlockClientViewer viewer)
	{
		List<IScript> runningScripts = ((IStormFrontClient)viewer.getWarlockClient()).getRunningScripts();
		if (runningScripts.size() > 0)
		{
			IScript currentScript = runningScripts.get(runningScripts.size() - 1);
			
			if (currentScript != null)
			{
				currentScript.stop();
			}
		}
	}
	
}
