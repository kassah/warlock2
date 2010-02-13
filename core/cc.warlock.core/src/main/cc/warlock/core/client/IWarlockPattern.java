package cc.warlock.core.client;

import java.util.regex.Pattern;

public interface IWarlockPattern {
	public Pattern getPattern();
	
	public String getText();
	public boolean isCaseInsensitive();
	public boolean isLiteral();
	public boolean isWholeWord();
	
	public void setText(String text);
	public void setCaseInsensitive(boolean caseInsensitive);
	public void setWholeWord(boolean wholeWord);
	public void setListeral(boolean literal);
}
