package cc.warlock.core.stormfront.script.javascript;

import org.mozilla.javascript.Scriptable;

import cc.warlock.core.stormfront.client.IStormFrontClient;

public class JavascriptComponent extends JavascriptProperty<String> {	
	public JavascriptComponent (Scriptable scope, IStormFrontClient client, String componentName)
	{
		super(scope, client.getComponent(componentName));
	}
}
