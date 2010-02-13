package cc.warlock.core.client.internal;

import java.util.regex.Pattern;

import cc.warlock.core.client.IWarlockPattern;

public class WarlockPattern implements IWarlockPattern {

	private Pattern pattern = null;
	
	private String text;
	private boolean literal;
	private boolean wholeWord;
	private boolean caseInsensitive;
	
	public WarlockPattern(String text, boolean literal, boolean wholeWord,
			boolean caseInsensitive) {
		this.text = text;
		this.literal = literal;
		this.wholeWord = wholeWord;
		this.caseInsensitive = caseInsensitive;
	}
	
	public Pattern getPattern() {
		if(pattern == null && text != null) {
			String str = text;
			int flags = 0;
			if(literal)
				str = Pattern.quote(str);
			if(caseInsensitive)
				flags |= Pattern.CASE_INSENSITIVE;
			if(wholeWord)
				str = "(?=(\\W|^))" + str + "(?!(\\W|$))";
		}
		return pattern;
	}

	public String getText() {
		return text;
	}

	public boolean isCaseInsensitive() {
		return caseInsensitive;
	}

	public boolean isWholeWord() {
		return wholeWord;
	}

	public boolean isLiteral() {
		return literal;
	}

	public void setText(String text) {
		this.pattern = null;
		this.text = text;
	}
	
	public void setCaseInsensitive(boolean caseInsensitive) {
		this.pattern = null;
		this.caseInsensitive = caseInsensitive;
	}
	
	public void setWholeWord(boolean wholeWord) {
		this.pattern = null;
		this.wholeWord = wholeWord;
	}
	
	public void setListeral(boolean literal) {
		this.pattern = null;
		this.literal = literal;
	}
}
