/*
 * Created on Jan 15, 2005
 */
package cc.warlock.core.stormfront.internal;

import java.util.Map;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;


/**
 * @author Marshall
 */
public class AppTagHandler extends DefaultTagHandler {
	
	public AppTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	public String[] getTagNames() {
		return new String[] { "app" };
	}
	
	public void handleStart(Map<String,String> attributes) {
		String characterName = attributes.get("char");
		String gameName = attributes.get("game");
		
		handler.getClient().getDefaultStream().getTitle().set("[" + gameName + "] " + characterName);
		handler.getClient().getCharacterName().set(characterName);
	}
}
