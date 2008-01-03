package cc.warlock.core.stormfront.script.javascript;

import org.mozilla.javascript.ScriptableObject;

import cc.warlock.core.stormfront.client.IStormFrontClient;

public class JavascriptComponent extends ScriptableObject {

	protected IStormFrontClient client;
	protected String componentName;
	
	public JavascriptComponent (IStormFrontClient client, String componentName)
	{
		this.client = client;
		this.componentName = componentName;
	}
	
	@Override
	public String getClassName() {
		return "JavascriptComponent";
	}
	
	@Override
	public Object getDefaultValue(Class typeHint) {
		return client.getComponent(componentName).get();
	}
}
