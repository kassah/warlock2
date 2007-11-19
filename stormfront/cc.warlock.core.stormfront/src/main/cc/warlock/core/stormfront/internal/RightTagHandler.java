package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


public class RightTagHandler extends DefaultTagHandler {
	
	private StringBuffer rightHandText;
	
	public RightTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	public String[] getTagNames() {
		return new String[] { "right" };
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes, String newLine) {
		rightHandText = new StringBuffer();
	}
	
	@Override
	public boolean handleCharacters(char[] ch, int start, int length) {
		rightHandText.append(ch, start, length);
		
		return true;
	}
	
	@Override
	public void handleEnd(String newLine) {
		handler.getClient().getRightHand().set(rightHandText.toString());
		
		rightHandText = null;
	}
}
