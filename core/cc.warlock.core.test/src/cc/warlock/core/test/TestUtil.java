package cc.warlock.core.test;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import org.junit.Assert;

import cc.warlock.core.configuration.Profile;
import cc.warlock.core.stormfront.client.internal.StormFrontClient;
import cc.warlock.core.stormfront.network.ISGEConnectionListener;
import cc.warlock.core.stormfront.network.SGEConnection;
import cc.warlock.core.stormfront.serversettings.server.IServerSettingsListener;

// the purpose of this class is to open connections / clients for unit tests and cache them so we don't
// login to our test profiles more than once in a given unit test session
public class TestUtil {


	protected static Hashtable<Profile, StormFrontClient> clients = new Hashtable<Profile, StormFrontClient>();
	protected static Hashtable<Profile, Map<String,String>> profileProperties = new Hashtable<Profile, Map<String,String>>();
	
	public static Map<String,String> autoLogin (Profile profile, ISGEConnectionListener listener)
	{
		if (!profileProperties.containsKey(profile))
		{
			profileProperties.put(profile, SGEConnection.autoLogin(profile, listener));
		}
		return profileProperties.get(profile);
	}
	
	public static StormFrontClient autoConnectToClient (Profile profile, IServerSettingsListener listener)
	{
		if (!clients.containsKey(profile))
		{
			Map<String,String> loginProperties = autoLogin(profile, null);
		
			StormFrontClient client = new StormFrontClient();
			if (listener != null)
				client.getServerSettings().addServerSettingsListener(listener);
			
			int port = Integer.parseInt(loginProperties.get(SGEConnection.PROPERTY_GAMEPORT));
			
			try {
				client.connect(loginProperties.get(SGEConnection.PROPERTY_GAMEHOST), port, loginProperties.get(SGEConnection.PROPERTY_KEY));
			} catch (IOException e) {
				Assert.fail(e.getMessage());
			}
			
			clients.put(profile, client);
		}
		
		return clients.get(profile);
	}
}
