package com.arcaner.warlock.client.stormfront.internal;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import com.arcaner.warlock.client.stormfront.IStormFrontStyle;

public class StormFrontStyle implements IStormFrontStyle {

	private URL linkAddress;
	private String styleName;
	private Collection<StyleType> styleTypes;
	
	public static final StormFrontStyle EMPTY_STYLE = new StormFrontStyle(new StyleType[] { StyleType.EMPTY }, "empty", null);
	public static final StormFrontStyle BOLD_STYLE = new StormFrontStyle(new StyleType[] { StyleType.BOLD }, "bold", null);
	
	public StormFrontStyle (StyleType[] styleTypes, String styleName, URL linkAddress)
	{
		this.linkAddress = linkAddress;
		this.styleName = styleName;
		this.styleTypes = Arrays.asList(styleTypes);
	}
	
	public static StormFrontStyle createCustomStyle (String styleName)
	{
		return new StormFrontStyle(new StyleType[] { StyleType.CUSTOM }, styleName, null);
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
