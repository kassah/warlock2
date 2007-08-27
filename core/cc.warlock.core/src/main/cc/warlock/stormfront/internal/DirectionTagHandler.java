package cc.warlock.stormfront.internal;

import java.util.Map;

import cc.warlock.stormfront.IStormFrontProtocolHandler;
import cc.warlock.stormfront.IStormFrontTagHandler;


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
		return true;
	}

	public String getCurrentTag() {
		return currentTag;
	}

	public void handleEnd() {
		// TODO Auto-generated method stub
		
	}

	public void handleStart(Map<String, String> attributes) {
		// TODO Auto-generated method stub
		
	}

	public void setCurrentTag(String tagName) {
		currentTag = tagName;
	}
	
	public Map<String, IStormFrontTagHandler> getTagHandlers() {
		return null;
	}
}

