package cc.warlock.core.script;

import java.io.Reader;


public interface IScriptInfo {

	public String getScriptName();
	
	public Reader openReader();
}
