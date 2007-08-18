package cc.warlock.script;

public interface IScriptCallback {

	public enum CallbackType
	{
		Matched, FinishedWaiting, FinishedWaitingForRoundtime, InNextRoom, FinishedPausing
	};
		
	public void handleCallback (CallbackEvent event);
}
