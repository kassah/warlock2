package cc.warlock.core.client;

import java.util.Collection;

public interface IHighlightProvider {
	public Collection<? extends IHighlightString> getHighlightStrings();
}
