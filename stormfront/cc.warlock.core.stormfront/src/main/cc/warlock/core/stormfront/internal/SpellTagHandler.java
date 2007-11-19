package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


public class SpellTagHandler extends DefaultTagHandler {

	private StringBuffer spellText;
	
	public SpellTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	public String[] getTagNames() {
		return new String[] { "spell" };
	}
	
	@Override
	public void handleStart(StormFrontAttributeList atts, String newLine) {
		spellText = new StringBuffer();
	}

	public boolean handleCharacters(char[] ch, int start, int length) {
		spellText.append(ch, start, length);
		return true;
	}
	
	@Override
	public void handleEnd(String newLine) {
		handler.getClient().getCurrentSpell().set(spellText.toString());
		
		spellText = null;
	}
}
