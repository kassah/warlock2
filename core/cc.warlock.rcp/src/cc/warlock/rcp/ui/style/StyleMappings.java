package cc.warlock.rcp.ui.style;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

import cc.warlock.client.IWarlockStyle;
import cc.warlock.client.internal.WarlockStyle;
import cc.warlock.client.stormfront.WarlockColor;
import cc.warlock.configuration.server.Preset;
import cc.warlock.configuration.server.ServerSettings;
import cc.warlock.configuration.skin.IWarlockSkin;
import cc.warlock.rcp.ui.StyleRangeWithData;


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
			else if (styleType.equals(IWarlockStyle.StyleType.UNDERLINE) || styleType.equals(IWarlockStyle.StyleType.LINK))
				range.underline = true;
			else if (styleType.equals(IWarlockStyle.StyleType.MONOSPACE))
			{
				String monoFontFace = settings.getFontFaceSetting(IWarlockSkin.FontFaceType.MainWindow_MonoFontFace);
				int monoFontSize = settings.getFontSizeSetting(IWarlockSkin.FontSizeType.MainWindow_MonoFontSize);
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
		
		Preset stylePreset = settings.getPreset(style.getStyleName());
		WarlockColor foreground = stylePreset == null ? WarlockColor.DEFAULT_COLOR : stylePreset.getForegroundColor();
		WarlockColor background = stylePreset == null ? WarlockColor.DEFAULT_COLOR : stylePreset.getBackgroundColor();
		
		if (foreground != WarlockColor.DEFAULT_COLOR)
			range.foreground = new Color(display, foreground.getRed(), foreground.getGreen(), foreground.getBlue());
		if (background != WarlockColor.DEFAULT_COLOR)
			range.background = new Color(display, background.getRed(), background.getGreen(), background.getBlue());
		
		if (stylePreset != null && stylePreset.isFillEntireLine())
		{
			range.data.put(FILL_ENTIRE_LINE, "true");
		}
		
		return range;
	}
	
	public static StyleRange getEchoStyle (ServerSettings settings, int start, int length)
	{
		return getStyle(settings, WarlockStyle.createCustomStyle("command", start, length), start, length);
		
//		Display display = Display.getDefault();
//		
//		StyleRange range = new StyleRange();
//		range.background = new Color(display, 0x30, 0x30, 0x30);
//		range.foreground = new Color(display, 255, 255, 255);
//		range.fontStyle = SWT.BOLD;
//		range.start = start;
//		range.length = length;
//		
//		return range;
	}
}