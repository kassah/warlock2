package cc.warlock.core.client.internal;

import cc.warlock.core.client.IWarlockHighlight;
import cc.warlock.core.client.IWarlockStyle;

public class WarlockHighlight extends WarlockPattern implements IWarlockHighlight {

	private IWarlockStyle style;
	
	public WarlockHighlight(String text, boolean literal, boolean wholeWord,
			boolean caseInsensitive, IWarlockStyle style) {
		super(text, literal, wholeWord, caseInsensitive);
		this.style = style;
	}

	public IWarlockStyle getStyle() {
		return style;
	}

	public void setStyle(IWarlockStyle style) {
		this.pattern = null;
		this.style = style;
	}
}
