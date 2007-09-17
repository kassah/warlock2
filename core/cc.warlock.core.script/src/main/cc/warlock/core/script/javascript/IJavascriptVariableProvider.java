package cc.warlock.core.script.javascript;

import org.mozilla.javascript.Scriptable;

public interface IJavascriptVariableProvider {

	public void loadVariables (JavascriptScript script, Scriptable scope);
	
}
