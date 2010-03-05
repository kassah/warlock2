package cc.warlock.core.stormfront.preferences;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.settings.WarlockPreferenceArrayProvider;
import cc.warlock.core.stormfront.profile.StormFrontProfile;

public class StormFrontProfileProvider extends
		WarlockPreferenceArrayProvider<StormFrontProfile> {

	@Override
	protected StormFrontProfile get(Preferences node) {
		// TODO Auto-generated method stub
		StormFrontProfile profile = new StormFrontProfile();
		profile.setId(node.get("id", null));
		profile.setName(node.get("name", null));
		profile.setGameCode(node.get("gameCode", null));
		profile.setGameName(node.get("gameName", null));
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
	}

}
