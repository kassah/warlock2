/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package cc.warlock.rcp.stormfront;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cc.warlock.core.configuration.IConfigurationProvider;
import cc.warlock.core.configuration.WarlockConfiguration;
import cc.warlock.core.profile.Profile;
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
