package cc.warlock.rcp.stormfront.ui.style;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

import cc.warlock.core.client.IWarlockSkin;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.stormfront.serversettings.server.HighlightString;
import cc.warlock.core.stormfront.serversettings.server.Preset;
import cc.warlock.core.stormfront.serversettings.server.ServerSettings;
import cc.warlock.rcp.ui.StyleRangeWithData;
import cc.warlock.rcp.ui.WarlockText;
import cc.warlock.rcp.ui.style.DefaultStyleProvider;
import cc.warlock.rcp.util.ColorUtil;

public class StormFrontStyleProvider extends DefaultStyleProvider {

	protected ServerSettings settings;
	
	public StormFrontStyleProvider (ServerSettings settings)
	{
		this.settings = settings;
	}
	
	public StyleRangeWithData getStyleRange (IWarlockStyle style, int start, int length)
	{
		Display display = Display.getDefault();
		StyleRangeWithData range = super.getStyleRange(style, start, length);
		
		if (style.getStyleTypes().contains(IWarlockStyle.StyleType.MONOSPACE))
		{
			String monoFontFace = settings.getFontFaceSetting(IWarlockSkin.FontFaceType.MainWindow_MonoFontFace);
			int monoFontSize = settings.getFontSizeSetting(IWarlockSkin.FontSizeType.MainWindow_MonoFontSize);
			if (monoFontFace != null)
			{
				if (JFaceResources.getFontRegistry().hasValueFor(monoFontFace))
				{
					range.font = new Font(display, monoFontFace, monoFontSize, SWT.NONE);
				}
			}
		}
		
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
	
	public void applyStyles (WarlockText styledText, StyleRange parentStyle, String text, int start, int lineIndex)
	{
		if (settings.getHighlightStrings() == null)
			return;
		
		Font font = styledText.getFont();
		if (parentStyle != null)
		{
			if (parentStyle.font != null)
				font = parentStyle.font;
		}
		
		for (HighlightString highlight : settings.getHighlightStrings())
		{
			int highlightLength = highlight.isFillEntireLine() ? text.length() : highlight.getText().length();
			int index = text.indexOf(highlight.getText());
			while (index > -1)
			{
				StyleRangeWithData range = new StyleRangeWithData();
				range.background = ColorUtil.warlockColorToColor(highlight.getBackgroundColor());
				range.foreground = ColorUtil.warlockColorToColor(highlight.getForegroundColor());
				range.start = highlight.isFillEntireLine() ? start : start + index;
				range.length = highlightLength;
				range.font = font;
				
				if (highlight.isFillEntireLine())
				{
					styledText.setLineBackground(lineIndex, range.background);
					styledText.setLineForeground(lineIndex, range.foreground);
				}
				
				styledText.setStyleRange(range);
				
				if (highlight.isFillEntireLine())
					break;
				
				index = text.indexOf(highlight.getText(), index+1);
			}
		}
	}
}
