package cc.warlock.core.stormfront.script.javascript;

import org.mozilla.javascript.Scriptable;

import cc.warlock.core.client.IProperty;
import cc.warlock.core.stormfront.client.BarStatus;

public class JavascriptBarStatus extends JavascriptProperty<BarStatus> {

	public JavascriptBarStatus (Scriptable scope, IProperty<BarStatus> property)
	{
		super(scope, property);
	}
	
	public String jsGet_text ()
	{
		return property.get().getText();
	}
	
	@Override
	public Object getDefaultValue(Class typeHint) {
		return property.get().getValue();
	}
}
