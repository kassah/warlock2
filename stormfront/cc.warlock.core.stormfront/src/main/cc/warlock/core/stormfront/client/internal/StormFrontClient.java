/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.core.stormfront.client.internal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import cc.warlock.core.client.ICharacterStatus;
import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.IRoomListener;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IWarlockSkin;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.internal.CharacterStatus;
import cc.warlock.core.client.internal.ClientProperty;
import cc.warlock.core.client.internal.WarlockClient;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.client.settings.IClientSettings;
import cc.warlock.core.configuration.ConfigurationUtil;
import cc.warlock.core.script.IScript;
import cc.warlock.core.script.IScriptListener;
import cc.warlock.core.script.ScriptEngineRegistry;
import cc.warlock.core.script.configuration.ScriptConfiguration;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.client.BarStatus;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.network.StormFrontConnection;
import cc.warlock.core.stormfront.settings.IStormFrontClientSettings;
import cc.warlock.core.stormfront.settings.StormFrontServerSettings;
import cc.warlock.core.stormfront.settings.internal.StormFrontClientSettings;
import cc.warlock.core.stormfront.settings.skin.DefaultSkin;
import cc.warlock.core.stormfront.settings.skin.IStormFrontSkin;
import cc.warlock.core.stormfront.xml.StormFrontDocument;
import cc.warlock.core.stormfront.xml.StormFrontElement;

import com.martiansoftware.jsap.CommandLineTokenizer;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StormFrontClient extends WarlockClient implements IStormFrontClient, IScriptListener, IRoomListener {

	protected ICharacterStatus status;
	protected ClientProperty<Integer> roundtime;
	protected ClientProperty<BarStatus> health, mana, fatigue, spirit;
	protected ClientProperty<String> leftHand, rightHand, currentSpell;
	protected StringBuffer buffer = new StringBuffer();
	protected IStormFrontProtocolHandler handler;
	protected ClientProperty<String> playerId, characterName, roomDescription;
	protected StormFrontClientSettings clientSettings;
	protected StormFrontServerSettings serverSettings;
	private Timer timer;
	protected SFTimerTask timerTask;
	private double currentTime = 0.0;
	private double rtRemaining = -1.0;
	protected ArrayList<IScript> runningScripts;
	protected ArrayList<IScriptListener> scriptListeners;
	protected DefaultSkin skin;
	protected HashMap<String, ClientProperty<String>> components = new HashMap<String, ClientProperty<String>>();
	protected HashMap<String, IStream> componentStreams = new HashMap<String, IStream>();
	protected ClientProperty<GameMode> mode;
	protected HashMap<String, String> commands;
	
	public StormFrontClient() {
		super();
		
		status = new CharacterStatus(this);
		leftHand = new ClientProperty<String>(this, "leftHand", null);
		rightHand = new ClientProperty<String>(this, "rightHand", null);
		currentSpell = new ClientProperty<String>(this, "currentSpell", null);
		
		roundtime = new ClientProperty<Integer>(this, "roundtime", 0);
		health = new ClientProperty<BarStatus>(this, "health", null);
		mana = new ClientProperty<BarStatus>(this, "mana", null);
		fatigue = new ClientProperty<BarStatus>(this, "fatigue", null);
		spirit = new ClientProperty<BarStatus>(this, "spirit", null);
		playerId = new ClientProperty<String>(this, "playerId", null);
		characterName = new ClientProperty<String>(this, "characterName", null);
		roomDescription = new ClientProperty<String>(this, "roomDescription", null);
		mode = new ClientProperty<GameMode>(this, "gameMode", GameMode.Game);

		timer = new Timer();
		timerTask = new SFTimerTask();
		timer.scheduleAtFixedRate(timerTask, 0, 100L);
		runningScripts = new ArrayList<IScript>();
		scriptListeners = new ArrayList<IScriptListener>();
		
		WarlockClientRegistry.activateClient(this);
	}
	
	@Override
	protected IClientSettings createClientSettings() {
		clientSettings = new StormFrontClientSettings(this);

		skin = new DefaultSkin(clientSettings);
		skin.loadDefaultStyles(clientSettings.getHighlightConfigurationProvider());
		
		serverSettings = new StormFrontServerSettings();
		clientSettings.addChildProvider(serverSettings);
		
		return clientSettings;
	}
	
	@Override
	public void send(String prefix, String command) {
		String scriptPrefix = ScriptConfiguration.instance().getScriptPrefix();
		
		if (command.startsWith(scriptPrefix)){
			runScriptCommand(command);
		} else {
			super.send(prefix, command);
		}
	}
	
	protected  void runScriptCommand(String command) {
		command = command.substring(1);
		command = command.replaceAll("[\\r\\n]", "");
		
		int firstSpace = command.indexOf(" ");
		String scriptName = command.substring(0, (firstSpace < 0 ? command.length() : firstSpace));
		String[] arguments = new String[0];
		
		if (firstSpace > 0)
		{
			String args = command.substring(firstSpace+1);
			arguments = CommandLineTokenizer.tokenize(args);
		}
		
		IScript script = ScriptEngineRegistry.startScript(scriptName, this, arguments);
		if (script != null)
		{
			script.addScriptListener(this);
			for (IScriptListener listener : scriptListeners) listener.scriptStarted(script);
			runningScripts.add(script);
		}
	}
	
	public void scriptAdded(IScript script) {
		for (IScriptListener listener : scriptListeners) listener.scriptAdded(script);
	}
	
	public void scriptRemoved(IScript script) {
		for (IScriptListener listener : scriptListeners) listener.scriptRemoved(script);
	}
	
	public void scriptPaused(IScript script) {
		for (IScriptListener listener : scriptListeners) listener.scriptPaused(script);
	}
	
	public void scriptResumed(IScript script) {
		for (IScriptListener listener : scriptListeners) listener.scriptResumed(script);
	}
	
	public void scriptStarted(IScript script) {
		for (IScriptListener listener : scriptListeners) listener.scriptStarted(script);
	}
	
	public void scriptStopped(IScript script, boolean userStopped) {
		runningScripts.remove(script);
		
		for (IScriptListener listener : scriptListeners) listener.scriptStopped(script, userStopped);
	}
	
	public IProperty<Integer> getRoundtime() {
		return roundtime;
	}

	private class SFTimerTask extends TimerTask
	{
		
		public void run () 
		{
			currentTime += 0.1;
			
			if(rtRemaining > 0.0) {
				rtRemaining -= 0.1;
				int newRT = (int)Math.ceil(rtRemaining);
				if(roundtime.get() != newRT)
					roundtime.set(newRT);
			}
		}
	}
	
	public void startRoundtime (int seconds)
	{
		roundtime.activate();
		rtRemaining = seconds;
		roundtime.set(seconds);
	}
	
	public IProperty<BarStatus> getHealth() {
		return health;
	}

	public IProperty<BarStatus> getMana() {
		return mana;
	}

	public IProperty<BarStatus> getFatigue() {
		return fatigue;
	}

	public IProperty<BarStatus> getSpirit() {
		return spirit;
	}

	public void connect(String server, int port, String key) throws IOException {
		connection = new StormFrontConnection(this, key);
		connection.connect(server, port);
		
		WarlockClientRegistry.clientConnected(this);
	}
	
	public void streamCleared() {
		// TODO Auto-generated method stub
		
	}

	public ClientProperty<String> getPlayerId() {
		return playerId;
	}
	
	public IStormFrontClientSettings getStormFrontClientSettings() {
		return clientSettings;
	}
	
	public IProperty<String> getCharacterName() {
		return characterName;
	}
	
	public IProperty<String> getClientId() {
		return playerId;
	}
	
	public IProperty<String> getLeftHand() {
		return leftHand;
	}
	
	public IProperty<String> getRightHand() {
		return rightHand;
	}
	
	public IProperty<String> getCurrentSpell() {
		return currentSpell;
	}
	
	public ICharacterStatus getCharacterStatus() {
		return status;
	}
	
	public Collection<IScript> getRunningScripts() {
		return runningScripts;
	}
	
	public void addScriptListener(IScriptListener listener)
	{
		scriptListeners.add(listener);
	}
	
	public void removeScriptListener (IScriptListener listener)
	{
		if (scriptListeners.contains(listener))
			scriptListeners.remove(listener);
	}
	
	public IWarlockSkin getSkin() {
		return skin;
	}
	
	public IStormFrontSkin getStormFrontSkin() {
		return skin;
	}
	
	@Override
	public IStream getStream(String streamName) {
		return StormFrontStream.fromNameAndClient(this, streamPrefix + streamName);
	}
	
	public IStream getThoughtsStream() {
		return getStream(THOUGHTS_STREAM_NAME);
	}
	
	public IStream getInventoryStream() {
		return getStream(INVENTORY_STREAM_NAME);
	}
	
	public IStream getDeathsStream() {
		return getStream(DEATH_STREAM_NAME);
	}
	
	public IStream getRoomStream() {
		return getStream(ROOM_STREAM_NAME);
	}
	
	@Override
	public IStream getDefaultStream() {
		return getStream(DEFAULT_STREAM_NAME);
	}

	public IStream getFamiliarStream() {
		return getStream(FAMILIAR_STREAM_NAME);
	}
	
	public void setComponent (String name, String value, IStream stream)
	{
		components.put(name, new ClientProperty<String>(this, name, value));
		componentStreams.put(name, stream);
		//stream.addComponent(name);
	}
	
	public void updateComponent(String name, String value) {
		components.get(name).set(value);
		componentStreams.get(name).updateComponent(name, value);
	}
	
	public IProperty<String> getComponent(String componentName) {
		return components.get(componentName);
	}
	
	@Override
	protected void finalize() throws Throwable {
		WarlockClientRegistry.removeClient(this);
		super.finalize();
	}
	
	public IWarlockStyle getCommandStyle() {
		IWarlockStyle style = clientSettings.getNamedStyle(StormFrontClientSettings.PRESET_COMMAND);
		if (style == null) {
			return new WarlockStyle();
		}
		return style;
	}
	
	public long getTime() {
		return (long)currentTime;
	}
	
	public void setTime(long time) {
		if(time < (long)currentTime)
			currentTime = time + 0.9;
		else if(time > (long)currentTime)
			currentTime = time;
	}
	
	public void loadCmdlist()
	{
		try {
			commands  = new HashMap<String, String>();
			FileInputStream stream = new FileInputStream(ConfigurationUtil.getConfigurationFile("cmdlist1.xml"));
			StormFrontDocument document = new StormFrontDocument(stream);
			stream.close();
			
			StormFrontElement cmdlist = document.getRootElement();
			for (StormFrontElement cliElement : cmdlist.elements())
			{
				if(cliElement.getName().equals("cli")) {
					String coord = cliElement.attributeValue("coord");
					String command = cliElement.attributeValue("command");
					
					if(coord != null && command != null)
						commands.put(coord, command);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getCommand(String coord) {
		if(commands == null) return null;
		return commands.get(coord);
	}
	/* Internal only.. meant for importing/exporting stormfront's savings */
	public StormFrontServerSettings getServerSettings() {
		return serverSettings;
	}
}
