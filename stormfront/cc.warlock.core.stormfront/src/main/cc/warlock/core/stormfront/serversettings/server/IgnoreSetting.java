package cc.warlock.core.stormfront.serversettings.server;

import java.util.regex.Pattern;

import cc.warlock.core.stormfront.xml.StormFrontElement;

public class IgnoreSetting extends ServerSetting {

	private String text;
	private boolean matchPartialWord = false;
	private boolean ignoreCase = false;
	private Pattern regex;
	
	public IgnoreSetting (ServerSettings settings, StormFrontElement ignoreElement)
	{
		super(settings, ignoreElement);
		
		this.text = ignoreElement.attributeValue("text");
	}
	
	@Override
	protected void saveToDOM() {
		// TODO Auto-generated method stub

	}

	@Override
	protected String toStormfrontMarkup() {
		// TODO Auto-generated method stub
		return null;
	}

	public Pattern getRegex() {
		if(regex == null) {
			String regText = Pattern.quote(text);
			if(!matchPartialWord)
				regText = "\\b" + regText + "\\b";
			int flags = 0;
			if(ignoreCase)
				flags |= Pattern.CASE_INSENSITIVE;
			regex = Pattern.compile(regText, flags);
		}

		return regex;
	}

	public void setText(String text) {
		this.text = text;
		regex = null;
	}

	public void setMatchPartialWord(boolean matchPartialWord) {
		this.matchPartialWord = matchPartialWord;
		regex = null;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
		regex = null;
	}

}
