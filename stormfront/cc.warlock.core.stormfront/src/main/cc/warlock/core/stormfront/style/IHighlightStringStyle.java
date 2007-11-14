package cc.warlock.core.stormfront.style;

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.stormfront.serversettings.server.HighlightPreset;

public interface IHighlightStringStyle extends IWarlockStyle {

	public HighlightPreset getHighlightString();
}
