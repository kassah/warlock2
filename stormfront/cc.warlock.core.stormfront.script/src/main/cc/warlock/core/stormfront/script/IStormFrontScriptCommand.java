package cc.warlock.core.stormfront.script;

/**
 * A stormfront script command just has a single execute() method (we use this for actions so we can implement in different languages)
 */ 
public interface IStormFrontScriptCommand {

	public void execute();
	
}
