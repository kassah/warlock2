package cc.warlock.stormfront.internal;

import java.util.Hashtable;

import cc.warlock.stormfront.IStormFrontProtocolHandler;


public class LeftTagHandler extends DefaultTagHandler {

	private StringBuffer leftHandText;
	
	public LeftTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	public String[] getTagNames() {
		return new String[] { "left" };
	}

	@Override
	public void handleStart(Hashtable<String,String> atts) {
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
