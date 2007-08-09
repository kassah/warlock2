package com.arcaner.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

public class SpellTagHandler extends DefaultTagHandler {

	private StringBuffer spellText;
	
	public SpellTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	public String[] getTagNames() {
		return new String[] { "spell" };
	}
	
	@Override
	public void handleStart(Attributes atts) {
		spellText = new StringBuffer();
	}

	public boolean handleCharacters(char[] ch, int start, int length) {
		spellText.append(ch, start, length);
		return true;
	}
	
	@Override
	public void handleEnd() {
		handler.getClient().getCurrentSpell().set(spellText.toString());
		
		spellText = null;
	}
}
