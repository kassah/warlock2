package com.arcaner.warlock.rcp.ui.style;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

import com.arcaner.warlock.client.IWarlockStyle;
import com.arcaner.warlock.client.stormfront.WarlockColor;
import com.arcaner.warlock.configuration.ServerSettings;
import com.arcaner.warlock.rcp.ui.StyleRangeWithData;

public class StyleMappings {

	public static final String FILL_ENTIRE_LINE = "fillEntireLine";
	
	public static StyleRangeWithData getStyle (ServerSettings settings, IWarlockStyle style, int start, int length)
	{
		if (style.getStyleTypes().contains(IWarlockStyle.StyleType.EMPTY))
			return null;
		
		Display display = Display.getDefault();
		StyleRangeWithData range = new StyleRangeWithData();
		range.start = start;
		range.length = length;
		range.fontStyle = 0;
		
		for (IWarlockStyle.StyleType styleType : style.getStyleTypes())
		{
			if (styleType.equals(IWarlockStyle.StyleType.BOLD))
				range.fontStyle |= SWT.BOLD;
			else if (styleType.equals(IWarlockStyle.StyleType.ITALIC))
				range.fontStyle |= SWT.ITALIC;
			else if (styleType.equals(IWarlockStyle.StyleType.UNDERLINE))
				range.underline = true;
			else if (styleType.equals(IWarlockStyle.StyleType.MONOSPACE))
			{
				String monoFontFace = settings.getStringSetting(ServerSettings.StringType.MainWindow_MonoFontFace);
				int monoFontSize = settings.getIntSetting(ServerSettings.IntType.MainWindow_MonoFontSize);
				if (monoFontFace != null)
				{
					if (JFaceResources.getFontRegistry().hasValueFor(monoFontFace))
					{
						range.font = new Font(display, monoFontFace, monoFontSize, SWT.NONE);
						break;
					}
					
				}
				range.font = JFaceResources.getTextFont();
				range.font.getFontData()[0].setHeight(monoFontSize);
			}
		}
		
//		Style systemStyle = SavedStyles.getStyleFromName(style.getStyleName());
//		if (systemStyle != null)
//		{
//			if (systemStyle.getForeground() != null)
//				range.foreground = systemStyle.getForeground();
//			if (systemStyle.getBackground() != null)
//				range.background = systemStyle.getBackground();
//			if (systemStyle.getFontName() != null)
//				range.font = JFaceResources.getFont(systemStyle.getFontName());
//		}
		
		WarlockColor foreground = settings.getPresetForegroundColor(style.getStyleName());
		WarlockColor background = settings.getPresetBackgroundColor(style.getStyleName());
		
		if (foreground != WarlockColor.DEFAULT_COLOR)
			range.foreground = new Color(display, foreground.getRed(), foreground.getGreen(), foreground.getBlue());
		if (background != WarlockColor.DEFAULT_COLOR)
			range.background = new Color(display, background.getRed(), background.getGreen(), background.getBlue());
		
		if (settings.getPresetFillEntireLine(style.getStyleName()))
		{
			range.data.put(FILL_ENTIRE_LINE, "true");
		}
		
		return range;
	}
	
	public static StyleRange getEchoStyle (int start, int length)
	{
		Display display = Display.getDefault();
		
		StyleRange range = new StyleRange();
		range.background = new Color(display, 0x30, 0x30, 0x30);
		range.foreground = new Color(display, 255, 255, 255);
		range.fontStyle = SWT.BOLD;
		range.start = start;
		range.length = length;
		
		return range;
	}
}
