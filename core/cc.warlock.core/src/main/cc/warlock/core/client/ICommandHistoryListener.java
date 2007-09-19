package cc.warlock.core.client;

public interface ICommandHistoryListener {

	public void commandAdded (ICommand command);
	
	public void historyNext (ICommand next);
	
	public void historyPrevious (ICommand previous);
	
	public void historyReset (ICommand current);
}
