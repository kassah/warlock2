package cc.warlock.core.stormfront.serversettings.skin;

import java.util.Map;

import cc.warlock.core.client.IWarlockSkin;
import cc.warlock.core.stormfront.client.StormFrontColor;
import cc.warlock.core.stormfront.serversettings.server.Preset;
import cc.warlock.core.stormfront.serversettings.server.ServerSettings;

public interface IStormFrontSkin extends IWarlockSkin {

	public void loadDefaultPresets (ServerSettings settings, Map<String, Preset> presets);
	
	public StormFrontColor getSkinForegroundColor (String presetId);
	public StormFrontColor getSkinBackgroundColor (String presetId);
	public StormFrontColor getStormFrontColor (ColorType type);
}
