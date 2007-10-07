package cc.warlock.core.stormfront.internal;

import java.util.HashMap;
import java.util.Map;

import cc.warlock.core.stormfront.IStormFrontTagHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;

abstract public class BaseTagHandler implements IStormFrontTagHandler {
	protected String currentTag;
	protected Map<String, IStormFrontTagHandler> tagHandlers;
	
	public abstract String[] getTagNames();
	
	public void handleStart(StormFrontAttributeList attributes) { }

	public void handleEnd() { }

	public boolean handleCharacters(char[] ch, int start, int length) {
		return false;
	}
	
	public String getCurrentTag() {
		return currentTag;
	}
	
	public void setCurrentTag(String tagName) {
		this.currentTag = tagName;
	}
	
	protected void addTagHandler(String tagName, IStormFrontTagHandler tagHandler) {
		if(tagHandlers == null) {
			tagHandlers = new HashMap<String, IStormFrontTagHandler>();
		}
		tagHandlers.put(tagName, tagHandler);
	}
	
	protected void addTagHandler(IStormFrontTagHandler tagHandler) {
		for(String tagName : tagHandler.getTagNames()) {
			addTagHandler(tagName, tagHandler);
		}
	}
	
	public Map<String, IStormFrontTagHandler> getTagHandlers() {
		return tagHandlers;
	}
}
