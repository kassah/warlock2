package cc.warlock.core.stormfront.script.wsl;

import java.util.Map;

public interface IWSLCommandProvider {

	public Map<String, IWSLCommand> getCommands();
}
