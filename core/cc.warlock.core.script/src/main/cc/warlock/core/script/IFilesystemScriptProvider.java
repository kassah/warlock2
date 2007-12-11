package cc.warlock.core.script;

import java.io.File;
import java.util.List;

public interface IFilesystemScriptProvider extends IScriptProvider {

	public void addScriptDirectory (File directory);
	
	public void removeScriptDirectory (File directory);
	
	public List<File> getScriptDirectories();
	
	public void forceScan();
}
