/*
 * Created on Jan 15, 2005
 */
package cc.warlock.core.client.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import cc.warlock.core.client.ICommandHistory;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.network.IConnection;


/**
 * @author Marshall
 */
public abstract class WarlockClient implements IWarlockClient {

	protected IConnection connection;
	protected ArrayList<IWarlockClientViewer> viewers;
	protected ICommandHistory commandHistory = new CommandHistory();
	protected String streamPrefix;

	protected Hashtable<IStream, StringBuffer> streamBuffers = new Hashtable<IStream, StringBuffer>();
	protected Hashtable<IStream, List<IWarlockStyle>> streamStyles = new Hashtable<IStream, List<IWarlockStyle>>();
	
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
					streamBuffers.get(stream).setLength(0);
				}
			}
			public void streamReceivedCommand(IStream stream, String text) {}
			public void streamEchoed(IStream stream, String text) {}
			public void streamPrompted(IStream stream, String prompt) {
				if (stream != null) {
					streamBuffers.get(stream).append(prompt + "\n");
				}
			}
			public void streamAddedStyle(IStream stream, IWarlockStyle style) {
				if (stream != null) {
					getStreamBufferStyles(stream).add(style);
				}
			}
			public void streamRemovedStyle(IStream stream, IWarlockStyle style) {
				if (stream != null) {
					getStreamBufferStyles(stream).remove(style);
				}
			}
			public void streamReceivedText(IStream stream, String text) {
				if (stream != null) {
					streamBuffers.get(stream).append(text);
				}
			}
		};
		
		for (IStream stream : getStreamsToBuffer())
		{
			streamBuffers.put(stream, new StringBuffer());
			stream.addStreamListener(listener);
		}
	}

	
	// IWarlockClient methods
	
	public ICommandHistory getCommandHistory() {
		return commandHistory;
	}
	
	public abstract void connect(String server, int port, String key) throws IOException;
	
	public void send(String command) {
		send(null, command);
	}
	
	public void send(String prefix, String command) {
		if(connection == null) {
			// Not yet connected to server
			return;
		}
		
		commandHistory.addCommand(command);
		
		String text = command + "\n";
		if(prefix != null)
			getDefaultStream().sendCommand(prefix + text);
		else
			getDefaultStream().sendCommand(text);
		
		try {
			connection.send(text);
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
	
	public StringBuffer getStreamBuffer(IStream stream) {
		if (streamBuffers.containsKey(stream))
			return streamBuffers.get(stream);
		else return null;
	}
	
	public List<IWarlockStyle> getStreamBufferStyles (IStream stream)
	{
		if (!streamStyles.containsKey(stream))
		{
			streamStyles.put(stream, new ArrayList<IWarlockStyle>());
		}
		return streamStyles.get(stream);
	}
	
	public IConnection getConnection() {
		return connection;
	}
}
