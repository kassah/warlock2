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
	
	@Override
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

	@Override
	public String getText() {
		return text;
	}

	@Override
	public boolean isCaseInsensitive() {
		return caseInsensitive;
	}

	@Override
	public boolean isWholeWord() {
		return wholeWord;
	}

	@Override
	public boolean isLiteral() {
		return literal;
	}

}
