package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


public class SpellTagHandler extends DefaultTagHandler {

	private StringBuffer spellText;
	
	public SpellTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "spell" };
	}
	
	@Override
	public void handleStart(StormFrontAttributeList atts) {
		spellText = new StringBuffer();
	}

	@Override
	public boolean handleCharacters(String characters) {
		spellText.append(characters);
		return true;
	}
	
	@Override
	public void handleEnd() {
		handler.getClient().getCurrentSpell().set(spellText.toString());
		
		spellText = null;
	}
}
