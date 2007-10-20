package cc.warlock.core.script.internal;

import cc.warlock.core.script.Match;

public class TextMatch extends Match {
	
	private String matchText;
	private String matchLine;//BFisher - For getting line we matched against.
	private boolean ignoreCase;
	
	public TextMatch(String text) {
		this(text, true);
	}

	//BFisher - Return the line matched against for JS scripting.
	//See matches() below and matchLine above.
	public String getMatchedLine() {
		return this.matchLine;
	}
	
	public TextMatch(String text, boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
		
		if(ignoreCase) {
			matchText = text.toLowerCase();
		} else {
			matchText = text;
		}
	}

	public boolean matches(String text) {
		boolean rv = false;

		if(ignoreCase) {
			if(text.toLowerCase().contains(matchText)) {
				rv = true;
				//BFisher - Set text line matched.
				this.matchLine = text;
			}
		} else {
			if(text.matches(matchText)) {
				rv = true;	
				//BFisher - Set text line matched.
				this.matchLine = text;
			}
		}
		/*
		if(rv) {
			System.out.println("matched text: \"" + text + "\" with \"" + matchText + "\"");
		} else {
			System.out.println("Didn't match text: \"" + text + "\" with \"" + matchText + "\"");
		}
		*/

		return rv;
	}
}
