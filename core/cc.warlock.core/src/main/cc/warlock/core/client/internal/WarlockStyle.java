/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
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
