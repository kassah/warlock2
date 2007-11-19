package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


public class LeftTagHandler extends DefaultTagHandler {

	private StringBuffer leftHandText;
	
	public LeftTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "left" };
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes, String newLine) {
		leftHandText = new StringBuffer();
	}
	
	@Override
	public boolean handleCharacters(String characters) {
		leftHandText.append(characters);
		
		return true;
	}
	
	@Override
	public void handleEnd(String newLine) {
		handler.getClient().getLeftHand().set(leftHandText.toString());
		
		leftHandText = null;
	}
}
