package cc.warlock.core.stormfront.network;


public interface ISGEGame {

	public static enum AccountStatus {
		Expired, Trial, Normal, Unknown
	};
	
	public enum GameURL {
		Root("ROOT"), Marketing("MKTG"), Main("MAIN"), GameInfo("GAMEINFO"),
		PlayInfo("PLAYINFO"), MessageBoard("MSGBRD"), Chat("CHAT"),
		Files("FILES"), BillingFAQ("BILLINGFAQ"), BillingOptions("BILLINGOPTIONS"),
		BillingInfo("BILLINGINFO"), Games("GAMES"), Feedback("FEEDBACK"),
		Signup("SIGNUP"), SignupAgain("SIGNUPA");
		
		protected String key;
		GameURL (String key)
		{
			this.key = key;
		}
		
		public String key() { return key; }
		
		public static GameURL getURL (String key)
		{
			for (GameURL url : values()) {
				if (url.key().equals(key)) {
					return url;
				}
			}
			return null;
		}
	};
	
	public String getGameCode();
	
	public String getGameName();
	
	public AccountStatus getAccountStatus();
	
	public int getAccountStatusInterval();

	public String getGameURL(GameURL url);
}
