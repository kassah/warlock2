package cc.warlock.core.script.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.warlock.core.script.Match;

public class RegexMatch extends Match {
	
	String matchText;
	Pattern regex;
	
	public RegexMatch(String text) {
		this(text, false);
	}
	
	public RegexMatch(String text, boolean ignoreCase) {
		matchText = text;
		
		int flags = 0;
		if(ignoreCase) flags |= Pattern.CASE_INSENSITIVE;
		
		regex = Pattern.compile(text, flags);
	}
	
	public boolean matches(String text) {
		Matcher m = regex.matcher(text);
		if(m.find()) {
			System.out.println("matched text: \"" + text + "\" with \"" + matchText + "\"");

			// TODO should set some variables for the groups here
			
			return true;
		} else {
			System.out.println("Didn't match text: \"" + text + "\" with \"" + matchText + "\"");
			return false;
		}
	}
}
