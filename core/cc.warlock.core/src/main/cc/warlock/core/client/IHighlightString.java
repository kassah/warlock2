package cc.warlock.core.client;

import java.util.regex.Pattern;

public interface IHighlightString {
	public String getText();
	public Pattern getPattern();
	public IWarlockStyle getStyle();
}
