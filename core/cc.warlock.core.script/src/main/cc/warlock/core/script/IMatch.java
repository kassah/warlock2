/**
 * 
 */
package cc.warlock.core.script;


public interface IMatch
{
	
	/**
	 * Matches the match against the text
	 * @param text Text to match against
	 * @return whether or not there was a match
	 */
	public boolean matches(String text);
	
	public String getText();
	
}