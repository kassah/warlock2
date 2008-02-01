/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
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
