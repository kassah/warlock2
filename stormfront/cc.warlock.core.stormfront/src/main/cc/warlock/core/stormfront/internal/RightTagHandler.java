package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


public class RightTagHandler extends DefaultTagHandler {
	
	private StringBuffer rightHandText;
	
	public RightTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "right" };
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes) {
		rightHandText = new StringBuffer();
	}
	
	@Override
	public boolean handleCharacters(String characters) {
		rightHandText.append(characters);
		
		return true;
	}
	
	@Override
	public void handleEnd() {
		handler.getClient().getRightHand().set(rightHandText.toString());
		
		rightHandText = null;
	}
}
