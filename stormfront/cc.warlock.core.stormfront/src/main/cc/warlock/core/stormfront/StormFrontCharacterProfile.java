package cc.warlock.core.stormfront;

public class StormFrontCharacterProfile {

	private String username = "";
	private String password = "";
	private String id = "";
	private String name = "";
	private String gameCode = "";
	private String gameName = "";
	
	public StormFrontCharacterProfile() {
		// Defaults
	}
	
	public StormFrontCharacterProfile(String username, String password, String id, String name, String gameCode, String gameName) {
		this.setUsername(username);
		this.setPassword(password);
		this.setId(id);
		this.setName(name);
		this.setGameCode(gameCode);
		this.setGameName(gameName);
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
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
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
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
}
