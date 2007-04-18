package com.arcaner.warlock.client.internal;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.arcaner.warlock.client.IWarlockStyle;

public class WarlockStyle implements IWarlockStyle {

	private URL linkAddress;
	private String styleName;
	private Collection<StyleType> styleTypes;
	private int start, length;
	
	public static final WarlockStyle EMPTY_STYLE = new WarlockStyle(new StyleType[] { StyleType.EMPTY }, "empty", null, -1, -1);
	
	public WarlockStyle (StyleType[] styleTypes, String styleName, URL linkAddress, int start, int length)
	{
		this.linkAddress = linkAddress;
		this.styleName = styleName;
		this.styleTypes = new ArrayList<StyleType>();
		this.styleTypes.addAll(Arrays.asList(styleTypes));
		this.start = start;
		this.length = length;
	}
	
	public static WarlockStyle createCustomStyle (String styleName, int start, int length)
	{
		return new WarlockStyle(new StyleType[] { StyleType.CUSTOM }, styleName, null, start, length);
	}
	
	public static WarlockStyle createBoldStyle (int start, int length)
	{
		return new WarlockStyle(new StyleType[] { StyleType.BOLD }, "bold", null, start, length);
	}
	
	public URL getLinkAddress() {
		return linkAddress;
	}

	public String getStyleName() {
		return styleName;
	}

	public Collection<StyleType> getStyleTypes() {
		return styleTypes;
	}
	
	public void addStyleType (StyleType styleType)
	{
		styleTypes.add(styleType);
	}

	public void setLinkAddress(URL linkAddress) {
		this.linkAddress = linkAddress;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	public int getLength() {
		return length;
	}
	
	public int getStart() {
		return start;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public void setStart(int start) {
		this.start = start;
	}
}
