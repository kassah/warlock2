/*
 * Created on Jan 11, 2005
 */
package com.arcaner.warlock.network;

import java.io.IOException;
import java.net.Socket;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.arcaner.warlock.client.IWarlockClient;
import com.arcaner.warlock.client.stormfront.IStormFrontClient;
import com.arcaner.warlock.stormfront.internal.StormFrontProtocolHandler;

/**
 * @author Sean Proctor
 * @author Marshall
 *
 * The Internal Storm Front protocol handler. Not meant to be insantiated outside of Warlock.
 */
public class StormFrontConnection implements IConnection {
	protected String host;
	protected int port;
	protected IStormFrontClient client;
	protected Socket socket;
	protected String key;
	
	public StormFrontConnection (IStormFrontClient client, String key) {
		this.client = client;
		this.key = key;
	}
	
	public void connect(String host, int port)
	throws IOException {
		this.host = host;
		this.port = port;
		
		new Thread(new XmlParser()).start();
	}
	
	public void disconnect()
	throws IOException {
		// Stub Function
	}
	
	public void addConnectionListener (IConnectionListener listener) {
	}
	
	public void send (String toSend)
	throws IOException {
		send(toSend.getBytes());
	}
	
	public void send (byte[] bytes)
	throws IOException {
		socket.getOutputStream().write(bytes);
	}
	
	public void sendLine (String line)
	throws IOException {
		send (line + "\n");
	}
	
	public IWarlockClient getClient() {
		return client;
	}
	
	class XmlParser implements Runnable {
		public void run() {
			try {
				socket = new Socket(host, port);
				
				sendLine(key);
				sendLine("/FE:WARLOCK /XML");
				
				StormFrontStream inputStream = new StormFrontStream(StormFrontConnection.this, socket.getInputStream());
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();
				saxParser.parse(inputStream, new StormFrontProtocolHandler(client));
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

}
