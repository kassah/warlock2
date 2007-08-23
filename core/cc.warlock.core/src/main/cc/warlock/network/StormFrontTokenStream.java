package cc.warlock.network;

import java.util.ArrayList;
import java.util.Hashtable;

import org.antlr.runtime.Token;
import org.antlr.runtime.TokenSource;
import org.antlr.runtime.TokenStream;

public class StormFrontTokenStream implements TokenStream {

	public static final int MAX_TOKENS = 500;
	
	protected TokenSource source;
	protected ArrayList<Token> tokens = new ArrayList<Token>();
	protected Hashtable<Integer,Integer> channelOverrides = new Hashtable<Integer,Integer>();
	protected int channel = Token.DEFAULT_CHANNEL;
	protected int currentToken = -1;
	protected int marker;
	
	public StormFrontTokenStream (TokenSource source)
	{
		this.source = source;
	}
	
	private Token readNextToken ()
	{
		if (this.tokens.size() >= MAX_TOKENS)
		{
			this.tokens.remove(0);
			currentToken--;
		}
		
		Token token = source.nextToken();
		this.tokens.add(token);
		
		return token;
	}
	
	protected Token LB(int k) {
		if ( currentToken == -1 ) {
			readNextToken();
		}
		if ( k==0 ) {
			return null;
		}
		if ( (currentToken-k)<0 ) {
			return null;
		}

		int i = currentToken;
		int n = 1;
		// find k good tokens looking backwards
		while ( n<=k ) {
			// skip off-channel tokens
			i = skipOffTokenChannelsReverse(i-1); // leave p on valid token
			n++;
		}
		if ( i<0 ) {
			return null;
		}
		return (Token)tokens.get(i);
	}
	
	public Token LT(int k) {
		if ( currentToken == -1 ) {
			readNextToken();
		}
		if ( k==0 ) {
			return null;
		}
		if ( k<0 ) {
			return LB(-k);
		}
		//System.out.print("LT(p="+p+","+k+")=");
		int readAhead = tokens.size() - (currentToken+k-1);
		
		if ( readAhead > 0 ) {
			Token token = null;
			for (int i = 0; i < readAhead; i++)
			{
				token = readNextToken();
			}
			return token;
		}
		
		//System.out.println(tokens.get(p+k-1));
		int i = currentToken;
		int n = 1;
		// find k good tokens
		while ( n<k ) {
			// skip off-channel tokens
			i = skipOffTokenChannels(i+1); // leave p on valid token
			n++;
		}
		if ( i>=tokens.size() ) {
			return Token.EOF_TOKEN;
		}
        return (Token)tokens.get(i);
	}

	public Token get(int i) {
		return tokens.get(i);
	}

	public TokenSource getTokenSource() {
		return source;
	}

	public String toString(int start, int stop) {
		if ( start<0 || stop<0 ) {
			return null;
		}
		if ( currentToken == -1 ) {
			readNextToken();
		}
		if ( stop>=tokens.size() ) {
			stop = tokens.size()-1;
		}
 		StringBuffer buf = new StringBuffer();
		for (int i = start; i <= stop; i++) {
			Token t = tokens.get(i);
			buf.append(t.getText());
		}
		return buf.toString();
	}

	public String toString(Token start, Token stop) {
		if ( start!=null && stop!=null ) {
			return toString(start.getTokenIndex(), stop.getTokenIndex());
		}
		return null;
	}

	public int LA(int i) {
		return LT(i).getType();
	}

	public void consume() {
		if (currentToken < tokens.size())
		{
			currentToken++;
			currentToken = skipOffTokenChannels(currentToken);
		}
	}

	public int index() {
		return currentToken;
	}

	public int mark() {
		if (currentToken == -1) {
			readNextToken();
		}
		
		marker = currentToken;
		return marker;
	}

	public void release(int marker) {
		// TODO Auto-generated method stub

	}

	public void rewind() {
		seek(marker);
	}

	public void rewind(int marker) {
		seek(marker);
	}

	public void seek(int index) {
		currentToken = index;
	}

	public int size() {
		return tokens.size();
	}
	
	protected int skipOffTokenChannels(int i) {
		int n = tokens.size();
		
		while (tokens.get(i).getChannel() != channel)
		{
			if (i <= n -1)
			{
				i++;
			}
			else {
				readNextToken();
				i++;
			}
		}
		
		return i;
	}

	protected int skipOffTokenChannelsReverse(int i) {
		while ( i>=0 && ((Token)tokens.get(i)).getChannel()!=channel ) {
			i--;
		}
		return i;
	}

}
