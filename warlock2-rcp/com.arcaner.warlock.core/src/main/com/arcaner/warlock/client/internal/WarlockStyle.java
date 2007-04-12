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
	
	public static final WarlockStyle EMPTY_STYLE = new WarlockStyle(new StyleType[] { StyleType.EMPTY }, "empty", null);
	public static final WarlockStyle BOLD_STYLE = new WarlockStyle(new StyleType[] { StyleType.BOLD }, "bold", null);
	
	public WarlockStyle (StyleType[] styleTypes, String styleName, URL linkAddress)
	{
		this.linkAddress = linkAddress;
		this.styleName = styleName;
		this.styleTypes = new ArrayList<StyleType>();
		this.styleTypes.addAll(Arrays.asList(styleTypes));
	}
	
	public static WarlockStyle createCustomStyle (String styleName)
	{
		return new WarlockStyle(new StyleType[] { StyleType.CUSTOM }, styleName, null);
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

}
