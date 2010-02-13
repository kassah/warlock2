package cc.warlock.core.client;

import java.util.regex.Pattern;

public interface IWarlockPattern {
	public Pattern getPattern();
	public String getText();
	public boolean isCaseInsensitive();
	public boolean isLiteral();
	public boolean isWholeWord();
}
