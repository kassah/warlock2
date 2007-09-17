/*
 * Created on Jan 15, 2005
 */
package cc.warlock.core.client;

/**
 * @author Marshall
 * 
 * The warlock client's command history
 */
public interface ICommandHistory {
	
	/**
	 * @return The last command entered in this command history
	 */
	public ICommand getLastCommand();
	
	/**
	 * @return The previous command in the command history towards the past
	 */
	public ICommand prev();
	
	/**
	 * @return The next command in the command history towards the present
	 */
	public ICommand next();

	/**
	 * @return The current command being edited.
	 */
	public ICommand current();
	
	/**
	 * @return The size of the command history
	 */
	public int size();
	
	/**
	 * @param position
	 * @return The command at the given position
	 */
	public ICommand getCommandAt(int position);
	
	/**
	 * Reset to the position of the "latest" command
	 */
	public void resetPosition();
	
	/**
	 * Add a command to the command history.
	 * Equivalent to addCommand(new Command(command, new Date()))
	 * 
	 * @param command
	 */
	public void addCommand (String command);
	
	/**
	 * Add a command to the command history
	 * @param command
	 */
	public void addCommand (ICommand command);
	
	/**
	 * Save this command history
	 */
	public void save ();
}
