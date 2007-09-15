package cc.warlock.client.internal;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import cc.warlock.client.IWarlockStyle;


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
	
	public WarlockStyle (IWarlockStyle other)
	{
		try {
			this.linkAddress = other.getLinkAddress() == null ? null : new URL(other.getLinkAddress().toExternalForm());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.styleName = other.getStyleName();
		this.styleTypes  = new ArrayList<StyleType>();
		if (other.getStyleTypes() != null) styleTypes.addAll(other.getStyleTypes());
		this.start = other.getStart();
		this.length = other.getLength();
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
	
	public void inheritFrom(IWarlockStyle style) {
		// Right now this just deals with inheriting monospace, eventually we should figure out a way to inherit other properties as well
		if (style.getStyleTypes().contains(StyleType.MONOSPACE)
			&& !styleTypes.contains(StyleType.MONOSPACE))
		{
			styleTypes.add(StyleType.MONOSPACE);
		}
	}
}
