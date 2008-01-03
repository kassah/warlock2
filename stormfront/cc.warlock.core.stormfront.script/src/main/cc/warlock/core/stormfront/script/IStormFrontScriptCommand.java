package cc.warlock.core.stormfront.script;

/**
 * A stormfront script command hides the script-language specific implementation
 * details so StormFrontScriptCommands can execute without knowing the difference.
 */ 
public interface IStormFrontScriptCommand {

	public void execute();
	
	public void setProperty(String name, Object value);
	public Object getProperty(String name);
}
