/*
 * Created on Jan 11, 2005
 */
package cc.warlock.network;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import org.antlr.runtime.CharStream;

import cc.warlock.client.IWarlockClient;
import cc.warlock.client.stormfront.IStormFrontClient;
import cc.warlock.stormfront.internal.SFLexerLexer;
import cc.warlock.stormfront.internal.SFParserParser;
import cc.warlock.stormfront.internal.StormFrontProtocolHandler;
import cc.warlock.stormfront.internal.StormFrontProtocolParser;

/**
 * @author Sean Proctor
 * @author Marshall
 *
 * The Internal Storm Front protocol handler. Not meant to be instantiated outside of Warlock.
 */
public class StormFrontConnection implements IConnection
{
	protected StormFrontProtocolHandler handler;
	protected IStormFrontClient client;
	protected String key, host;
	protected int port;
	protected ArrayList<IConnectionListener> listeners = new ArrayList<IConnectionListener>();
	protected Socket socket;
	
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
		new Thread(new SFParser()).start();
	}
	
	public void addConnectionListener(IConnectionListener listener) {
		listeners.add(listener);
	}
	
	public void disconnect() throws IOException {
		// TODO Auto-generated method stub
		
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
	
	class SFParser implements Runnable {
		public void run() {
			try {
				sendLine(key);
				sendLine("/FE:WARLOCK /VERSION:1.0.1.22 /XML\n");
				
				// StormFrontStream inputStream = new StormFrontStream(StormFrontConnection.this, socket.getInputStream());
				StormFrontProtocolParser parser = new StormFrontProtocolParser(socket.getInputStream());
				parser.setHandler(handler);
				parser.Document();
				
//				System.out.println("about to create input");
//				CharStream input = new StormFrontCharStream(socket.getInputStream());
//				System.out.println("about to pass input to lexer");
//				SFLexerLexer lex = new SFLexerLexer(input);
//				System.out.println("about to create token stream");
//				StormFrontTokenStream tokens = new StormFrontTokenStream(lex);
//				
//				System.out.println("about to run parser");
//				SFParserParser parser = new SFParserParser(tokens);
//				parser.setHandler(new StormFrontProtocolHandler(client));
//				parser.document();
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
