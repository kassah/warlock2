package cc.warlock.core.stormfront.script.wsl;

public interface IWSLCommandDefinition {
	public void execute(String arguments) throws InterruptedException;
}
