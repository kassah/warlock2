/*
 * Created on Dec 31, 2004
 */
package cc.warlock.core.configuration;

/**
 * @author Marshall
 */
public class Profile {

	private String profileId, characterCode, characterName, gameCode, gameName;
	private Account account;
	
	/**
	 * @return Returns the characterCode.
	 */
	public String getCharacterCode() {
		return characterCode;
	}
	/**
	 * @param characterCode The characterCode to set.
	 */
	public void setCharacterCode(String characterCode) {
		this.characterCode = characterCode;
	}
	/**
	 * @return Returns the characterName.
	 */
	public String getCharacterName() {
		return characterName;
	}
	/**
	 * @param characterName The characterName to set.
	 */
	public void setCharacterName(String characterName) {
		this.characterName = characterName;
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
	public String getProfileId() {
		return profileId;
	}
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
}
