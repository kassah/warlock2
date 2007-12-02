/*
 * Created on Jan 11, 2005
 */
package cc.warlock.core.stormfront.network;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.network.IConnection;
import cc.warlock.core.network.IConnectionListener;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.internal.StormFrontProtocolHandler;
import cc.warlock.core.stormfront.internal.StormFrontProtocolParser;

/**
 * @author Sean Proctor
 * @author Marshall
 *
 * The Internal Storm Front protocol handler. Not meant to be instantiated outside of Warlock.
 */
public class StormFrontConnection implements IConnection
{
	protected StormFrontProtocolHandler handler;
	private StormFrontReader reader;
	private StormFrontProtocolParser parser;
	protected IStormFrontClient client;
	protected String key, host;
	protected int port;
	protected ArrayList<IConnectionListener> listeners = new ArrayList<IConnectionListener>();
	protected Socket socket;
	protected boolean connected = false;
	
	public StormFrontConnection (IStormFrontClient client, String key) {
		super();
		this.client = client;
		this.key = key;
		this.handler = new StormFrontProtocolHandler(client);
	}
	
	public void connect(String host, int port)
	throws IOException {
		this.host = host;
		this.port = port;
		
		this.socket = new Socket(host, port);
		connected = true;
		new Thread(new SFParser()).start();
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public void addConnectionListener(IConnectionListener listener) {
		listeners.add(listener);
	}
	
	public void disconnect() throws IOException {
		sendLine("quit");
	}
	
	public void send(byte[] bytes) throws IOException {
		socket.getOutputStream().write(bytes);
	}
	
	public void send(String toSend) throws IOException {
		send(toSend.getBytes());
	}
	
	public void sendLine (String line)
	throws IOException {
		send ("<c>" + line + "\n");
	}
	
	public IWarlockClient getClient() {
		return client;
	}
	
	public void dataReady (String line)
	{
		for (IConnectionListener listener : listeners)
		{
			listener.dataReady(this, line);
		}
	}
	
	public void passThrough() {
		parser.passThrough();
	}
	
	class SFParser implements Runnable {
		public void run() {
			try {
				sendLine(key);
				sendLine("/FE:WARLOCK /VERSION:1.0.1.22 /XML\n");
				
				reader = new StormFrontReader(StormFrontConnection.this, socket.getInputStream());
				parser = new StormFrontProtocolParser(reader);
				parser.setHandler(handler);
				parser.Document();
				
				connected = false;
				client.getDefaultStream().echo(
						"******************************\n"+
						"* Disconnected from the game *\n" +
						"******************************\n");
			
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getKey() {
		return key;
	}
}
