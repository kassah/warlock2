package cc.warlock.core.script;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.script.internal.ScriptCommands;

public abstract class ScriptCommandsFactory {

	protected static ScriptCommandsFactory factory = new ScriptCommandsFactoryDefault();
	
	public static ScriptCommandsFactory getFactory()
	{
		return factory;
	}
	
	public static void setFactory (ScriptCommandsFactory factory) {
		ScriptCommandsFactory.factory = factory;
	}
	
	public abstract IScriptCommands createScriptCommands (IWarlockClient client, String name);
	
	protected static class ScriptCommandsFactoryDefault extends ScriptCommandsFactory
	{
		@Override
		public IScriptCommands createScriptCommands(IWarlockClient client, String name) {
			return new ScriptCommands(client, name);
		}
	}
}
