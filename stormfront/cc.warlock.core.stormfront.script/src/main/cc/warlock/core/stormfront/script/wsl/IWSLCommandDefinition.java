package cc.warlock.core.stormfront.script.wsl;

public interface IWSLCommandDefinition {
	public void execute(WSLScript script, String arguments) throws InterruptedException;
}
