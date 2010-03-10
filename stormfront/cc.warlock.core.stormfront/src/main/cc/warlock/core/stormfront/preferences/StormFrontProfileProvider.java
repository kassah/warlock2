package cc.warlock.core.stormfront.preferences;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.settings.WarlockPreferenceArrayProvider;
import cc.warlock.core.client.settings.WarlockPreferences;
import cc.warlock.core.stormfront.profile.StormFrontProfile;

public class StormFrontProfileProvider extends
		WarlockPreferenceArrayProvider<StormFrontProfile> {
	
	private static final StormFrontProfileProvider instance =
		new StormFrontProfileProvider();
	
	private StormFrontProfileProvider() { }
	
	public static StormFrontProfileProvider getInstance() {
		return instance;
	}
	
	@Override
	protected StormFrontProfile get(Preferences node) {
		StormFrontProfile profile = new StormFrontProfile();
		profile.setId(node.get("id", null));
		profile.setName(node.get("name", null));
		profile.setGameCode(node.get("gameCode", null));
		profile.setGameName(node.get("gameName", null));
		profile.setGameViewId(node.get("gameViewId", null));
		return profile;
	}

	@Override
	protected String getNodeName() {
		return "profile";
	}

	@Override
	protected void set(Preferences node, StormFrontProfile value) {
		node.put("id", value.getId());
		node.put("name", value.getName());
		node.put("gameCode", value.getGameCode());
		node.put("gameName", value.getGameName());
		node.put("gameViewId", value.getGameViewId());
	}

	public StormFrontProfile getByGameViewId(String gameViewId) {
		for(StormFrontProfile profile : getAll(WarlockPreferences.getInstance())) {
			if(profile.getGameViewId() == gameViewId)
				return profile;
		}
		return null;
	}
	
	public StormFrontProfile getByName(String name) {
		for(StormFrontProfile profile : getAll(WarlockPreferences.getInstance())) {
			if(profile.getName() == name)
				return profile;
		}
		return null;
	}
}
