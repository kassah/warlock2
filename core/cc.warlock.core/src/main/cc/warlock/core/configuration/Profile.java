/*
 * Created on Dec 31, 2004
 */
package cc.warlock.core.configuration;

/**
 * @author Marshall
 */
public class Profile {

	private String id, name, gameCode, gameName;
	private Account account;
	
	public Profile () { }
	public Profile (Account account, String id, String name, String gameCode, String gameName)
	{
		this(id, name, gameCode, gameName);
		
		this.account = account;
		if (account != null && !account.getProfiles().contains(this)) {
			account.getProfiles().add(this);
		}
	}
	
	public Profile (String id, String name, String gameCode, String gameName)
	{
		this.id = id;
		this.name = name;
		this.gameCode = gameCode;
		this.gameName = gameName;
	}
	
	/**
	 * @return Returns the gameCode.
	 */
	public String getGameCode() {
		return gameCode;
	}
	/**
	 * @param gameCode The gameCode to set.
	 */
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	/**
	 * @return Returns the gameName.
	 */
	public String getGameName() {
		return gameName;
	}
	/**
	 * @param gameName The gameName to set.
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
