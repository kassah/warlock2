/*
 * Created on Mar 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.rcp.ui.macros;

import java.util.Collection;

import cc.warlock.core.client.IWarlockClientViewer;

/**
 * @author Marshall
 *
 * A macro represents a certain key code / modifier combination and the list of handlers (ordered by precedence) that are able/willing to handle the macro.
 */
public interface IMacro {

	public static final int NO_MODIFIERS = 0;
	
	public int getKeyCode ();
	public void setKeyCode (int keycode);
	
	public int getModifiers ();
	public void setModifiers (int modifiers);
	
	public void addHandler (IMacroHandler handler);
	public void removeHandler (IMacroHandler handler);
	
	public Collection<IMacroHandler> getHandlers();
	
	public void execute(IWarlockClientViewer viewer);
	
	public Object getUserData();
	public void setUserData(Object object);
}
