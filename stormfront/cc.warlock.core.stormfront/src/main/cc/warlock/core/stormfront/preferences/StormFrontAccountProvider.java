package cc.warlock.core.stormfront.preferences;

import java.util.Collection;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.settings.WarlockPreferenceArrayProvider;
import cc.warlock.core.stormfront.profile.StormFrontAccount;

public class StormFrontAccountProvider
	extends WarlockPreferenceArrayProvider<StormFrontAccount> {

	private static final StormFrontAccountProvider instance =
		new StormFrontAccountProvider();
	
	private StormFrontAccountProvider() { }
	
	public static StormFrontAccountProvider getInstance() {
		return instance;
	}
	
	@Override
	protected StormFrontAccount get(Preferences node) {
		// TODO Auto-generated method stub
		StormFrontAccount account = new StormFrontAccount();
		account.setAccountName(node.get("accountName", null));
		account.setPassword(node.get("password", null));
		return account;
	}

	@Override
	protected String getNodeName() {
		// TODO Auto-generated method stub
		return "account";
	}

	@Override
	protected void set(Preferences node, StormFrontAccount value) {
		// TODO Auto-generated method stub
		node.put("accountName", value.getAccountName());
		node.put("password", value.getPassword());
	}
	
	/**
	 * Look up account by account name.
	 * 
	 * @param prefs StormFrontPreferences object to read off of.
	 * @param accountname Name of Account
	 * @return the specified account, null if not found
	 */
	public StormFrontAccount getAccount(StormFrontPreferences prefs, String accountname) {
		for (StormFrontAccount acct: this.getAll(prefs)) {
			if (acct.getAccountName().equals(accountname)) {
				return acct;
			}
		}
		// No such account
		return null;
	}
}
