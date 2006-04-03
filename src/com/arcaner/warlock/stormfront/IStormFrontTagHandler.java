/*
 * Created on Jan 15, 2005
 */
package com.arcaner.warlock.stormfront;

import org.xml.sax.Attributes;

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
	
	public String getName();
	
	/**
	 * Handle the start tag 
	 * @param atts
	 */
	public void handleStart(Attributes atts);
	
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
