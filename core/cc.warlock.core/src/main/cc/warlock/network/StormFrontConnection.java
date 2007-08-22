/*
 * Created on Jan 11, 2005
 */
package cc.warlock.network;

import java.io.IOException;
import java.net.Socket;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

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
public class StormFrontConnection extends Connection implements IConnectionListener{
	protected StormFrontProtocolHandler handler;
	protected IStormFrontClient client;
	protected String key;
	
	public StormFrontConnection (IStormFrontClient client, String key) {
		super();
		this.client = client;
		this.key = key;
		this.handler = new StormFrontProtocolHandler(client);
		
		addConnectionListener(this);
	}
	
	public void connect(String host, int port)
	throws IOException {
		this.host = host;
		this.port = port;
		
		super.connect(host, port);
	}
	
	public void sendLine (String line)
	throws IOException {
		send ("<c>" + line + "\n");
	}
	
	public IWarlockClient getClient() {
		return client;
	}
	
	public void connected(IConnection connection) {
		try {
			sendLine(key);
			sendLine("/FE:WARLOCK /VERSION:1.0.1.22 /XML\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void dataReady(IConnection connection, String line) {
		CharStream input = new ANTLRStringStream(line);
		SFLexerLexer lex = new SFLexerLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lex);
		
		SFParserParser parser = new SFParserParser(tokens);
		parser.setHandler(handler);
		
		try {
			parser.document();
		} catch (RecognitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void disconnected(IConnection connection) {}
	
//	class SFParser implements Runnable {
//		public void run() {
//			try {
//				
//				// StormFrontStream inputStream = new StormFrontStream(StormFrontConnection.this, socket.getInputStream());
//				System.out.print("about to create input\n");
//				System.out.flush();
//				CharStream input = new ANTLRInputStream(socket.getInputStream());
//				System.out.print("about to pass input to lexer\n");
//				System.out.flush();
//				SFLexerLexer lex = new SFLexerLexer(input);
//				System.out.print("about to create token stream\n");
//				System.out.flush();
//				CommonTokenStream tokens = new CommonTokenStream(lex);
//				
//				System.out.print("about to run parser\n");
//				System.out.flush();
//				SFParserParser parser = new SFParserParser(tokens);
//				parser.setHandler(new StormFrontProtocolHandler(client));
//				parser.document();
//			} catch (Throwable t) {
//				t.printStackTrace();
//			}
//		}
//	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

}
