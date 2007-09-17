package cc.warlock.core.script;

import java.io.File;

public interface IScriptFileInfo extends IScriptInfo {

	public File getScriptFile();
	public String getExtension();
}
