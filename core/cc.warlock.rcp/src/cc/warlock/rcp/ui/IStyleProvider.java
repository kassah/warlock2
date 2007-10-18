package cc.warlock.rcp.ui;

import org.eclipse.swt.custom.StyleRange;

import cc.warlock.core.client.IWarlockStyle;

public interface IStyleProvider {

	public static final String FILL_ENTIRE_LINE = "fillEntireLine";

	public StyleRangeWithData getStyleRange (IWarlockStyle style, int start);
	
	public StyleRangeWithData getEchoStyle (int start);
}
