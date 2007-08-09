package com.arcaner.warlock.script;

import java.util.Hashtable;

public interface IMatch {

	public String getMatchText();
	
	public boolean isRegex();
	
	public boolean ignoreCase();
	
	public Hashtable<String, String> getData();
}
