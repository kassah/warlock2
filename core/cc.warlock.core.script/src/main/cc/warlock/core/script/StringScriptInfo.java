package cc.warlock.core.script;

import java.io.Reader;
import java.io.StringReader;

public class StringScriptInfo implements IScriptInfo {

	protected String string, name;
	
	public StringScriptInfo (String name, String string)
	{
		this.name = name;
		this.string = string;
	}
	
	public String getScriptName() {
		return name;
	}

	public Reader openReader() {
		return new StringReader(string);
	}

}
