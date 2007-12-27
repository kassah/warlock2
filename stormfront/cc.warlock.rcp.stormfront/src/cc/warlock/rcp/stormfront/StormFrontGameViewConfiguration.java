package cc.warlock.rcp.stormfront;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cc.warlock.core.configuration.IConfigurationProvider;
import cc.warlock.core.configuration.Profile;
import cc.warlock.core.configuration.WarlockConfiguration;
import cc.warlock.core.stormfront.ProfileConfiguration;

public class StormFrontGameViewConfiguration implements IConfigurationProvider {

	protected HashMap<String, String> profileViewMappings = new HashMap<String, String>();
	
	protected static StormFrontGameViewConfiguration _instance;
	
	public static StormFrontGameViewConfiguration instance()
	{
		if (_instance == null) _instance = new StormFrontGameViewConfiguration();
		return _instance;
	}
	
	public StormFrontGameViewConfiguration ()
	{
		WarlockConfiguration.getMainConfiguration().addConfigurationProvider(this);
	}
	
	public List<Element> getTopLevelElements() {
		ArrayList<Element> elements = new ArrayList<Element>();
		
		for (Map.Entry<String,String> entry : profileViewMappings.entrySet())
		{
			Element profileView = DocumentHelper.createElement("profileView");
			profileView.addAttribute("viewId", entry.getKey());
			profileView.addAttribute("profileId", entry.getValue());
			elements.add(profileView);
		}
		
		return elements;
	}

	public void parseElement(Element element) {
		profileViewMappings.put(element.attributeValue("viewId"), element.attributeValue("profileId"));
	}

	public boolean supportsElement(Element element) {
		if (element.getName().equals("profileView")) {
			return true;
		}
		return false;
	}
	
	public String getProfileId (String viewId)
	{
		if (profileViewMappings.containsKey(viewId))
		{
			return profileViewMappings.get(viewId);
		}
		return null;
	}
	
	public Profile getProfileByViewId (String viewId)
	{
		if (profileViewMappings.containsKey(viewId))
		{
			String profileId = profileViewMappings.get(viewId);
			
			return ProfileConfiguration.instance().getProfileByCharacterName(profileId);
		}
		return null;
	}

	public void addProfileMapping (String viewId, String profileId)
	{
		profileViewMappings.put(viewId, profileId);
	}
}
