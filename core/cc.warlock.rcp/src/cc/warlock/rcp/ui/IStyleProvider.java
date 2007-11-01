package cc.warlock.rcp.ui;

import org.eclipse.swt.custom.StyleRange;

import cc.warlock.core.client.IWarlockStyle;

public interface IStyleProvider {
	public StyleRange getStyleRange (IWarlockStyle style);
}
