/*
 * Created on Jan 15, 2005
 */
package com.arcaner.warlock.client.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.arcaner.warlock.client.ICommandHistory;
import com.arcaner.warlock.client.IWarlockClient;
import com.arcaner.warlock.client.IWarlockClientViewer;
import com.arcaner.warlock.network.IConnection;
import com.arcaner.warlock.script.internal.ScriptRunner;

/**
 * @author Marshall
 */
public abstract class WarlockClient implements IWarlockClient {

	protected IConnection connection;
	protected ArrayList<IWarlockClientViewer> viewers;
	protected ICommandHistory commandHistory = new CommandHistory();
	
	public WarlockClient () {
		viewers = new ArrayList<IWarlockClientViewer>();
	}
	
	// IWarlockClient methods
	
	public ICommandHistory getCommandHistory() {
		return commandHistory;
	}
	
	public abstract void connect(String server, int port, String key) throws IOException;
	
	public void append (String viewName, String text) {
		for (IWarlockClientViewer viewer : viewers) viewer.append(viewName, text);
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
	
	public void echo (String viewName, String text) {
		/*
		 * TODO this needs to only be used for sending commands
		 *   sending random text needs to go through the append
		 *   mechanism
		 */
		for (IWarlockClientViewer viewer : viewers) viewer.echo(viewName, text);
	}
	
	public void clear(String viewName) {
		// TODO clear the view here
	}
	
	public void setTitle(String title) {
		for (IWarlockClientViewer viewer : viewers) viewer.setViewerTitle(title);
	}
	
	public Collection<IWarlockClientViewer> getViewers() {
		return viewers;
	}
	
	public void addViewer(IWarlockClientViewer viewer) {
		viewers.add(viewer);
	}
	
}
