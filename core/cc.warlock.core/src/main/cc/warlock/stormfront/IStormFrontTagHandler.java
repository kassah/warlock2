/*
 * Created on Jan 15, 2005
 */
package cc.warlock.stormfront;

import java.util.Map;

/**
 * @author Marshall
 * 
 * Implementors of this interface will supply a list of XPath expressions which will be tested on the
 * received XML document from StormFront, and if any (or all -- depending on the implementation) match,
 * the interfaces "handleNodes" method will be called with the subsequent matching nodeset of the matching
 * XPath.
 * 
 * If multiple XPaths are matched, handleNodes will be called for each match.
 */
public interface IStormFrontTagHandler {
	
	public String[] getTagNames();
	
	public String getCurrentTag();
	public void setCurrentTag(String tagName);
	
	/**
	 * Handle the start tag 
	 * @param atts
	 */
	public void handleStart(Map<String,String> attributes);
	
	/**
	 * handle the end tag
	 * @param
	 */
	public void handleEnd();
	
	/**
	 * handle contents of the tag
	 * @param ch, start, length
	 */
	public boolean handleCharacters(char[] ch, int start, int length);
}
