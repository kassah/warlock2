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
	private Collection<StyleType> styleTypes;
	private WarlockColor FGColor;
	private WarlockColor BGColor;
	private boolean fullLine;
	private String name;
	
	public WarlockStyle (StyleType[] styleTypes, URL linkAddress)
	{
		this.linkAddress = linkAddress;
		this.styleTypes = new ArrayList<StyleType>();
		this.styleTypes.addAll(Arrays.asList(styleTypes));
	}
	
	public WarlockStyle (StyleType[] styleTypes) {
		this(styleTypes, null);
	}
	
	public WarlockStyle () {
		this(new StyleType[] { });
	}
	
	public WarlockStyle (IWarlockStyle other)
	{
		try {
			this.linkAddress = other.getLinkAddress() == null ? null : new URL(other.getLinkAddress().toExternalForm());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.styleTypes  = new ArrayList<StyleType>();
		if (other.getStyleTypes() != null) styleTypes.addAll(other.getStyleTypes());
	}
	
	public static WarlockStyle createBoldStyle ()
	{
		return new WarlockStyle(new StyleType[] { StyleType.BOLD }, null);
	}
	
	public URL getLinkAddress() {
		return linkAddress;
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
	
	public boolean isFullLine() {
		return fullLine;
	}
	
	public void addStyleType (StyleType styleType)
	{
		styleTypes.add(styleType);
	}

	public void setLinkAddress(URL linkAddress) {
		this.linkAddress = linkAddress;
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
	
	public void setFullLine(boolean fullLine) {
		this.fullLine = fullLine;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
