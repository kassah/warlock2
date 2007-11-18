package cc.warlock.core.test;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import junit.framework.TestSuite;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import cc.warlock.core.configuration.Profile;
import cc.warlock.core.configuration.SavedProfiles;
import cc.warlock.core.configuration.ConfigurationUtil;
import cc.warlock.core.stormfront.network.ISGEConnectionListener;
import cc.warlock.core.stormfront.network.SGEConnection;

public class SGETest {
	protected Hashtable<String,Boolean> success = new Hashtable<String,Boolean>();

	@Test(timeout=30000)
	public void testAutoLogin() {
		List<String> profileNames = TestProperties.getList(TestProperties.PROFILE_NAMES);
		if (profileNames == null) {
			TestProperties.failProperty(TestProperties.PROFILE_NAMES);
		}
		
		for (final String profileName : profileNames)
		{
			Profile profile = SavedProfiles.getProfileByCharacterName(profileName);
			Assert.assertNotNull("Profile described by \"" + profileName + "\" was null!", profile);
			
			success.put(profileName, false);
			TestUtil.autoLogin(profile, new ISGEConnectionListener(){
				public void charactersReady(SGEConnection connection, Map<String, String> characters) {}
				public void gamesReady(SGEConnection connection, Map<String, String> games) {}
				public void loginFinished(SGEConnection connection, int status) {}
				public void loginReady(SGEConnection connection) {}
				public void readyToPlay(SGEConnection connection, java.util.Map<String,String> loginProperties) {
					success.put(profileName, true);
				};
			});
			
			Assert.assertTrue(success.get(profileName));
		}
	}

}
