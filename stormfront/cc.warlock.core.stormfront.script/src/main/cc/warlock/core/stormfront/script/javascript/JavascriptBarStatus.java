package cc.warlock.core.stormfront.script.javascript;

import cc.warlock.core.client.IProperty;
import cc.warlock.core.stormfront.client.BarStatus;

public class JavascriptBarStatus extends JavascriptProperty<BarStatus> {

	public JavascriptBarStatus (IProperty<BarStatus> property)
	{
		super(property);
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
