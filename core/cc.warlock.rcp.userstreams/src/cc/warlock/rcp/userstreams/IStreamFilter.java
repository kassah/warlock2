/**
 * 
 */
package cc.warlock.rcp.userstreams;

import cc.warlock.core.client.WarlockString;

/**
 * @author Will Robertson
 * Interface for storing a stream filter
 */
public interface IStreamFilter {
	public enum type { string, regex }
	
	/*
	 * Does the text matches this filter?
	 */
	public boolean match(String text);
	public boolean match(WarlockString text);
	
	/*
	 * Sets the type of filter { string, regex }
	 */
	public void setType(type filterType);
	
	/*
	 * Get the type of filter { string, regex }
	 */
	public type getType();
	
	/*
	 * Sets the content of the filter to match against
	 */
	public void setContent(String content);
	
	/*
	 * Get the content of the filter
	 */
	public String getContent();
}
