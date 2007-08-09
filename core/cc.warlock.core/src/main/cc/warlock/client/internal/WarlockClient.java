/*
 * Created on Jan 15, 2005
 */
package cc.warlock.client.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import cc.warlock.client.ICommandHistory;
import cc.warlock.client.IStream;
import cc.warlock.client.IWarlockClient;
import cc.warlock.client.IWarlockClientViewer;
import cc.warlock.network.IConnection;


/**
 * @author Marshall
 */
public abstract class WarlockClient implements IWarlockClient {

	protected IConnection connection;
	protected ArrayList<IWarlockClientViewer> viewers;
	protected ICommandHistory commandHistory = new CommandHistory();
	protected String streamPrefix;
	
	public WarlockClient () {
		viewers = new ArrayList<IWarlockClientViewer>();
		streamPrefix = "client:" + hashCode() + ":";
	}
	
	// IWarlockClient methods
	
	public ICommandHistory getCommandHistory() {
		return commandHistory;
	}
	
	public abstract void connect(String server, int port, String key) throws IOException;
	
	public void send(String command) {
		if(connection == null) {
			// Not yet connected to server
			return;
		}
		
		commandHistory.addCommand(command);
		
		try {
			connection.send(command + "\n");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public Collection<IWarlockClientViewer> getViewers() {
		return viewers;
	}
	
	public void addViewer(IWarlockClientViewer viewer) {
		viewers.add(viewer);
	}
	
	public IStream getDefaultStream() {
		return getStream(IWarlockClient.DEFAULT_STREAM_NAME);
	}
	
	public IStream getStream(String streamName) {
		return Stream.fromName(streamPrefix + streamName);
	}
	
	public IConnection getConnection() {
		return connection;
	}
}
