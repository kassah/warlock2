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
/**
 * 
 */
package cc.warlock.rcp.ui;

import java.util.HashMap;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;

/**
 * A custom style range extension that allows for arbitrary style hints/properties
 * 
 * @author marshall
 */
public class StyleRangeWithData extends StyleRange
{
	public HashMap<String, String> data = new HashMap<String, String>();
	public String tooltip;
	public Runnable action;
	
	public StyleRangeWithData() {
		super();
	}
	
	public StyleRangeWithData(int start, int length, Color foreground, Color background, int fontStyle) {
		new StyleRange(start, length, foreground, background, fontStyle);
	}
	
	@Override
	public boolean isUnstyled() {
		if(data != null) return false;
		if(tooltip != null) return false;
		if(action != null) return false;
		return super.isUnstyled();
	}
	
	@Override
	public boolean equals(Object object) {
		if(object == this) return true;
		StyleRangeWithData style;
		if(object instanceof StyleRangeWithData) style = (StyleRangeWithData)object;
		else return false;
		if(this.start != style.start) return false;
		if(this.length != style.length) return false;
		return similarTo(style);
	}
	
	@Override
	public int hashCode() {
		int code = super.hashCode();
		if(data.size() > 0)
			code += data.hashCode();
		if(tooltip != null)
			code += tooltip.hashCode();
		if(action != null)
			code += action.hashCode();
		return code;
	}
	
	@Override
	public boolean similarTo(StyleRange style) {
		StyleRangeWithData s;
		if(style instanceof StyleRangeWithData) s = (StyleRangeWithData)style;
		else return data.size() == 0 && tooltip == null && action == null && super.similarTo(style);
		if (this.data != null) {
			if (!this.data.equals(s.data)) return false;
		} else if (s.data != null) return false;
		if (this.tooltip != null) {
			if (!this.tooltip.equals(s.tooltip)) return false;
		} else if (s.tooltip != null) return false;
		if (this.action != null) {
			if (!this.action.equals(s.action)) return false;
		} else if (s.action != null) return false;
		return super.similarTo(style);
	}
	
	@Override
	public Object clone() {
		StyleRangeWithData style = (StyleRangeWithData)super.clone();
		style.data = this.data;
		style.tooltip = this.tooltip;
		style.action = this.action;
		return style;
	}
}