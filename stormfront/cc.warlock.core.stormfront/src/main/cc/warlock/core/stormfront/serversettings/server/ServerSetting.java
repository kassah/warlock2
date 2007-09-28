package cc.warlock.core.stormfront.serversettings.server;

import org.dom4j.Element;

public abstract class ServerSetting {

	public static final String UPDATE_PREFIX = "<<m>";
	public static final String UPDATE_SUFFIX = "</<m>";
	public static final String DELETE_PREFIX = "<<d>";
	public static final String DELETE_SUFFIX = "</<d>";
	public static final String ADD_PREFIX = "<<a>";
	public static final String ADD_SUFFIX = "</<a>";
	
	/// what the hell does R stand for?
	public static final String R_PREFIX = "<<r>";
	public static final String R_SUFFIX = "</<r>";
	
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
	
	protected void deleteFromDOM ()
	{
		element.getParent().remove(element);
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
