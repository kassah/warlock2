package cc.warlock.core.stormfront;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cc.warlock.core.configuration.Account;
import cc.warlock.core.configuration.IConfigurationProvider;
import cc.warlock.core.configuration.Profile;
import cc.warlock.core.configuration.WarlockConfiguration;

public class ProfileConfiguration implements IConfigurationProvider {
	
	public static final String PROFILE_CONFIGURATION_FILE = "profiles.xml";
	
	protected static ProfileConfiguration _instance;
	
	public static ProfileConfiguration instance() {
		if (_instance == null) _instance = new ProfileConfiguration();
		return _instance;
	}
	
	public static WarlockConfiguration getProfileConfiguration ()
	{
		return WarlockConfiguration.getWarlockConfiguration(PROFILE_CONFIGURATION_FILE);
	}
	
	protected Hashtable<String, Account> accounts = new Hashtable<String, Account>();
	
	protected ProfileConfiguration () {
		WarlockConfiguration.getWarlockConfiguration(PROFILE_CONFIGURATION_FILE).addConfigurationProvider(this);
	}
	
	public List<Element> getTopLevelElements() {
		ArrayList<Element> elements = new ArrayList<Element>();
		
		for (Account account : accounts.values())
		{
			Element aElement = DocumentHelper.createElement("account");
			aElement.addAttribute("name", account.getAccountName());
			aElement.addAttribute("password", Account.encryptPassword(account.getPassword()));
			
			for (Profile profile : account.getProfiles())
			{
				Element pElement = aElement.addElement("profile");
				pElement.addAttribute("id", profile.getId());
				pElement.addAttribute("name", profile.getName());
				pElement.addAttribute("gameCode", profile.getGameCode());
				pElement.addAttribute("gameName", profile.getGameName());
			}
			elements.add(aElement);
		}
		return elements;
	}

	public void parseElement(Element element) {
		if (element.getName().equals("account"))
		{
			String accountName = element.attributeValue("name");
			String password = element.attributeValue("password");
			
			Account account = new Account(accountName, Account.decryptPassword(password));
			accounts.put(accountName, account);
			
			for (Element pElement : (List<Element>)element.elements())
			{
				String profileId = pElement.attributeValue("id");
				String profileName = pElement.attributeValue("name");
				String gameCode = pElement.attributeValue("gameCode");
				String gameName = pElement.attributeValue("gameName");
				
				Profile profile = new Profile();
				profile.setAccount(account);
				profile.setId(profileId);
				profile.setName(profileName);
				profile.setGameCode(gameCode);
				profile.setGameName(gameName);
				
				account.getProfiles().add(profile);
			}
		}
	}

	public boolean supportsElement(Element element) {
		if (element.getName().equals("account") || element.getName().equals("profile"))
		{
			return true;
		}
		return false;
	}
	
	public void addAccount (Account account)
	{
		accounts.put(account.getAccountName(), account);
	}
	
	public void removeAccount (Account account)
	{
		if (accounts.containsKey(account.getAccountName()))
		{
			accounts.remove(account.getAccountName());
		}
	}

	public Account getAccount (String accountName)
	{
		if (accounts.containsKey(accountName))
			return accounts.get(accountName);
		return null;
	}
	
	public Collection<Account> getAllAccounts ()
	{
		return accounts.values();
	}
	
	public Collection<Profile> getAllProfiles ()
	{
		ArrayList<Profile> allProfiles = new ArrayList<Profile>();
		for (Account acct : getAllAccounts())
		{
			allProfiles.addAll(acct.getProfiles());
		}
		
		return allProfiles;
	}
	
	public Profile getProfileByCharacterName (String characterName)
	{
		Collection<Profile> profiles = getAllProfiles();
		
		for (Profile profile : profiles)
		{
			if (profile.getName().equals(characterName))
				return profile;
		}
		return null;
	}
}
