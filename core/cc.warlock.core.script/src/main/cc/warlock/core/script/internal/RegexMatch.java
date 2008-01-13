package cc.warlock.core.script.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.warlock.core.script.IMatch;

public class RegexMatch implements IMatch {
	
	Pattern regex;
	ArrayList<String> groups = new ArrayList<String>();
	
	public RegexMatch(String text) {
		this(text, false);
	}
	
	public RegexMatch(String text, boolean ignoreCase) {
		
		int flags = 0;
		if(ignoreCase) flags |= Pattern.CASE_INSENSITIVE;
		
		regex = Pattern.compile(text, flags);
	}
	
	public boolean matches(String text) {
		groups.clear();
		Matcher m = regex.matcher(text);
		if(m.find()) {
			groups.add(m.group());
			for(int i = 1; i <= m.groupCount(); i++) {
				groups.add(m.group(i));
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	public String getText() {
		return regex.pattern();
	}
	public Collection<String> groups() {
		return groups;
	}
}
