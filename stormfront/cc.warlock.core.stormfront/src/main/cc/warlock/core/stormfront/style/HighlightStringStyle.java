package cc.warlock.core.stormfront.style;

import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.stormfront.serversettings.server.HighlightString;

public class HighlightStringStyle extends WarlockStyle implements
		IHighlightStringStyle {
	
	protected HighlightString string;
	
	public HighlightStringStyle (HighlightString string)
	{
		super(new StyleType[] { StyleType.CUSTOM }, "custom", null);
		
		this.string = string;
	}

	public HighlightString getHighlightString() {
		return string;
	}
}
