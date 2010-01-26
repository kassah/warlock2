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
package cc.warlock.rcp.stormfront.ui.style;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.WarlockFont;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.settings.IStormFrontClientSettings;
import cc.warlock.rcp.ui.StyleRangeWithData;
import cc.warlock.rcp.ui.style.DefaultStyleProvider;
import cc.warlock.rcp.util.ColorUtil;

public class StormFrontStyleProvider extends DefaultStyleProvider {

	protected IStormFrontClientSettings settings;
	
	public StormFrontStyleProvider (IStormFrontClientSettings settings)
	{
		super(settings.getClient());
		this.settings = settings;
	}
	
	public StyleRangeWithData getStyleRange (IWarlockStyle style)
	{
		Display display = Display.getDefault();
		StyleRangeWithData range = super.getStyleRange(style);
		
		IStormFrontClient sfClient = (IStormFrontClient)client;
		
		if (style.getName() != null)
		{
			if (style.getBackgroundColor().isDefault()) {
				WarlockColor color = sfClient.getStormFrontSkin().getDefaultBackgroundColor(style.getName());
				if (!color.isDefault())
					range.background = ColorUtil.warlockColorToColor(color);
			}
			if (style.getForegroundColor().isDefault()) {
				WarlockColor color = sfClient.getStormFrontSkin().getDefaultForegroundColor(style.getName());
				if (!color.isDefault())
					range.foreground = ColorUtil.warlockColorToColor(color);
			}
		}
		
		if (style.getStyleTypes().contains(IWarlockStyle.StyleType.MONOSPACE))
		{
			WarlockFont columnFont = settings.getMainWindowSettings().getColumnFont();
//			if (Platform.getOS().equals(Platform.OS_MACOSX)) {
//				monoFontSize = settings.getMainWindowSettings().getColumnFontSizeInPixels();
//			}
			if (columnFont != null)
			{
				String monoFontFace = columnFont.getFamilyName();
				int monoFontSize = columnFont.getSize();
				
				if (!columnFont.isDefaultFont() && JFaceResources.getFontRegistry().hasValueFor(monoFontFace))
				{
					range.font = new Font(display, monoFontFace, monoFontSize, SWT.NONE);
				}
			}
		}
		
		return range;
	}
	
//	public void applyStyles (WarlockText styledText, StyleRange parentStyle, String text, int start, int lineIndex)
//	{
//		if (settings.getHighlightStrings() == null)
//			return;
//		
//		Font font = styledText.getFont();
//		if (parentStyle != null)
//		{
//			if (parentStyle.font != null)
//				font = parentStyle.font;
//		}
//		
//		for (HighlightString highlight : settings.getHighlightStrings())
//		{
//			int highlightLength = highlight.isFillEntireLine() ? text.length() : highlight.getText().length();
//			int index = text.indexOf(highlight.getText());
//			while (index > -1)
//			{
//				StyleRangeWithData range = new StyleRangeWithData();
//				range.background = ColorUtil.warlockColorToColor(highlight.getBackgroundColor());
//				range.foreground = ColorUtil.warlockColorToColor(highlight.getForegroundColor());
//				range.start = highlight.isFillEntireLine() ? start : start + index;
//				range.length = highlightLength;
//				range.font = font;
//				
//				if (highlight.isFillEntireLine())
//				{
//					styledText.setLineBackground(lineIndex, range.background);
//					styledText.setLineForeground(lineIndex, range.foreground);
//				}
//				
//				styledText.setStyleRange(range);
//				
//				if (highlight.isFillEntireLine())
//					break;
//				
//				index = text.indexOf(highlight.getText(), index+1);
//			}
//		}
//	}
}
