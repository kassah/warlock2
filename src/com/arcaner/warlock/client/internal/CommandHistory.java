/*
 * Created on Jan 15, 2005
 */
package com.arcaner.warlock.client.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.prefs.Preferences;

import com.arcaner.warlock.client.ICommand;
import com.arcaner.warlock.client.ICommandHistory;

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
	
	public void addCommand(String command) {
		commands.push(new Command(command, new Date()));
		System.out.println("position = " + (commands.size() - 1));
		position = commands.size() - 1;
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
