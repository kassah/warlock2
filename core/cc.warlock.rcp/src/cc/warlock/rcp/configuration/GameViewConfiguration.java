package cc.warlock.rcp.configuration;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.jface.resource.JFaceResources;

import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.configuration.IConfigurationProvider;
import cc.warlock.core.configuration.WarlockConfiguration;

public class GameViewConfiguration implements IConfigurationProvider {

	protected int bufferLines = 1000;
	protected WarlockColor defaultBackground, defaultForeground, defaultEchoBackground, defaultEchoForeground;
	protected String defaultFontFace;
	protected int defaultFontSize;
	
	protected static GameViewConfiguration _instance;
	
	public static GameViewConfiguration instance()
	{
		if (_instance == null) _instance = new GameViewConfiguration();
		return _instance;
	}
	
	protected GameViewConfiguration ()
	{
		defaultBackground = new WarlockColor(25, 25, 50);
		defaultForeground = new WarlockColor(240, 240, 255);
		defaultFontFace = JFaceResources.getDefaultFont().getFontData()[0].getName();
		defaultFontSize = JFaceResources.getDefaultFont().getFontData()[0].getHeight();
		defaultEchoBackground = new WarlockColor(64, 64, 64);
		defaultEchoForeground = new WarlockColor(255, 255, 255);
		
		WarlockConfiguration.getMainConfiguration().addConfigurationProvider(this);
	}
	
	public List<Element> getTopLevelElements() {
		ArrayList<Element> elements = new ArrayList<Element>();
		Element gameView = DocumentHelper.createElement("game-view");
		gameView.addAttribute("buffer", bufferLines+"");
		
		Element defaultColors = DocumentHelper.createElement("default-colors");
		gameView.add(defaultColors);
		
		defaultColors.addAttribute("background", defaultBackground.toHexString());
		defaultColors.addAttribute("foreground", defaultForeground.toHexString());
		defaultColors.addAttribute("echo-background", defaultEchoBackground.toHexString());
		defaultColors.addAttribute("echo-foreground", defaultEchoForeground.toHexString());
		
		Element defaultFont = DocumentHelper.createElement("default-font");
		gameView.add(defaultFont);
		defaultFont.addAttribute("face", defaultFontFace);
		defaultFont.addAttribute("size", defaultFontSize+"");
		
		elements.add(gameView);
		return elements;
	}

	public void parseElement(Element element) {
		if (element.getName().equals("game-view"))
		{
			bufferLines = Integer.parseInt(element.attributeValue("buffer"));
			
			for (Element e : (List<Element>)element.elements())
			{
				if (e.getName().equals("default-colors"))
				{
					defaultBackground = new WarlockColor(e.attributeValue("background"));
					defaultForeground = new WarlockColor(e.attributeValue("foreground"));
					defaultEchoBackground = new WarlockColor(e.attributeValue("echo-background"));
					defaultEchoForeground = new WarlockColor(e.attributeValue("echo-foreground"));
				}
				else if (e.getName().equals("default-font"))
				{
					defaultFontFace = e.attributeValue("face");
					defaultFontSize = Integer.parseInt(e.attributeValue("size"));
				}
			}
		}
	}

	public boolean supportsElement(Element element) {
		if (element.getName().equals("game-view"))
		{
			return true;
		}
		return false;
	}

	public int getBufferLines() {
		return bufferLines;
	}

	public void setBufferLines(int bufferLines) {
		this.bufferLines = bufferLines;
	}

	public WarlockColor getDefaultBackground() {
		return defaultBackground;
	}

	public void setDefaultBackground(WarlockColor defaultBackground) {
		this.defaultBackground = defaultBackground;
	}

	public WarlockColor getDefaultForeground() {
		return defaultForeground;
	}

	public void setDefaultForeground(WarlockColor defaultForeground) {
		this.defaultForeground = defaultForeground;
	}

	public WarlockColor getDefaultEchoBackground() {
		return defaultEchoBackground;
	}

	public void setDefaultEchoBackground(WarlockColor defaultEchoBackground) {
		this.defaultEchoBackground = defaultEchoBackground;
	}

	public WarlockColor getDefaultEchoForeground() {
		return defaultEchoForeground;
	}

	public void setDefaultEchoForeground(WarlockColor defaultEchoForeground) {
		this.defaultEchoForeground = defaultEchoForeground;
	}

	public String getDefaultFontFace() {
		return defaultFontFace;
	}

	public void setDefaultFontFace(String defaultFontFace) {
		this.defaultFontFace = defaultFontFace;
	}

	public int getDefaultFontSize() {
		return defaultFontSize;
	}

	public void setDefaultFontSize(int defaultFontSize) {
		this.defaultFontSize = defaultFontSize;
	}

}
