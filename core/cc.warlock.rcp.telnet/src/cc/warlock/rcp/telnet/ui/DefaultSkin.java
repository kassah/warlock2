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
package cc.warlock.rcp.telnet.ui;

import cc.warlock.core.client.IWarlockSkin;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.settings.IHighlightString;

/**
 * @author Will Robertson
 *
 */
public class DefaultSkin implements IWarlockSkin {

	/* (non-Javadoc)
	 * @see cc.warlock.core.client.IWarlockSkin#getBackgroundColor(cc.warlock.core.client.settings.IHighlightString)
	 */
	public WarlockColor getBackgroundColor(IHighlightString string) {
		return new WarlockColor("#191932");
	}

	/* (non-Javadoc)
	 * @see cc.warlock.core.client.IWarlockSkin#getColor(cc.warlock.core.client.IWarlockSkin.ColorType)
	 */
	public WarlockColor getColor(ColorType type) {
		// TODO Auto-generated method stub
		if (type == ColorType.MainWindow_Background)
			return new WarlockColor("#191932");
		else if (type == ColorType.CommandLine_Background)
			return new WarlockColor("#191932");
		else
			return new WarlockColor("#F0F0FF");
	}

	/* (non-Javadoc)
	 * @see cc.warlock.core.client.IWarlockSkin#getDefaultBackgroundColor(java.lang.String)
	 */
	public WarlockColor getDefaultBackgroundColor(String styleName) {
		// TODO Auto-generated method stub
		return new WarlockColor("#191932");
	}

	/* (non-Javadoc)
	 * @see cc.warlock.core.client.IWarlockSkin#getDefaultForegroundColor(java.lang.String)
	 */
	public WarlockColor getDefaultForegroundColor(String styleName) {
		// TODO Auto-generated method stub
		return new WarlockColor("#F0F0FF");
	}

	/* (non-Javadoc)
	 * @see cc.warlock.core.client.IWarlockSkin#getDefaultWindowBackground()
	 */
	public WarlockColor getDefaultWindowBackground() {
		// TODO Auto-generated method stub
		return new WarlockColor("#191932");
	}

	/* (non-Javadoc)
	 * @see cc.warlock.core.client.IWarlockSkin#getDefaultWindowForeground()
	 */
	public WarlockColor getDefaultWindowForeground() {
		// TODO Auto-generated method stub
		return new WarlockColor("#F0F0FF");
	}

	/* (non-Javadoc)
	 * @see cc.warlock.core.client.IWarlockSkin#getFontFace(cc.warlock.core.client.IWarlockSkin.FontFaceType)
	 */
	public String getFontFace(FontFaceType type) {
		if (System.getProperties().getProperty("os.name").indexOf("Windows") != -1)
		{
			return "Verdana";
		}
		return "Sans";
	}

	/* (non-Javadoc)
	 * @see cc.warlock.core.client.IWarlockSkin#getFontSize(cc.warlock.core.client.IWarlockSkin.FontSizeType)
	 */
	public int getFontSize(FontSizeType type) {
		// TODO Auto-generated method stub
		return 12;
	}

	/* (non-Javadoc)
	 * @see cc.warlock.core.client.IWarlockSkin#getForegroundColor(cc.warlock.core.client.settings.IHighlightString)
	 */
	public WarlockColor getForegroundColor(IHighlightString string) {
		// TODO Auto-generated method stub
		return new WarlockColor("#F0F0FF");
	}

	/* (non-Javadoc)
	 * @see cc.warlock.core.client.IWarlockSkin#getMainBackground()
	 */
	public WarlockColor getMainBackground() {
		// TODO Auto-generated method stub
		return new WarlockColor("#191932");
	}

	/* (non-Javadoc)
	 * @see cc.warlock.core.client.IWarlockSkin#getMainForeground()
	 */
	public WarlockColor getMainForeground() {
		// TODO Auto-generated method stub
		return new WarlockColor("#F0F0FF");
	}

}
