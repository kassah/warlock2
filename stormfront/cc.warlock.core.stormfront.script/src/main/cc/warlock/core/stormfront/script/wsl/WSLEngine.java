package cc.warlock.core.stormfront.script.wsl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.script.IScript;
import cc.warlock.core.script.IScriptEngine;
import cc.warlock.core.script.IScriptFileInfo;
import cc.warlock.core.script.IScriptInfo;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.serversettings.server.IServerScriptInfo;


public class WSLEngine implements IScriptEngine {

	public static final String ENGINE_ID = "cc.warlock.script.wsl.WSLEngine";
	protected ArrayList<IScript> runningScripts = new ArrayList<IScript>();
	
	public String getScriptEngineId() {
		return ENGINE_ID;
	}
	
	public String getScriptEngineName() {
		return "Standard Wizard Scripting Language Engine (c) 2002-2007 Warlock Team";
	}
	
	protected static Collection<String> extensions = new ArrayList<String>();
	static {
		Collections.addAll(extensions, "wiz", "cmd", "wsl");
	}
	
	public boolean supports(IScriptInfo scriptInfo) {
		if (scriptInfo instanceof IScriptFileInfo)
		{
			IScriptFileInfo info = (IScriptFileInfo) scriptInfo;
			if (info.getExtension() != null)
			{
				if (extensions.contains(info.getExtension().toLowerCase())) {
					return true;
				}
			}
		}
		else if (scriptInfo instanceof IServerScriptInfo)
		{
			return true;
		}
		
		return false;
	}
	
	public IScript startScript(IScriptInfo info, IWarlockClient client, String[] arguments) {
		try {
			WSLScript wslScript = new WSLScript(this, info, (IStormFrontClient)client);

			runningScripts.add(wslScript);
			wslScript.start(Arrays.asList(arguments));
			return wslScript;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<IScript> getRunningScripts() {
		return runningScripts;
	}
}
