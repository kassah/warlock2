/*
 * Created on Jan 15, 2005
 */
package cc.warlock.client.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.prefs.Preferences;

import cc.warlock.client.ICommand;
import cc.warlock.client.ICommandHistory;


/**
 * @author Marshall
 * 
 * A Command History implementation
 */
public class CommandHistory implements ICommandHistory {

	protected int position = 0;
	protected Stack<ICommand> commands = new Stack<ICommand>();
	static Preferences prefs = Preferences.userNodeForPackage(Command.class);
	
	public CommandHistory () {
		// load saved history
		byte[] array = prefs.getByteArray("command", null);
		if(array != null) {
			ByteArrayInputStream bytes = new ByteArrayInputStream(array);
			try {
				ObjectInputStream stream = new ObjectInputStream(bytes);
				commands = (Stack<ICommand>)stream.readObject();
				stream.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public ICommand getLastCommand() {
		try {
			return commands.peek();
		} catch(EmptyStackException e) {
			return null;
		}
	}

	public ICommand prev() {
		try {
			if (position > 0)
				position--;
			
			ICommand command = commands.get(position);

			return command;
		} catch(ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ICommand next() {

		try {
			if (position < commands.size() - 1)
				position++;
			
			ICommand command = commands.get(position);
			return command;
		} catch(ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ICommand current () {
		try {
			ICommand command = commands.get(position);
			return command;
		}
		catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void resetPosition() {
		position = commands.size() - 1;
	}
	
	public void addCommand (String string)
	{
		addCommand(new Command(string, new Date()));
	}
	
	public void addCommand(ICommand command) {
		if (commands.size() == 0) {
			command.setFirst(true);
			command.setLast(true);
		}
		else {
			commands.get(commands.size()-1).setLast(false);
			command.setLast(true);
		}
		
		command.setInHistory(true);
		commands.push(command);
		
		resetPosition();
	}
	
	public void save () {
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			ObjectOutputStream stream = new ObjectOutputStream(bytes);
			stream.writeObject(commands);
			stream.flush();
			stream.close();
			prefs.putByteArray("command", bytes.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
