package cc.warlock.core.test;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import cc.warlock.core.configuration.Profile;
import cc.warlock.core.configuration.SavedProfiles;
import cc.warlock.core.stormfront.client.internal.StormFrontClient;
import cc.warlock.core.stormfront.network.SGEConnection;
import cc.warlock.core.stormfront.serversettings.server.IServerSettingsListener;
import cc.warlock.core.stormfront.serversettings.server.ServerScript;
import cc.warlock.core.stormfront.serversettings.server.ServerSettings;

public class ServerScriptTest {

	public static final String script =
		"#Sample script\nlabel:\nput exp\nmove n\nmove e\nmove w\nmove s\nmove ne\nmove nw\nmove se\nmove sw\nput look\nmatch asdf";
	
	protected static SGEConnection sgeConnection;
	protected static StormFrontClient client;
	
	@BeforeClass
	public static void setUpBeforeClass ()
	{
		if (TestProperties.getBoolean(TestProperties.DO_CONNECTED_TESTS))
		{
			String defaultProfile = TestProperties.getString(TestProperties.DEFAULT_PROFILE);
			if (defaultProfile == null)
			{
				TestProperties.failProperty(TestProperties.DEFAULT_PROFILE);
			}
			
			Profile profile = SavedProfiles.getProfileByCharacterName(defaultProfile);
			SettingsListener listener = new SettingsListener();
			client = TestUtil.autoConnectToClient(profile, listener);
			
			// make sure server settings are loaded before we load scripts
			Assert.assertNotNull(client);
			while (!listener.loaded) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					Assert.fail(e.getMessage());
				}
			}
		}
	}
	
	private static class SettingsListener implements IServerSettingsListener
	{
		public boolean loaded = false;
		public void serverSettingsLoaded(ServerSettings settings) {
			loaded = true;
		}
	}
	
	@AfterClass
	public static void tearDownAfterClass ()
	{
		client = null;
	}
	
	@Test
	public void testSimpleScript ()
	{
		String tokens = ServerScript.convertScriptToTokens(script);
		String script2 = ServerScript.convertTokensToScript(tokens);
		
//		System.out.println("tokens="+tokens);
//		System.out.println("script="+script);
//		System.out.println("script2="+script2);
		
		Assert.assertEquals(script, script2);
	}
	
	@Test
	public void testCharacterScripts()
	{
		for (ServerScript script : client.getServerSettings().getAllServerScripts())
		{
			System.out.println("testing script: " + script.getName());
			
			String originalScript = script.getScriptContents();
			String originalTokens = script.getTokens();
			
			System.out.println("our script='"+originalScript+"'");
			
			System.out.println("ours = '" + ServerScript.convertScriptToTokens(originalScript, false) + "'\ntheirs='" + originalTokens + "'\n");
			Assert.assertEquals(ServerScript.convertScriptToTokens(originalScript, false), originalTokens);
			Assert.assertEquals(ServerScript.convertTokensToScript(originalTokens), originalScript);
		}
	}
}
