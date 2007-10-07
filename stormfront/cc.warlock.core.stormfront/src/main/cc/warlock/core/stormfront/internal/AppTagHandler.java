/*
 * Created on Jan 15, 2005
 */
package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


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
	
	public void handleStart(StormFrontAttributeList attributes) {
		String characterName = attributes.getValue("char");
		String gameName = attributes.getValue("game");
		
		handler.getClient().getDefaultStream().getTitle().set("[" + gameName + "] " + characterName);
		handler.getClient().getCharacterName().set(characterName);
	}
}
