package cc.warlock.core.stormfront.preferences;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.settings.WarlockPreferenceArrayProvider;
import cc.warlock.core.stormfront.StormFrontCharacterProfile;

public class StormFrontCharacterProfileProvider extends
		WarlockPreferenceArrayProvider<StormFrontCharacterProfile> {

	@Override
	protected StormFrontCharacterProfile get(Preferences node) {
		// TODO Auto-generated method stub
		StormFrontCharacterProfile profile = new StormFrontCharacterProfile();
		profile.setUsername(node.get("username", null));
		profile.setPassword(node.get("password", null));
		profile.setId(node.get("id", null));
		profile.setName(node.get("name", null));
		profile.setGameCode(node.get("gameCode", null));
		profile.setGameName(node.get("gameName", null));
		return profile;
	}

	@Override
	protected String getNodeName() {
		return "sfCharacterProfile";
	}

	@Override
	protected void set(Preferences node, StormFrontCharacterProfile value) {
		node.put("username", value.getUsername());
		node.put("password", value.getPassword());
		node.put("id", value.getId());
		node.put("name", value.getName());
		node.put("gameCode", value.getGameCode());
		node.put("gameName", value.getGameName());
	}

}
