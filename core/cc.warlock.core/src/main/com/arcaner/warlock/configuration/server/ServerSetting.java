package com.arcaner.warlock.configuration.server;

import org.dom4j.Element;

public abstract class ServerSetting {

	protected Element element;
	protected ServerSettings serverSettings;
	
	public ServerSetting (ServerSettings serverSettings)
	{
		this.serverSettings = serverSettings;
	}
	
	public ServerSetting (ServerSettings serverSettings, Element element)
	{
		this.element = element;
		this.serverSettings = serverSettings;
	}
	
	protected abstract void saveToDOM ();
	protected abstract String toStormfrontMarkup();
	
	protected void setAttribute (String attrName, String value)
	{
		setAttribute(element, attrName, value);
	}
	
	protected static void setAttribute (Element element, String attrName, String value)
	{
		if (element.attribute(attrName) == null)
		{
			element.addAttribute(attrName, value);
		} else {
			element.attribute(attrName).setValue(value);
		}
	}
}
