package cc.warlock.core.client.internal;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockColor;


public class WarlockStyle implements IWarlockStyle {

	private URL linkAddress;
	private String styleName;
	private Collection<StyleType> styleTypes;
	private WarlockColor FGColor;
	private WarlockColor BGColor;
	
	public static final WarlockStyle EMPTY_STYLE = new WarlockStyle(new StyleType[] { StyleType.EMPTY }, "empty", null);
	
	public WarlockStyle (StyleType[] styleTypes, String styleName, URL linkAddress)
	{
		this.linkAddress = linkAddress;
		this.styleName = styleName;
		this.styleTypes = new ArrayList<StyleType>();
		this.styleTypes.addAll(Arrays.asList(styleTypes));
	}
	
	public WarlockStyle (IWarlockStyle other)
	{
		try {
			this.linkAddress = other.getLinkAddress() == null ? null : new URL(other.getLinkAddress().toExternalForm());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.styleName = other.getStyleName() == null ? null : new String(other.getStyleName());
		this.styleTypes  = new ArrayList<StyleType>();
		if (other.getStyleTypes() != null) styleTypes.addAll(other.getStyleTypes());
	}
	
	public static WarlockStyle createCustomStyle (String styleName)
	{
		return new WarlockStyle(new StyleType[] { StyleType.CUSTOM }, styleName, null);
	}
	
	public static WarlockStyle createBoldStyle ()
	{
		return new WarlockStyle(new StyleType[] { StyleType.BOLD }, "bold", null);
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
	
	public WarlockColor getFGColor() {
		return FGColor;
	}
	
	public WarlockColor getBGColor() {
		return BGColor;
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
	
	public void inheritFrom(IWarlockStyle style) {
		// Right now this just deals with inheriting monospace, eventually we should figure out a way to inherit other properties as well
		if (style.getStyleTypes().contains(StyleType.MONOSPACE)
			&& !styleTypes.contains(StyleType.MONOSPACE))
		{
			styleTypes.add(StyleType.MONOSPACE);
		}
	}
	
	public void setFGColor(WarlockColor color) {
		FGColor = color;
	}
	
	public void setBGColor(WarlockColor color) {
		BGColor = color;
	}
}
