/*
 * Created on Jan 15, 2005
 */
package cc.warlock.core.client.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;

import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.ICommandHistory;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.IStyledString;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.network.IConnection;


/**
 * @author Marshall
 */
public abstract class WarlockClient implements IWarlockClient {

	protected IConnection connection;
	protected ArrayList<IWarlockClientViewer> viewers;
	protected ICommandHistory commandHistory = new CommandHistory();
	protected String streamPrefix;

	protected Hashtable <IStream, IStyledString> streamBuffers = new Hashtable<IStream, IStyledString>();
	
	public WarlockClient () {
		viewers = new ArrayList<IWarlockClientViewer>();
		streamPrefix = "client:" + hashCode() + ":";
		
		bufferStreams();
	}
	
	protected abstract Collection<IStream> getStreamsToBuffer();
	
	protected void bufferStreams ()
	{
		IStreamListener listener = new IStreamListener () {
			public void streamCleared(IStream stream) {
				if (stream != null) {
					streamBuffers.get(stream).clear();
				}
			}
			public void streamDonePrompting(IStream stream) {}
			public void streamEchoed(IStream stream, String text) {}
			public void streamPrompted(IStream stream, String prompt) {
				if (stream != null) {
					streamBuffers.get(stream).append(prompt + "\n");
				}
			}
			public void streamReceivedText(IStream stream, IStyledString text) {
				if (stream != null) {
					streamBuffers.get(stream).append(text);
				}
			}
		};
		
		for (IStream stream : getStreamsToBuffer())
		{
			streamBuffers.put(stream, new StyledString());
			stream.addStreamListener(listener);
		}
	}

	
	// IWarlockClient methods
	
	public ICommandHistory getCommandHistory() {
		return commandHistory;
	}
	
	public abstract void connect(String server, int port, String key) throws IOException;
	
	public void send(String command) {
		send(new Command(command, new Date()));
	}
	
	public void send(ICommand command) {
		if(connection == null) {
			// Not yet connected to server
			return;
		}
		
		if (!command.isInHistory())
			commandHistory.addCommand(command);
		
		/* FIXME: instead of stopping prompting, we should add a method
		 * displayCommand() to IStream. a lot of different places send 
		 * commands, all of them should be updated with that functionality
		 */
		getDefaultStream().donePrompting();
		
		try {
			connection.send(command.getCommand() + "\n");
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
	
	public IStyledString getStreamBuffer(IStream stream) {
		if (streamBuffers.containsKey(stream))
			return streamBuffers.get(stream);
		else return null;
	}
	
	public IConnection getConnection() {
		return connection;
	}
}
