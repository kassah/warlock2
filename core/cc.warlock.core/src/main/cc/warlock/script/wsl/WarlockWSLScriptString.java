package cc.warlock.script.wsl;

import java.util.HashMap;

public class WarlockWSLScriptString extends WarlockWSLScriptArg {
	private String string;
	
	public WarlockWSLScriptString(String string) {
		this.string = string;
	}
	
	@Override
	public String getString(HashMap<String, String> variables) {
		return string;
	}

}
