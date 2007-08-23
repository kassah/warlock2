package cc.warlock.stormfront.internal;

import java.util.Hashtable;

import cc.warlock.stormfront.IStormFrontProtocolHandler;


public class RightTagHandler extends DefaultTagHandler {
	
	private StringBuffer rightHandText;
	
	public RightTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	public String[] getTagNames() {
		return new String[] { "right" };
	}

	@Override
	public void handleStart(Hashtable<String,String> atts) {
		rightHandText = new StringBuffer();
	}
	
	public boolean handleCharacters(char[] ch, int start, int length) {
		rightHandText.append(ch, start, length);
		
		return true;
	}
	
	@Override
	public void handleEnd() {
		handler.getClient().getRightHand().set(rightHandText.toString());
		
		rightHandText = null;
	}
}
