package cc.warlock.rcp.ui;

import org.eclipse.swt.custom.StyleRange;

import cc.warlock.core.client.IWarlockStyle;

public interface IStyleProvider {

	public static final String FILL_ENTIRE_LINE = "fillEntireLine";

	public StyleRangeWithData getStyleRange (IWarlockStyle style, int start, int length);
	
	public StyleRangeWithData getEchoStyle (int start, int length);
	
	public void applyStyles (WarlockText styledText, StyleRange parentStyle, String text, int start, int lineIndex);
}
