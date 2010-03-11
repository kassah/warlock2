/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package cc.warlock.core.test;

import java.util.Hashtable;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cc.warlock.core.stormfront.network.SGEConnection;
import cc.warlock.core.stormfront.network.SGEConnectionListener;
import cc.warlock.core.stormfront.preferences.StormFrontPreferences;
import cc.warlock.core.stormfront.preferences.StormFrontProfileProvider;
import cc.warlock.core.stormfront.profile.StormFrontProfile;

public class SGETest {
	protected Hashtable<String,Boolean> success = new Hashtable<String,Boolean>();

	@Test(timeout=30000)
	public void testAutoLogin() {
		List<String> profileNames = TestProperties.getList(TestProperties.PROFILE_NAMES);
		if (profileNames == null) {
			TestProperties.failProperty(TestProperties.PROFILE_NAMES);
		}
		
		for (final String profileName : profileNames) {
			StormFrontProfile profile = StormFrontProfileProvider.getInstance().getByName(profileName);
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
