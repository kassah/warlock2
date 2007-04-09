package com.arcaner.warlock.rcp.ui.style;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

import com.arcaner.warlock.client.stormfront.IStormFrontStyle;
import com.arcaner.warlock.client.stormfront.WarlockColor;
import com.arcaner.warlock.configuration.ServerSettings;

public class StyleMappings {

	public static StyleRange getStyle (ServerSettings settings, IStormFrontStyle style, int start, int length)
	{
		Display display = Display.getDefault();
		StyleRange range = new StyleRange();
		range.start = start;
		range.length = length;
		range.fontStyle = 0;
		
		for (IStormFrontStyle.StyleType styleType : style.getStyleTypes())
		{
			if (styleType.equals(IStormFrontStyle.StyleType.BOLD))
				range.fontStyle |= SWT.BOLD;
			else if (styleType.equals(IStormFrontStyle.StyleType.ITALIC))
				range.fontStyle |= SWT.ITALIC;
			else if (styleType.equals(IStormFrontStyle.StyleType.UNDERLINE))
				range.underline = true;
			else if (styleType.equals(IStormFrontStyle.StyleType.MONOSPACE))
			{
				String monoFontFace = settings.getStringSetting(ServerSettings.StringType.MainWindow_MonoFontFace);
				int monoFontSize = settings.getIntSetting(ServerSettings.IntType.MainWindow_MonoFontSize);
				
				range.font = monoFontFace == null ? JFaceResources.getDialogFont() : new Font(display, monoFontFace, monoFontSize-2, SWT.NONE);
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
		
		return range;
	}
}
