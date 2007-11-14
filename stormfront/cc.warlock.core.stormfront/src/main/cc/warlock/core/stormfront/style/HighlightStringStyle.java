package cc.warlock.core.stormfront.style;

import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.stormfront.serversettings.server.HighlightPreset;

public class HighlightStringStyle extends WarlockStyle implements
		IHighlightStringStyle {
	
	protected HighlightPreset string;
	
	public HighlightStringStyle (HighlightPreset string)
	{
		super();
		
		this.string = string;
	}

	public HighlightPreset getHighlightString() {
		return string;
	}
	
	@Override
	public boolean isFullLine() {
		return string.isFillEntireLine();
	}
}
