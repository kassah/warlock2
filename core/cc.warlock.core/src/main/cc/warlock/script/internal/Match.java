/**
 * 
 */
package cc.warlock.script.internal;

import java.util.Hashtable;

import cc.warlock.script.IMatch;

public class Match implements IMatch
{
	public String matchText;
	public boolean regex, ignoreCase = false;
	public Hashtable<String,String> data = new Hashtable<String,String>();
	
	public Hashtable<String, String> getData() {
		return data;
	}
	
	public String getMatchText() {
		return matchText;
	}
	
	public boolean ignoreCase() {
		return ignoreCase;
	}
	
	public boolean isRegex() {
		return regex;
	}
}