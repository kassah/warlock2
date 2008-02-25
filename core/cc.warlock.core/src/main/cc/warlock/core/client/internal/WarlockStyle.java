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
	private WarlockColor foregroundColor = WarlockColor.DEFAULT_COLOR;
	private WarlockColor backgroundColor = WarlockColor.DEFAULT_COLOR;
	private boolean fullLine;
	private String name;
	private String command;
	private boolean commandVisible;
	private IWarlockStyle originalStyle;
	private boolean needsUpdate;
	
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
		
		this.backgroundColor = new WarlockColor(other.getBackgroundColor());
		this.foregroundColor = new WarlockColor(other.getForegroundColor());
		this.name = other.getName() == null ? null : new String(other.getName());
		this.command = other.getCommand() == null ? null : new String(other.getCommand());
		
		this.styleTypes  = new ArrayList<StyleType>();
		if (other.getStyleTypes() != null) styleTypes.addAll(other.getStyleTypes());
		
		this.originalStyle = other;
	}
	
	public static WarlockStyle createBoldStyle ()
	{
		return new WarlockStyle(new StyleType[] { StyleType.BOLD }, null);
	}
	
	public URL getLinkAddress() {
		return linkAddress;
	}
	
	public String getCommand() {
		return command;
	}
	
	public boolean commandVisible() {
		return commandVisible;
	}

	public Collection<StyleType> getStyleTypes() {
		return styleTypes;
	}
	
	public boolean isFullLine() {
		return fullLine;
	}
	
	public void addStyleType (StyleType styleType)
	{
		needsUpdate = true;
		
		styleTypes.add(styleType);
	}

	public void setLinkAddress(URL linkAddress) {
		if (!linkAddress.equals(this.linkAddress))
			needsUpdate = true;
		
		this.linkAddress = linkAddress;
	}
	
	public void setCommand(String command, boolean visible) {
		if (!command.equals(this.command) || visible != this.commandVisible)
			needsUpdate = true;
		
		this.command = command;
		this.commandVisible = visible;
	}
	
	public void inheritFrom(IWarlockStyle style) {
		// Right now this just deals with inheriting monospace, eventually we should figure out a way to inherit other properties as well
		if (style.getStyleTypes().contains(StyleType.MONOSPACE)
			&& !styleTypes.contains(StyleType.MONOSPACE))
		{
			needsUpdate = true;
			
			styleTypes.add(StyleType.MONOSPACE);
		}
	}
	
	public void setFullLine(boolean fullLine) {
		if (fullLine != this.fullLine)
			needsUpdate = true;
		
		this.fullLine = fullLine;
	}
	
	public void setName(String name) {
		if (!name.equals(this.name))
			needsUpdate = true;
		
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public WarlockColor getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(WarlockColor foregroundColor) {
		if (!foregroundColor.equals(this.foregroundColor))
			needsUpdate = true;
		
		this.foregroundColor = foregroundColor;
	}

	public WarlockColor getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(WarlockColor backgroundColor) {
		if (!backgroundColor.equals(this.backgroundColor))
			needsUpdate = true;
		
		this.backgroundColor = backgroundColor;
	}
	
	public boolean needsUpdate ()
	{
		return needsUpdate;
	}
	
	public IWarlockStyle getOriginalStyle ()
	{
		return originalStyle;
	}
}
