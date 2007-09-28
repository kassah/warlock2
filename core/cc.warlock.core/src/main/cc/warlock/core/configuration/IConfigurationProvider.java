package cc.warlock.core.configuration;

import java.util.List;

import org.dom4j.Element;

public interface IConfigurationProvider {
	
	public boolean supportsElement (Element element);
	
	public void parseElement (Element element);
	
	public List<Element> getTopLevelElements();
}
