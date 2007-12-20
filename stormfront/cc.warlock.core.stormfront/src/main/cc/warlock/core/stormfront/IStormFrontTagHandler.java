/*
 * Created on Jan 15, 2005
 */
package cc.warlock.core.stormfront;

import java.util.Map;

import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


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
	
	public Map<String, IStormFrontTagHandler> getTagHandlers();
	
	/**
	 * Handle the start tag 
	 * @param atts
	 */
	public void handleStart(StormFrontAttributeList attributes);
	
	/**
	 * handle the end tag
	 * @param
	 */
	public void handleEnd();
	
	/**
	 * handle contents of the tag
	 * @param characters
	 */
	public boolean handleCharacters(String characters);
	
	public boolean ignoreNewlines();
}
