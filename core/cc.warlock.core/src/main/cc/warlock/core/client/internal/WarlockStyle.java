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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.WarlockFont;


public class WarlockStyle implements IWarlockStyle {

	private Collection<StyleType> styleTypes = new ArrayList<StyleType>();
	private WarlockColor foregroundColor = new WarlockColor(WarlockColor.DEFAULT_COLOR);
	private WarlockColor backgroundColor = new WarlockColor(WarlockColor.DEFAULT_COLOR);
	private WarlockFont font;
	private boolean fullLine;
	private String name;
	private Runnable action;
	private String sound = new String();
	
	public WarlockStyle (StyleType[] styleTypes)
	{
		this.styleTypes.addAll(Arrays.asList(styleTypes));
	}
	
	public WarlockStyle(String name) {
		this.name = name;
	}
	
	public WarlockStyle () {
	}
	
	public WarlockStyle (IWarlockStyle other)
	{
		
		this.backgroundColor = new WarlockColor(other.getBackgroundColor());
		this.foregroundColor = new WarlockColor(other.getForegroundColor());
		if(other.getFont() != null)
			this.font = new WarlockFont(other.getFont());
		this.name = other.getName() == null ? null : new String(other.getName());
		this.action = other.getAction();
		
		if (other.getStyleTypes() != null)
			styleTypes.addAll(other.getStyleTypes());
		
		this.fullLine = other.isFullLine();
		this.setSound(other.getSound());
	}
	
	public Runnable getAction() {
		return action;
	}
	
	public void setAction(Runnable action) {
		this.action = action;
	}
	
	public Collection<StyleType> getStyleTypes() {
		return styleTypes;
	}
	
	public boolean isFullLine() {
		return fullLine;
	}
	
	public void addStyleType (StyleType styleType)
	{
		styleTypes.add(styleType);
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

	public WarlockColor getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(WarlockColor foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	public WarlockColor getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(WarlockColor backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public WarlockFont getFont() {
		return font;
	}
	
	public void setFont(WarlockFont font) {
		this.font = font;
	}
	
	public String getSound(){
		return sound;
	}
	
	public void setSound(String sound){
		this.sound = sound;
	}
	
}
