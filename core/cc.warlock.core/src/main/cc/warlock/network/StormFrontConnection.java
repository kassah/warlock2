/*
 * Created on Jan 11, 2005
 */
package cc.warlock.network;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;

import cc.warlock.client.IWarlockClient;
import cc.warlock.client.stormfront.IStormFrontClient;
import cc.warlock.stormfront.internal.SFLexerLexer;
import cc.warlock.stormfront.internal.SFParserParser;
import cc.warlock.stormfront.internal.StormFrontProtocolHandler;

/**
 * @author Sean Proctor
 * @author Marshall
 *
 * The Internal Storm Front protocol handler. Not meant to be instantiated outside of Warlock.
 */
public class StormFrontConnection implements IConnection {
	protected String host;
	protected int port;
	protected IStormFrontClient client;
	protected Socket socket;
	protected String key;
	private ArrayList<IConnectionListener> listeners = new ArrayList<IConnectionListener>();
	
	public StormFrontConnection (IStormFrontClient client, String key) {
		this.client = client;
		this.key = key;
	}
	
	public void connect(String host, int port)
	throws IOException {
		this.host = host;
		this.port = port;
		
		System.out.print("about to create parser thread\n");
		new Thread(new SFParser()).start();
	}
	
	public void disconnect()
	throws IOException {
		// Stub Function
	}
	
	public void addConnectionListener (IConnectionListener listener) {
		listeners.add(listener);
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
		send ("<c>" + line + "\n");
	}
	
	public IWarlockClient getClient() {
		return client;
	}
	
	public void dataReady (String data)
	{
		for (IConnectionListener listener : listeners) listener.dataReady(this, data);
	}
	
	class SFParser implements Runnable {
		public void run() {
			try {
				System.out.print("about to connect to socket\n");
				System.out.flush();
				socket = new Socket(host, port);
				
				System.out.print("about to send lines\n");
				System.out.flush();
				sendLine(key);
				sendLine("/FE:WARLOCK /VERSION:1.0.1.22 /XML\n");
				
				// StormFrontStream inputStream = new StormFrontStream(StormFrontConnection.this, socket.getInputStream());
				System.out.print("about to create input\n");
				System.out.flush();
				CharStream input = new ANTLRInputStream(socket.getInputStream());
				System.out.print("about to pass input to lexer\n");
				System.out.flush();
				SFLexerLexer lex = new SFLexerLexer(input);
				System.out.print("about to create token stream\n");
				System.out.flush();
				CommonTokenStream tokens = new CommonTokenStream(lex);
				
				System.out.print("about to run parser\n");
				System.out.flush();
				SFParserParser parser = new SFParserParser(tokens);
				parser.setHandler(new StormFrontProtocolHandler(client));
				parser.document();
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

}
