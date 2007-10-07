package cc.warlock.core.stormfront.xml;

import java.util.ArrayList;
import java.util.List;

public class StormFrontAttributeList {

	protected List<StormFrontAttribute> attributes = new ArrayList<StormFrontAttribute>();
	
	public void addAttribute (StormFrontAttribute attribute)
	{
		attributes.add(attribute);
	}
	
	public void clear ()
	{
		attributes.clear();
	}
	
	public StormFrontAttribute getAttribute (String attributeName)
	{
		for (StormFrontAttribute attribute : attributes)
		{
			if (attribute.getName().equals(attributeName))
			{
				return attribute;
			}
		}
		return null;
	}
	
	public String getValue (String attributeName)
	{
		StormFrontAttribute attr = getAttribute(attributeName);
		if (attr != null)
		{
			return attr.getValue();
		}
		return null;
	}
	
	public List<StormFrontAttribute> getList()
	{
		return attributes;
	}
}
