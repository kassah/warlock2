package cc.warlock.core.stormfront.script;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.script.IScriptCommands;
import cc.warlock.core.script.ScriptCommandsFactory;
import cc.warlock.core.script.internal.ScriptCommands;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.script.internal.StormFrontScriptCommands;

public class StormFrontScriptCommandsFactory extends ScriptCommandsFactory {

	@Override
	public IScriptCommands createScriptCommands(IWarlockClient client, String name) {
		
		if (client instanceof IStormFrontClient)
		{
			return new StormFrontScriptCommands((IStormFrontClient)client, name);
		}
		
		return new ScriptCommands(client, name);
	}

}
