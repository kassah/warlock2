package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


public class LeftTagHandler extends DefaultTagHandler {

	private StringBuffer leftHandText;
	
	public LeftTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	public String[] getTagNames() {
		return new String[] { "left" };
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes) {
		leftHandText = new StringBuffer();
	}
	
	public boolean handleCharacters(char[] ch, int start, int length) {
		leftHandText.append(ch, start, length);
		
		return true;
	}
	
	@Override
	public void handleEnd() {
		handler.getClient().getLeftHand().set(leftHandText.toString());
		
		leftHandText = null;
	}
}
