/*
 * Created on Sep 20, 2004
 */
package cc.warlock.core.stormfront.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cc.warlock.core.configuration.Profile;
import cc.warlock.core.network.Connection;
import cc.warlock.core.network.IConnection;
import cc.warlock.core.network.IConnectionListener;

/**
 * @author Marshall
 */
public class SGEConnection extends Connection implements IConnectionListener {

	public static final String SGE_SERVER = "eaccess.play.net";
	public static final int SGE_PORT = 7900;

	public static final int INVALID_PASSWORD = 0;
	public static final int INVALID_ACCOUNT = 1;
	public static final int ACCOUNT_REJECTED = 2;
	public static final int LOGIN_SUCCESS = 3;
	public static final int ACCOUNT_EXPIRED = 4;
	
	public static final String PROPERTY_KEY = "KEY";
	public static final String PROPERTY_GAMEHOST = "GAMEHOST";
	public static final String PROPERTY_GAMEPORT = "GAMEPORT";
	
	public static final String NEW_CHARACTER_CODE = "0";
	
	protected static final int SGE_NONE = 0;
	protected static final int SGE_INITIAL = 1;
	protected static final int SGE_ACCOUNT = 2;
	protected static final int SGE_MENU = 3;
	protected static final int SGE_GAME = 4;
	protected static final int SGE_PICK = 5;
	protected static final int SGE_CHARACTERS = 6;
	protected static final int SGE_LOAD = 7;
	
	protected static final int LOGIN_READY = 0;
	protected static final int LOGIN_FINISHED = 1;
	protected static final int GAMES_READY = 2;
	protected static final int CHARACTERS_READY = 3;
	protected static final int READY_TO_PLAY = 4;
	protected static final int SGE_ERROR = 5;
	
	protected int state, errorCode;
	protected String passwordHash;
	protected ArrayList<ISGEConnectionListener> sgeListeners;
	
	protected HashMap<String, String> games, characters, loginProperties;
	
	public SGEConnection ()
	{
		super();
		addConnectionListener(this);
		
		state = SGE_NONE;
		sgeListeners = new ArrayList<ISGEConnectionListener>();
		games = new HashMap<String, String>();
		characters = new HashMap<String, String>();
		loginProperties = new HashMap<String, String>();
	}
	
	protected void resetState () {
		state = SGE_NONE;
		passwordHash = null;
		games.clear();
		characters.clear();
		loginProperties.clear();
	}
	
	public void connect ()
	{
		try {
			resetState();
			connect (SGE_SERVER, SGE_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void addSGEConnectionListener (ISGEConnectionListener listener)
	{
		sgeListeners.add (listener);
	}
	
	public void login (String username, String password)
	{
		state = SGE_ACCOUNT;
		
		// This is an ugly hack. If we try to append the encrypted password as a "String", the java language String will
		// try interpreting the characters in that string as Unicode, and fail miserably, giving us a botched encrypted string.
		// The only way to make this work the right way is manually cast each and every "char" primitive to "byte" and send it
		// raw over the socket.
		
		try {
			byte usernameBytes[] = username.getBytes();
			byte bytes[] = new byte[2 + usernameBytes.length + 1 + password.length() + 1];
			bytes[0] = (byte) 'A'; bytes[1] = (byte) '\t';
			
			System.arraycopy(usernameBytes, 0, bytes, 2, usernameBytes.length);
			bytes[usernameBytes.length + 2] = (byte)'\t';
			
			char encrypted[] = encryptPassword(password, passwordHash);
			for (int i = 0; i < password.length(); i++)
			{
				int index = usernameBytes.length + 3 + i;
				bytes[index] = (byte) encrypted[i];
			}
			
			bytes[bytes.length - 1] = (byte) '\n';
			send (bytes);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected char[] encryptPassword (String password, String hash)
	{
		char encrypted[] = new char[33];
		for (int i = 0; i < 32 && password.length() > i  && hash.length() > i; i++)
		{
			encrypted[i] = (char) ((hash.charAt(i)  ^ (password.charAt(i) - 32)) + 32);
		}
		
		String encryptedString = "";
		for (int i = 0; i < password.length()+1; i++)
		{
			encryptedString += encrypted[i];
		}
		
		return encrypted;
	}
	
	public void selectGame (String gameCode)
	{
		state = SGE_GAME;
		
		try {
			sendLine("G\t" +gameCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void selectCharacter (String characterCode)
	{
		state = SGE_CHARACTERS;
		
		try {
			sendLine("L\t"+characterCode+"\tSTORM");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void connected(IConnection connection) {
		try {
			connection.sendLine("K");
			state = SGE_INITIAL;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void dataReady(IConnection connection, String line) {
		try {
			
			if (state == SGE_INITIAL)
			{
				passwordHash = line;
				fireEvent (LOGIN_READY);
				return;
			}
			
			switch (line.charAt(0))
			{
				case 'A':
				{
					if (line.indexOf("REJECT") != -1)
					{
						errorCode = ACCOUNT_REJECTED;
					}
					else if (line.indexOf("PASSWORD") != -1)
					{
						errorCode = INVALID_PASSWORD;
					}
					else if (line.indexOf("NORECORD") != -1)
					{
						errorCode = INVALID_ACCOUNT;
					}
					else errorCode = LOGIN_SUCCESS;

					
					if (errorCode == LOGIN_SUCCESS) {
						sendLine("M");
						state = SGE_GAME;

						fireEvent (LOGIN_FINISHED);
					}
					else {
						fireEvent (SGE_ERROR);
						
						disconnect();
					}

				} break;
				
				case 'M':
				{
					String tokens[] = line.split("\t");
					for (int i = 1; i < tokens.length; i+=2)
					{
						games.put(tokens[i], tokens[i+1]);
					}
					
					fireEvent(GAMES_READY);
					
				} break;
				
				case 'G':
				{
					sendLine("C");
				} break;
				
				case 'C':
				{
					characters.clear();
					String tokens[] = line.split("\t");
					for (int i = 5; i < tokens.length; i+=2)
					{
						characters.put(tokens[i], tokens[i+1]);
					}
					fireEvent (CHARACTERS_READY);
				} break;
				
				case 'L':
				{
					String tokens[] = line.split("\t");
					String loginResponse = tokens[1];
					if ("OK".equals(loginResponse))
					{
						for (int i = 2; i < tokens.length; i++)
						{
							String property[] = tokens[i].split("=");
							loginProperties.put(property[0], property[1]);
						}

						fireEvent(READY_TO_PLAY);
					}
					else if ("PROBLEM".equals(loginResponse))
					{
						errorCode = ACCOUNT_EXPIRED;
						fireEvent(SGE_ERROR);
					}
					
					disconnect();
				} break;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void fireEvent (final int event)
	{
		for (ISGEConnectionListener listener : sgeListeners) {
			switch (event) {
			case LOGIN_READY: listener.loginReady(SGEConnection.this); break;
			case LOGIN_FINISHED: listener.loginFinished(SGEConnection.this); break;
			case GAMES_READY: listener.gamesReady(SGEConnection.this, games); break;
			case CHARACTERS_READY: listener.charactersReady(SGEConnection.this, characters); break;
			case READY_TO_PLAY: listener.readyToPlay(SGEConnection.this, loginProperties); break;
			case SGE_ERROR: listener.sgeError(SGEConnection.this, errorCode); break;
			default: break;
			}
		}	
	}
	
	public void disconnected(IConnection connection) {

	}
	
	private static class AutoLoginListener extends SGEConnectionListener
	{
		public Profile profile;
		public boolean loggedIn = false;
		public Map <String,String> properties = null;
		
		public void loginReady(SGEConnection connection) {
			connection.login(profile.getAccount().getAccountName(), profile.getAccount().getPassword());
		}
		
		public void loginFinished(SGEConnection connection, int status) {/* noop */}
		
		public void gamesReady(SGEConnection connection,
				Map<String, String> games) {
			connection.selectGame(profile.getGameCode());
		}
		
		public void charactersReady(SGEConnection connection,
				Map<String, String> characters) {
			connection.selectCharacter(profile.getId());
		}
		
		@Override
		public void readyToPlay(SGEConnection connection,
				Map<String, String> loginProperties) {
			this.properties = loginProperties;
			this.loggedIn = true;
		}
	}
	
	public static Map<String, String> autoLogin (Profile profile, ISGEConnectionListener extraListener)
	{
		SGEConnection connection = new SGEConnection();
		AutoLoginListener listener = new AutoLoginListener();
		listener.profile = profile;
		
		connection.addSGEConnectionListener(listener);
		if (extraListener != null) {
			connection.addSGEConnectionListener(extraListener);
		}
			
		connection.connect();
		while (!listener.loggedIn) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return listener.properties;
	}

}
