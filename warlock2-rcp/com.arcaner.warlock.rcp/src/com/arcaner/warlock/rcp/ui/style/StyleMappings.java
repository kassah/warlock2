package com.arcaner.warlock.rcp.ui.style;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;

import com.arcaner.warlock.client.stormfront.IStormFrontStyle;

public class StyleMappings {

	public static StyleRange getStyle (IStormFrontStyle style, int start, int length)
	{
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
		}
		
		Style systemStyle = SavedStyles.getStyleFromName(style.getStyleName());
		if (systemStyle != null)
		{
			if (systemStyle.getForeground() != null)
				range.foreground = systemStyle.getForeground();
			if (systemStyle.getBackground() != null)
				range.background = systemStyle.getBackground();
			if (systemStyle.getFontName() != null)
				range.font = JFaceResources.getFont(systemStyle.getFontName());
		}
		
		return range;
	}
}
