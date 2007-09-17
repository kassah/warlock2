/*
 * Created on Jan 15, 2005
 */
package cc.warlock.core.client;

/**
 * @author Marshall
 */
public interface ICompassListener {
	public void directionEnabled (int direction);
	public void directionDisabled (int direction);
	public void compassCleared ();
	
	public void setCompass (ICompass compass);
}
