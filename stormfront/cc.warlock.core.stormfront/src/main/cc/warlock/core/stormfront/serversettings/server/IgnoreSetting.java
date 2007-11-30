package cc.warlock.core.stormfront.serversettings.server;

import cc.warlock.core.stormfront.xml.StormFrontElement;

public class IgnoreSetting extends ServerSetting {

	protected String text;
	protected boolean matchPartialWord;
	protected boolean ignoreCase;
	
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isMatchPartialWord() {
		return matchPartialWord;
	}

	public void setMatchPartialWord(boolean matchPartialWord) {
		this.matchPartialWord = matchPartialWord;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

}
