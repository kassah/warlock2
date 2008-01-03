package cc.warlock.core.stormfront.script.javascript;

import cc.warlock.core.stormfront.client.IStormFrontClient;

public class JavascriptComponent extends JavascriptProperty<String> {	
	public JavascriptComponent (IStormFrontClient client, String componentName)
	{
		super(client.getComponent(componentName));
	}
}
