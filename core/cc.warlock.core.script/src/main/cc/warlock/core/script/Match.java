/**
 * 
 */
package cc.warlock.core.script;

import java.util.HashMap;

abstract public class Match
{
	
	private HashMap<String, Object> data = new HashMap<String, Object>();
	
	public Object setAttribute(String key, Object value) {
		return data.put(key, value);
	}
	
	public Object getAttribute(String key) {
		return data.get(key);
	}
	
	abstract public boolean matches(String text);
}