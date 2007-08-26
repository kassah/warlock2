package cc.warlock.stormfront.internal;

import java.util.Hashtable;

import cc.warlock.stormfront.IStormFrontProtocolHandler;


public class InvTagHandler extends DefaultTagHandler {

	public InvTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	public String[] getTagNames() {
		return new String[] { "inv" };
	}

	public void handleStart(Hashtable<String,String> attributes) {
	}
	
	public boolean handleCharacters(char[] ch, int start, int length) {
		return true;
	}
}
