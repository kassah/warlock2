package cc.warlock.core.stormfront.internal;

import java.util.Map;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.IStormFrontTagHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


public class DirectionTagHandler implements IStormFrontTagHandler {
	private IStormFrontProtocolHandler protocol;
	private String currentTag;
	
	public DirectionTagHandler(IStormFrontProtocolHandler protocol) {
		this.protocol = protocol;
	}
	
	public String[] getTagNames() {
		return new String[] { "d" };
	}

	public boolean handleCharacters(char[] ch, int start, int length) {
		protocol.getClient().getCompass().set(String.copyValueOf(ch, start, length));
		
		return false;
	}

	public String getCurrentTag() {
		return currentTag;
	}

	public void handleEnd() {
	}

	public void handleStart(StormFrontAttributeList attributes) {
	}

	public void setCurrentTag(String tagName) {
		currentTag = tagName;
	}
	
	public Map<String, IStormFrontTagHandler> getTagHandlers() {
		return null;
	}
}

