/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.core.stormfront.client.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import cc.warlock.core.client.ICharacterStatus;
import cc.warlock.core.client.ICompass;
import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.IRoomListener;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IWarlockSkin;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.internal.CharacterStatus;
import cc.warlock.core.client.internal.ClientProperty;
import cc.warlock.core.client.internal.Compass;
import cc.warlock.core.client.internal.WarlockClient;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.script.IScript;
import cc.warlock.core.script.IScriptListener;
import cc.warlock.core.script.ScriptEngineRegistry;
import cc.warlock.core.script.configuration.ScriptConfiguration;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.client.BarStatus;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.network.StormFrontConnection;
import cc.warlock.core.stormfront.serversettings.server.Preset;
import cc.warlock.core.stormfront.serversettings.server.ServerSettings;
import cc.warlock.core.stormfront.serversettings.skin.DefaultSkin;
import cc.warlock.core.stormfront.serversettings.skin.IStormFrontSkin;

import com.martiansoftware.jsap.CommandLineTokenizer;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StormFrontClient extends WarlockClient implements IStormFrontClient, IScriptListener, IRoomListener {

	protected ICompass compass;
	protected ICharacterStatus status;
	protected ClientProperty<Integer> roundtime;
	protected ClientProperty<BarStatus> health, mana, fatigue, spirit;
	protected ClientProperty<String> leftHand, rightHand, currentSpell;
	protected StringBuffer buffer = new StringBuffer();
	protected IStormFrontProtocolHandler handler;
	protected ClientProperty<String> playerId, characterName, roomDescription;
	protected ServerSettings serverSettings;
	private Timer timer;
	protected SFTimerTask timerTask;
	private double currentTime = 0.0;
	private double rtEnd = -1.0;
	protected ArrayList<IScript> runningScripts;
	protected ArrayList<IScriptListener> scriptListeners;
	protected DefaultSkin skin;
	protected Hashtable<String, ClientProperty<String>> components = new Hashtable<String, ClientProperty<String>>();
	protected ClientProperty<GameMode> mode;
	
	public StormFrontClient() {
		super();
		compass = new Compass(this);
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
		serverSettings = new ServerSettings(this);
		skin = new DefaultSkin(serverSettings);
		timer = new Timer();
		timerTask = new SFTimerTask();
		timer.scheduleAtFixedRate(timerTask, 0, 100L);
		runningScripts = new ArrayList<IScript>();
		scriptListeners = new ArrayList<IScriptListener>();
		
		WarlockClientRegistry.activateClient(this);
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
			
			if(rtEnd >= 0.0) {
				int newRT = (int)(rtEnd - currentTime + 1);
				updateRoundtime(newRT);
			}
		}
	}
	
	public void startRoundtime (int seconds)
	{
		roundtime.activate();
		rtEnd = currentTime + seconds;
		updateRoundtime(seconds);
	}
	
	public void updateRoundtime (int newRT)
	{
		if(newRT <= 0) {
			newRT = 0;
			rtEnd = -1;
		}
		if(roundtime.get() != newRT)
			roundtime.set(newRT);
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

	public ICompass getCompass() {
		return compass;
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
	
	public ServerSettings getServerSettings() {
		return serverSettings;
	}
	
	public IProperty<String> getCharacterName() {
		return characterName;
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
	
	public void setComponent (String componentName, String value)
	{
		if (!components.containsKey(componentName)) {
			components.put(componentName, new ClientProperty<String>(this, componentName, value));
		}
		else {
			components.get(componentName).set(value);
		}
	}
	
	public IProperty<String> getComponent(String componentName) {
		if (components.containsKey(componentName))
			return components.get(componentName);
		
		return null;
	}
	
	@Override
	protected void finalize() throws Throwable {
		WarlockClientRegistry.removeClient(this);
		super.finalize();
	}
	
	public IWarlockStyle getCommandStyle() {
		Preset commandPreset = serverSettings.getPreset(Preset.PRESET_COMMAND);
		if (commandPreset == null) {
			return new WarlockStyle();
		}
		return commandPreset.getStyle();
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
}
