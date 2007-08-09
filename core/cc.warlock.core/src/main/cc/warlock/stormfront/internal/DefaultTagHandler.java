package cc.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import cc.warlock.stormfront.IStormFrontProtocolHandler;
import cc.warlock.stormfront.IStormFrontTagHandler;

abstract public class DefaultTagHandler implements IStormFrontTagHandler {
	protected IStormFrontProtocolHandler handler;
	protected String currentTag;
	
	public DefaultTagHandler(IStormFrontProtocolHandler handler) {
		this.handler = handler;
		handler.registerHandler(this);
	}
	
	public abstract String[] getTagNames();
	
	public void handleStart(Attributes atts) {
		// TODO Auto-generated method stub

	}

	public void handleEnd() {
		// TODO Auto-generated method stub

	}

	public boolean handleCharacters(char[] ch, int start, int length) {
		return false;
	}
	
	public String getCurrentTag() {
		return currentTag;
	}
	
	public void setCurrentTag(String tagName) {
		this.currentTag = tagName;
	}
}