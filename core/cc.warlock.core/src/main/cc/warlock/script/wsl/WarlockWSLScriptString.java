package cc.warlock.script.wsl;

import java.util.Map;

public class WarlockWSLScriptString extends WarlockWSLScriptArg {
	private String string;
	
	public WarlockWSLScriptString(String string) {
		this.string = string;
	}
	
	@Override
	public String getString(Map<String, String> variables) {
		return string;
	}

}
