package cc.warlock.script;

import java.util.Hashtable;

public class CallbackEvent {

	public CallbackEvent () { }
	public CallbackEvent (IScriptCallback.CallbackType type)
	{
		this.type = type;
	}
	
	public static final String DATA_MATCH = "match";
	
	public Hashtable<String, Object> data = new Hashtable<String, Object>();
	public IScriptCallback.CallbackType type;
	
}
