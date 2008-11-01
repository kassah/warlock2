package cc.warlock.core.stormfront.script.wsl;

public interface IWSLCommand {
	public void execute(String arguments) throws InterruptedException;
}
