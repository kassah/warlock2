/*
 * Created on Jan 15, 2005
 */
package com.arcaner.warlock.client.internal;

import java.io.IOException;

import com.arcaner.warlock.client.ICommandHistory;
import com.arcaner.warlock.client.IWarlockClient;
import com.arcaner.warlock.client.IWarlockClientViewer;
import com.arcaner.warlock.network.IConnection;
import com.arcaner.warlock.script.internal.ScriptRunner;

/**
 * @author Marshall
 */
abstract public class WarlockClient implements IWarlockClient {

	protected IConnection connection;
	protected IWarlockClientViewer viewer;
	protected ICommandHistory commandHistory = new CommandHistory();
	
	public WarlockClient (IWarlockClientViewer viewer) {
		this.viewer = viewer;
		this.viewer.setWarlockClient(this);
	}
	
	// IWarlockClient methods
	
	public ICommandHistory getCommandHistory() {
		return commandHistory;
	}
	
	abstract public void connect(String server, int port, String key) throws IOException;
	
	public void output(String viewName, String text) {
		viewer.append(text);
	}
	
	public void send(String command) {
		if(connection == null) {
			// Not yet connected to server
			return;
		}
		
		commandHistory.addCommand(command);
		
		try {
			if (command.startsWith(".")){
				runScriptCommand(command);
			} else {
				connection.send(command + "\n");
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private void runScriptCommand(String command) {
		int firstSpace = command.indexOf(" ") - 1;
		
		String scriptName = command.substring(1, (firstSpace < 0 ? command.length() : firstSpace));
		// Need to parse and handle args at this point.. TODO
		
		System.out.println("scriptname=" +scriptName);
		ScriptRunner.runScript(this, "C:\\Code\\warlock2\\test\\" + scriptName);
	}
	
	public void echo(String text) {
		/*
		 * TODO this needs to only be used for sending commands
		 *   sending random text needs to go through the append
		 *   mechanism
		 */
		viewer.echo(text);
	}
	
	public void clear(String viewName) {
		// TODO clear the view here
	}
	
	public void setTitle(String title) {
		viewer.setViewerTitle(title);
	}
	
}
