package cc.warlock.script;

public interface IScriptCallback {

	public enum CallbackType
	{
		Matched, FinishedWaiting, InNextRoom
	};
		
	public void handleCallback (CallbackEvent event);
}
