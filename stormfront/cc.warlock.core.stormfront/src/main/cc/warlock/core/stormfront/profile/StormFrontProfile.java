package cc.warlock.core.stormfront.profile;

import cc.warlock.core.profile.Profile;

public class StormFrontProfile extends Profile {
	private String id = "";
	private String gameCode = "";
	private String gameName = "";
	private String gameViewId = null;
	private StormFrontAccount account = null;
	
	public StormFrontProfile() {
		// Defaults
	}
	
	public StormFrontProfile(String username, String password, String id, String name, String gameCode, String gameName) {
		super(name);
		this.id = id;
		this.gameCode = gameCode;
		this.gameName = gameName;
	}
	
	public StormFrontProfile(StormFrontProfile other) {
		super(other);
		this.account = other.account; // We actually want to link the account object together.
		this.id = (other.id == null) ? null : new String(other.id);
		this.gameCode = (other.gameCode == null) ? null : new String(other.gameCode);
		this.gameName = (other.gameName == null) ? null : new String(other.gameName);
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param gameCode the gameCode to set
	 */
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	/**
	 * @return the gameCode
	 */
	public String getGameCode() {
		return gameCode;
	}

	/**
	 * @param gameName the gameName to set
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	/**
	 * @return the gameName
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(StormFrontAccount account) {
		this.account = account;
	}

	/**
	 * @return the account
	 */
	public StormFrontAccount getAccount() {
		return account;
	}
	
	public void setGameViewId(String gameViewId) {
		this.gameViewId = gameViewId;
	}
	
	public String getGameViewId() {
		return gameViewId;
	}
}
