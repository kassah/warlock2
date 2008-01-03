package cc.warlock.core.stormfront.script.javascript;

import org.mozilla.javascript.ScriptableObject;

import cc.warlock.core.client.IProperty;

public class JavascriptProperty<T> extends ScriptableObject {

	protected IProperty<T> property;
	
	public JavascriptProperty (IProperty<T> property)
	{
		this.property = property;
	}

	@Override
	public Object getDefaultValue(Class typeHint) {
		return property.get();
	}
	
	@Override
	public String getClassName() {
		return "JavascriptProperty";
	}
}
