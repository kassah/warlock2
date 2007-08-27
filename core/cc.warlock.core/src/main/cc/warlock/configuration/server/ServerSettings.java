package cc.warlock.configuration.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cc.warlock.client.IWarlockClientViewer;
import cc.warlock.client.stormfront.IStormFrontClient;
import cc.warlock.client.stormfront.IStormFrontClientViewer;
import cc.warlock.client.stormfront.WarlockColor;
import cc.warlock.configuration.WarlockConfiguration;
import cc.warlock.configuration.skin.DefaultSkin;
import cc.warlock.configuration.skin.IWarlockSkin.ColorType;
import cc.warlock.configuration.skin.IWarlockSkin.FontFaceType;
import cc.warlock.configuration.skin.IWarlockSkin.FontSizeType;


public class ServerSettings implements Comparable<ServerSettings>
{

	public static final String PRESET_OR_STRING_SETTING_UPDATE_PREFIX = "<stgupd>";
	public static final String IGNORES_TEXT = "<<m><ignores disable=\"n\"></ignores><ignores disable=\"n\"></ignores></<m>";
	
	private IStormFrontClient client;
	private String playerId;
	private Document document;
	protected Palette palette;
	protected Hashtable<String, Preset> presets = new Hashtable<String,Preset>();
	protected Hashtable<String, HighlightString> highlightStrings = new Hashtable<String, HighlightString>();
	protected Hashtable<String, String> variables = new Hashtable<String, String>();
	protected ArrayList<ArrayList<MacroKey>> macroSets = new ArrayList<ArrayList<MacroKey>>();
	protected Hashtable<String, ServerScript> scripts = new Hashtable<String, ServerScript>();
	protected DefaultSkin defaultSkin;
	
	private Element mainWindowElement, mainWindowFontElement,
		mainWindowColumnFontElement, commandLineElement, paletteElement, presetsElement, stringsElement, namesElement;
	
	public static int getPixelSizeInPoints (int pixelSize)
	{
		// we'll assume 96 dpi for now
		double points = pixelSize * (72.0/96.0);
		return (int) Math.round(points);
	}
	
	public ServerSettings (IStormFrontClient client)
	{
		this.client = client;
	}
	
	public ServerSettings (IStormFrontClient client, String playerId)
	{
		this.client = client;
		
		load(playerId);
	}
	
	public void load (String playerId)
	{
		this.playerId = playerId;
		
		try {
			FileInputStream stream = new FileInputStream(WarlockConfiguration.getConfigurationFile("serverSettings_" + playerId + ".xml"));
			SAXReader reader = new SAXReader();
			document = reader.read(stream);
			
			mainWindowElement = (Element) document.selectSingleNode("/settings/stream/w[@id=\"smain\"]");
			mainWindowFontElement = (Element) mainWindowElement.selectSingleNode("font");
			mainWindowColumnFontElement = (Element) mainWindowElement.selectSingleNode("columnFont");
			commandLineElement = (Element) document.selectSingleNode("/settings/cmdline");
			
			loadPalette();
			loadPresets();
			loadHighlightStrings();
			loadVariables();
			loadMacros();
			loadScripts();
			
			// initalize before we call the viewers
			defaultSkin = new DefaultSkin(this);
			
			for (IWarlockClientViewer v : client.getViewers())
			{
				IStormFrontClientViewer viewer = (IStormFrontClientViewer) v;
				viewer.loadServerSettings(this);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadPalette ()
	{
		paletteElement = (Element) document.selectSingleNode("/settings/palette");
		palette = new Palette(this, paletteElement);
	}
	
	private void loadPresets ()
	{
		presetsElement = (Element) document.selectSingleNode("/settings/presets");
		if (presetsElement != null)
		{
			for (Object o : presetsElement.elements())
			{
				Element pElement = (Element) o;
				String presetId = pElement.attributeValue("id");
				
				presets.put(presetId, new Preset(this, pElement, palette));
			}
		}
	}
	
	private void loadHighlightStrings()
	{
		stringsElement = (Element) document.selectSingleNode("/settings/strings");
		if (stringsElement != null)
		{
			for (Object o : stringsElement.elements())
			{
				Element hElement = (Element) o;
				String text = hElement.attributeValue("text");
				
				highlightStrings.put(text, new HighlightString(this, hElement, palette));
			}
		}
		
		namesElement = (Element) document.selectSingleNode("/settings/names");
		if (namesElement != null)
		{
			for (Object o : namesElement.elements())
			{
				Element hElement = (Element) o;
				String text = hElement.attributeValue("text");
				
				highlightStrings.put(text, new HighlightString(this, hElement, palette));
				highlightStrings.get(text).setIsName(true);
			}
		}
	}
	
	private void loadVariables()
	{
		Element varsElement = (Element) document.selectSingleNode("/settings/vars");
		if (varsElement != null)
		{
			for (Object o : varsElement.elements())
			{
				Element varElement = (Element) o;
				variables.put(varElement.attributeValue("name"), varElement.attributeValue("value"));
			}
		}
	}
	
	private void loadMacros ()
	{
		Element macrosElement = (Element) document.selectSingleNode("/settings/macros");
		if (macrosElement != null)
		{
			for (Object o : macrosElement.elements())
			{
				Element keysElement = (Element) o;
				ArrayList<MacroKey> keys = new ArrayList<MacroKey>();
				macroSets.add(keys);
				
				for (Object k : keysElement.elements())
				{
					Element kElement = (Element) k;
					
					keys.add(new MacroKey(this, kElement.attributeValue("key"), kElement.attributeValue("action")));
				}
			}
		}
	}
	
	private void loadScripts ()
	{
		Element scriptsElement = (Element) document.selectSingleNode("/settings/scripts");
		if (scriptsElement != null)
		{
			for (Object s : scriptsElement.elements())
			{
				Element sElement = (Element) s;
				scripts.put(sElement.attributeValue("name"), new ServerScript(this, sElement));
			}
		}
	}
	
	public String getFontFaceSetting (FontFaceType settingType)
	{
		Element fontElement = null;
		
		switch (settingType)
		{
			case MainWindow_FontFace: fontElement = mainWindowFontElement; break;
			case MainWindow_MonoFontFace: fontElement = mainWindowColumnFontElement; break;
			case CommandLine_FontFace: fontElement = commandLineElement; break;
		}
		
		if (fontElement != null)
		{
			return fontElement.attributeValue("face");
		}
		
		return null;
	}
	
	public WarlockColor getColorSetting (ColorType settingType)
	{
		return getColorSetting(settingType, true);
	}
	
	public WarlockColor getColorSetting (ColorType settingType, boolean skinFallback)
	{
		String color = null;
		
		switch (settingType)
		{
			case MainWindow_Background: color = mainWindowElement.attributeValue("bgcolor"); break;
			case MainWindow_Foreground: color = mainWindowElement.attributeValue("fgcolor"); break;
			case CommandLine_Background: color = commandLineElement.attributeValue("bgcolor"); break;
			case CommandLine_Foreground: color = commandLineElement.attributeValue("fgcolor"); break;
			case CommandLine_BarColor: color = commandLineElement.attributeValue("barcolor"); break;
		}
		
		if (color == null) color = "skin";
		
		if (color.charAt(0) == '@')
		{
			WarlockColor paletteColor = palette.getPaletteColor(color.substring(1));
			paletteColor.addPaletteReference(this);
			return paletteColor;
		}
		else if ("skin".equals(color) && skinFallback)
		{
			return defaultSkin.getColor(settingType);
		}
		else if (color.charAt(0) == '#') {
			return new WarlockColor(color);
		}
		
		else return WarlockColor.DEFAULT_COLOR;
	}
	
	
	public int getFontSizeSetting (FontSizeType settingType)
	{
		Element fontElement = null;
		
		switch (settingType)
		{
			case MainWindow_FontSize: fontElement = mainWindowFontElement;
			case MainWindow_MonoFontSize: fontElement = mainWindowColumnFontElement;
		}
		
		if (fontElement != null)
		{
			return getPixelSizeInPoints(Integer.parseInt(fontElement.attributeValue("size")));
		}
		
		return defaultSkin.getFontSize(settingType);
	}
	
	public Palette getPalette ()
	{
		return palette;
	}
	
	public Preset getPreset (String presetId)
	{
		return presets.get(presetId);
	}
	
	public Collection<HighlightString> getHighlightStrings ()
	{	
		return highlightStrings == null ? null : highlightStrings.values();
	}
	
	public void clearHighlightStrings ()
	{
		highlightStrings.clear();
	}
	
	public void addHighlightString (HighlightString string)
	{
		highlightStrings.put(string.getText(), string);
	}
	
	protected void saveHighlights(boolean saveNames)
	{
		StringBuffer stringsMarkup = new StringBuffer();
		for (HighlightString string : highlightStrings.values())
		{
			if (saveNames && string.isName()) {
				if (string.needsUpdate())
				{
					if (string.getOriginalHighlightString() != null)
						stringsMarkup.append(string.getOriginalHighlightString().toStormfrontMarkup());
					
					stringsMarkup.append(string.toStormfrontMarkup());
					string.saveToDOM();
					string.setNeedsUpdate(false);
				}
			}
			else if (!saveNames && !string.isName()) {
				if (string.needsUpdate())
				{
					if (string.getOriginalHighlightString() != null)
						stringsMarkup.append(string.getOriginalHighlightString().toStormfrontMarkup());
					
					stringsMarkup.append(string.toStormfrontMarkup());
					string.saveToDOM();
					string.setNeedsUpdate(false);
				}
			}
		}
		
		// Palette changes are appended onto the end of highlight strings
		String paletteMarkup = "";
		if (palette.needsUpdate())
		{
			paletteMarkup = palette.toStormfrontMarkup();
		}
		
		if (stringsMarkup.length() > 0)
		{
			sendSettingsUpdate(
				PRESET_OR_STRING_SETTING_UPDATE_PREFIX +
				(saveNames ? 
					HighlightString.STORMFRONT_NAMES_MARKUP_PREFIX :
					HighlightString.STORMFRONT_STRINGS_MARKUP_PREFIX),
				stringsMarkup,
				(saveNames ?
					HighlightString.STORMFRONT_NAMES_MARKUP_SUFFIX :
					HighlightString.STORMFRONT_STRINGS_MARKUP_SUFFIX) +
				IGNORES_TEXT +
				paletteMarkup);
		}
		saveLocalXml();
	}
	
	public void saveHighlightStrings ()
	{
		saveHighlights(false);
	}
	
	public void saveHighlightNames ()
	{
		saveHighlights(true);
	}
	
	public void savePresets ()
	{
		StringBuffer presetMarkup = new StringBuffer();
		
		for (Preset preset : presets.values())
		{
			if (preset.needsUpdate())
			{
				presetMarkup.append(preset.getOriginalPreset().toStormfrontMarkup());
				presetMarkup.append(preset.toStormfrontMarkup());
				preset.saveToDOM();
				preset.setNeedsUpdate(false);
			}
		}
		
		if (presetMarkup.length() > 0)
		{
			sendSettingsUpdate(
				PRESET_OR_STRING_SETTING_UPDATE_PREFIX +
				Preset.STORMFRONT_MARKUP_PREFIX,
				presetMarkup,
				Preset.STORMFRONT_MARKUP_SUFFIX);
		}
		
		saveLocalXml();
	}
	
	protected void saveLocalXml ()
	{
		try {
			FileWriter writer = new FileWriter(WarlockConfiguration.getConfigurationFile("serverSettings_" + playerId + ".xml"));
			document.write(writer);
			writer.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	private void sendSettingsUpdate (String prefix, StringBuffer markup, String suffix)
	{
		if (markup.length() > 0)
		{
			System.out.println("[test-settings-update]\n\n" + prefix + markup.toString() + suffix);
			
			try {
				// pray for all of our souls
				client.getConnection().send(prefix + markup.toString() + suffix + "\n");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Preset createPreset ()
	{
		return Preset.createPresetFromParent(this, presetsElement);
	}
	
	public HighlightString createHighlightString (boolean isName)
	{
		HighlightString string = null;
		if (isName)
			string = HighlightString.createHighlightStringFromParent(this, namesElement);
		else
			string = HighlightString.createHighlightStringFromParent(this, stringsElement);
		
		string.setIsName(isName);
		return string;
	}
	
	public int compareTo(ServerSettings o) {
		if (this == o) return 0;
		return -1;
	}
	
	public DefaultSkin getDefaultSkin ()
	{
		return defaultSkin;
	}
	
	public boolean containsVariable (String name)
	{
		return variables.containsKey(name);
	}
	
	public String getVariable (String name)
	{
		return variables.get(name);
	}
	
	public Collection<String> getVariableNames ()
	{
		return variables.keySet();
	}
	
	public ArrayList<MacroKey> getMacroSet (int set)
	{
		return macroSets.get(set);
	}
	
	public boolean containsServerScript (String scriptName)
	{
		return scripts.containsKey(scriptName);
	}
	
	public ServerScript getServerScript (String scriptName)
	{
		return scripts.get(scriptName);
	}
}
