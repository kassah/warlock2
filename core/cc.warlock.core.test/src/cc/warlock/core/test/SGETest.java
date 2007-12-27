package cc.warlock.core.test;

import java.util.Hashtable;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cc.warlock.core.configuration.Profile;
import cc.warlock.core.stormfront.ProfileConfiguration;
import cc.warlock.core.stormfront.network.SGEConnection;
import cc.warlock.core.stormfront.network.SGEConnectionListener;

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
			Profile profile = ProfileConfiguration.instance().getProfileByCharacterName(profileName);
			Assert.assertNotNull("Profile described by \"" + profileName + "\" was null!", profile);
			
			success.put(profileName, false);
			TestUtil.autoLogin(profile, new SGEConnectionListener(){
				public void readyToPlay(SGEConnection connection, java.util.Map<String,String> loginProperties) {
					success.put(profileName, true);
				};
			});
			
			Assert.assertTrue(success.get(profileName));
		}
	}

}
