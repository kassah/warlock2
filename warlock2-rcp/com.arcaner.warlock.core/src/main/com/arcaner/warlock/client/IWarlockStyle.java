package com.arcaner.warlock.client;

import java.net.URL;
import java.util.Collection;

/**
 * This interface represents a "style" that applies to a string of text. 
 * @author Marshall
 *
 */
public interface IWarlockStyle {

	public enum StyleType {
		BOLD, ITALIC, UNDERLINE, LINK, MONOSPACE, EMPTY, CUSTOM
	};
	
	public String getStyleName();
	
	public Collection<StyleType> getStyleTypes();
	
	public URL getLinkAddress();
	
	public void addStyleType (StyleType styleType);

	public void setLinkAddress(URL linkAddress);

	public void setStyleName(String styleName);
	
	public int getStart();
	public void setStart (int start);
	
	public int getLength();
	public void setLength(int length);
	
	public void inheritFrom(IWarlockStyle style);
}
