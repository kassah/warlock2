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
			setAttribute("0", m.group());
			for(int i = 1; i <= m.groupCount(); i++) {
				setAttribute(String.valueOf(i), m.group(i));
			}
			
			return true;
		} else {
			return false;
		}
	}
}
