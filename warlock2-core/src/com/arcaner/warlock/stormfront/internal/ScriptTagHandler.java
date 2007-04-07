package com.arcaner.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

public class ScriptTagHandler extends DefaultTagHandler {
	private String name, comment;
	private StringBuffer contents;
	
	public ScriptTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return "s";
	}
	
	@Override
	public void handleStart(Attributes attributes) {
		name = attributes.getValue("name");
		comment = attributes.getValue("comment");
		contents = new StringBuffer();
	}
	
	@Override
	public boolean handleCharacters(char[] ch, int start, int length) {
		String text = String.copyValueOf(ch, start, length);
		contents.append(text);
		
		return true;
	}

	@Override
	public void handleEnd() {
		StormFrontScriptRepository.addScript(name, comment, contents.toString());
		
		name = comment = null;
		contents = null;
	}
}
