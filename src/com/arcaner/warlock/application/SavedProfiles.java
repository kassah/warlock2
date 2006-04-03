/*
 * Created on Dec 31, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.osgi.framework.Bundle;

import com.arcaner.warlock.plugin.Warlock2Plugin;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SavedProfiles {

	private static Properties props;
	
	private static Properties getProperties ()
		throws IOException, FileNotFoundException
	{
		if (props == null)
		{
			props = new Properties();
			
			Bundle bundle = Warlock2Plugin.getDefault().getBundle();
			URL url = bundle.getEntry("/profiles.properties");
			if (url == null) throw new FileNotFoundException();
			InputStream propStream = url.openStream();
			props.load(propStream);
		}
		return props;
	}

	public static Map getAccounts ()
		throws IOException
	{
		Properties props = getProperties();
		HashMap<String, String> accounts = new HashMap<String, String>();
		
		String savedAccounts = props.getProperty("warlock.saved.accounts");
		String savedAccountIds[];
		if( savedAccounts != null ) {
			savedAccountIds = savedAccounts.split(",");
		} else {
			savedAccountIds = new String[] { };
		}
		for (int i = 0; i < savedAccountIds.length; i++)
		{
			String savedAccountId = savedAccountIds[i];
			accounts.put(getAccountProperty(props, savedAccountId, "accountName"), getAccountProperty(props, savedAccountId, "password"));
		}
		
		return accounts;
	}
	
	public static List getProfiles ()
		throws IOException
	{
		Properties props = getProperties();
		ArrayList<Profile> profiles = new ArrayList<Profile>();
		
		String savedProfiles = props.getProperty("warlock.saved.profiles");
		String savedProfileIds[] = savedProfiles.split(",");
		
		for (int i = 0; i < savedProfileIds.length; i++)
		{
			String savedProfileId = savedProfileIds[i];
			Profile profile = new Profile();
			
			profile.setAccountName(getProfileProperty(props, savedProfileId, "accountName"));
			profile.setCharacterCode(getProfileProperty(props, savedProfileId, "characterCode"));
			profile.setCharacterName(getProfileProperty(props, savedProfileId, "characterName"));
			profile.setGameCode(getProfileProperty(props, savedProfileId, "gameCode"));
			profile.setGameName(getProfileProperty(props, savedProfileId, "gameName"));
			profiles.add(profile);
		}
		
		return profiles;
	}
	
	private static String getProfileProperty (Properties props, String profileId, String propertyName)
	{
		return props.getProperty("warlock.profile." + profileId + "." + propertyName);
	}
	
	private static String getAccountProperty (Properties props, String accountId, String propertyName)
	{
		return props.getProperty("warlock.account." + accountId + "." + propertyName);
	}
}
